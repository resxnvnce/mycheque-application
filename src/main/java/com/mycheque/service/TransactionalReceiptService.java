package com.mycheque.service;

import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.Objects;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.lang.Nullable;
import org.springframework.data.jpa.domain.Specification;

import com.mycheque.util.Maps;

import com.mycheque.domain.Receipt;
import com.mycheque.domain.id.FiscalDataRecord;

import com.mycheque.repository.ReceiptRepository;
import com.mycheque.repository.PurchaseRepository;

import com.mycheque.datatransfer.query.Patchnotes;
import com.mycheque.datatransfer.query.ReceiptQuery;
import com.mycheque.datatransfer.query.PurchaseQuery;
import com.mycheque.datatransfer.result.RequestedReceipt;

import com.mycheque.client.RequestBodyAttributes;
import com.mycheque.client.ClientTemplateException;
import com.mycheque.client.ResponseStatusCodeException;

import com.mycheque.service.filter.PatchStateTrackerFilter;
import com.mycheque.service.wrapper.AuthorizedWrapper;
import com.mycheque.service.wrapper.PatchnotesOutcome;
import com.mycheque.service.commons.PatchStateTracker;
import com.mycheque.service.context.PatchnotesContext;
import com.mycheque.service.context.PatchnotesContextSetup;
import com.mycheque.service.observer.support.ReceiptQueryObserver;
import com.mycheque.service.exception.IntegrationException;
import com.mycheque.service.exception.ObsoleteTokenException;
import com.mycheque.service.integrate.OncePerRequestIntegration;
import com.mycheque.service.integrate.OncePerRequestIntegrationProvider;

/**
 * The default {@link ReceiptService} implementation.
 *
 * @author resxnvnce
 */
@Service
@AllArgsConstructor // the constructor might be too large
public class TransactionalReceiptService implements ReceiptService {

    /**
     * The {@code Purchase} collection service.
     */
    private final PurchaseService purchaseService;

    /**
     * The accessor of the {@link Receipt}s persistence store.
     */
    private final ReceiptRepository receiptRepository;

    /**
     * The accessor of the {@code Purchase}s persistence store.
     */
    private final PurchaseRepository purchaseRepository;

    /**
     * A {@link PatchStateTracker} filter bean.
     */
    private final PatchStateTrackerFilter trackerFilter;

    /**
     * A {@link OncePerRequestIntegration} prototype bean factory.
     */
    private final OncePerRequestIntegrationProvider integrationProvider;

    @Override
    public List<RequestedReceipt> findAll(AuthorizedWrapper<ReceiptQuery> wrapper) {
        var spec = deriveSpecification(wrapper);
        var receipts = Maps.mapToIdentity(this.receiptRepository.findAll(spec).stream(), Receipt::getId);

        // might be null, but this case is covered further
        final PurchaseQuery query = wrapper.object().purchase();

        // the worst case scenario is an empty pool
        return receipts.isEmpty() ? List.of() : findAll(receipts, query);
    }

    /**
     * Derives a receipt {@code Specification} from an authorized wrapper.
     *
     * @param wrapper the {@code AuthorizedWrapper} to derive a specification from.
     * @return a derived specification, never {@code null}.
     */
    private Specification<Receipt> deriveSpecification(AuthorizedWrapper<ReceiptQuery> wrapper) {
        final ReceiptQuery query = wrapper.object();

        return Arrays.stream(ReceiptQueryObserver.VALUES)
                .map(
                        observer -> observer.deriveIfNecessary(query)
                )
                .filter(Objects::nonNull)
                .reduce(wrapper.customer().toReceiptSpecification(), Specification::and);
    }

    /**
     * Returns a {@code List} of requested receipts, mapped from the {@code Purchase}s found upon calling
     * the {@link PurchaseService#findAll(java.util.Collection, PurchaseQuery) findAll(pool, query)} method.
     *
     * @param receipts a map of {@code Receipt}s matched against their identifiers.
     * @param query    a query.
     * @return the requested receipts, might be blank.
     */
    private List<RequestedReceipt> findAll(Map<FiscalDataRecord, Receipt> receipts, @Nullable PurchaseQuery query) {
        this.purchaseService.findAll(receipts.keySet(), query).forEach(
                p -> {
                    var id = p.id();
                    receipts.get(id).setPurchase(p);
                }
        );

        return receipts.values().stream()
                .filter(r -> r.getPurchase() != null)
                .map(RequestedReceipt::mappedFrom)
                .toList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PatchnotesOutcome saveAll(AuthorizedWrapper<Patchnotes> wrapper) throws ReceiptServiceException {
        final var context = PatchnotesContextSetup.with(wrapper);

        /* Checks all the id-based definitions, possibly reducing the number of
         * calls to the external REST API, which is the most expensive operation atm. */
        prepareIntegration(context);

        /* Performs the integration for each tracker remaining and then
         * filters the entities such that each of them is worth to be saved. */
        executeIntegration(context);

        /* We do only save entities if there is no
         * 6xx Failures, following the method contract. */
        final boolean isSaveAllowed = !context.has6xxFailures();
        if (isSaveAllowed) {
            doSaveAll(context);
        }

        return context.toOutcome();
    }

    /**
     * Save all the remaining {@link Receipt}s to the persistence store, if it's possible.
     *
     * @param context the current {@link PatchnotesContext}.
     */
    private void doSaveAll(PatchnotesContext context) {
        final var toSave = context.getStateTrackers().stream()
                .map(PatchStateTracker::getEntityState)
                .toArray(Receipt[]::new);

        final var owner = context.getCustomer();

        for (Receipt receipt : toSave) {
            receipt.setCustomer(owner);
            this.receiptRepository.save(receipt);

            var purchase = receipt.getPurchase();
            this.purchaseRepository.save(purchase);

            receipt.setNew(false);
        }
    }

    /**
     * Prepare the patchnotes {@code context} before integration.
     *
     * @param context the current {@link PatchnotesContext}.
     */
    private void prepareIntegration(PatchnotesContext context) {
        for (var iterator = context.getStateTrackers().iterator(); iterator.hasNext(); ) {
            var tracker = iterator.next();

            boolean isRemovableTracker = this.trackerFilter.beforeIntegration(tracker, context);

            if (isRemovableTracker)
                iterator.remove();
        }
    }

    /**
     * Execute an integration for each {@code PatchStateTracker} within the patchnotes {@code context}.
     *
     * @param context the current {@link PatchnotesContext}.
     * @throws ReceiptServiceException in case of an internal error.
     */
    private void executeIntegration(PatchnotesContext context) {
        final var integration = toOncePerRequestIntegration(context);

        for (var iterator = context.getStateTrackers().iterator(); iterator.hasNext(); ) {
            var tracker = iterator.next();

            try {
                integration.retrieve(tracker);
            }
            catch (ClientTemplateException cte) {
                context.cleanUp();
                integration.cleanUp();
                throw toReceiptServiceException(cte);
            }

            boolean isRemovableTracker = this.trackerFilter.afterIntegration(tracker, context);

            if (isRemovableTracker)
                iterator.remove();
        }

        context.stealAll(integration);
    }

    /**
     * Convert the integration layer exception into a {@link ReceiptServiceException}.
     *
     * @param cte an exception thrown by the integration layer.
     * @return a service exception.
     */
    private ReceiptServiceException toReceiptServiceException(ClientTemplateException cte) {
        if (cte instanceof ResponseStatusCodeException.Unauthorized) {
            return new ObsoleteTokenException("Could not use the context customer's token.", cte);
        }

        return new IntegrationException(cte);
    }

    /**
     * Returns a {@link OncePerRequestIntegration} to be used within the current patchnotes context.
     *
     * @param context a patchnotes context to evaluate an integration within.
     * @return an integration prototype bean.
     */
    private OncePerRequestIntegration toOncePerRequestIntegration(PatchnotesContext context) {
        final var attributesFactory = RequestBodyAttributes.factory(
                context.getCustomer().getThirdpartyToken()
        );

        return this.integrationProvider.getPrototype(attributesFactory);
    }
}
