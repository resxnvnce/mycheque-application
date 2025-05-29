package com.mycheque.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mycheque.domain.Receipt;

import com.mycheque.repository.ReceiptRepository;
import com.mycheque.repository.PurchaseRepository;

import com.mycheque.client.RequestBodyAttributes;
import com.mycheque.client.ClientTemplateException;
import com.mycheque.client.ResponseStatusCodeException;

import com.mycheque.service.wrapper.PatchnotesOutcome;
import com.mycheque.service.wrapper.PatchnotesWrapper;
import com.mycheque.service.commons.PatchStateTracker;
import com.mycheque.service.context.PatchnotesContext;
import com.mycheque.service.context.PatchnotesContextSetup;
import com.mycheque.service.filter.PatchStateTrackerFilter;
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
public class TransactionalReceiptService implements ReceiptService {

    /**
     * The accessor of the {@link Receipt}s persistence store.
     */
    private final ReceiptRepository receiptRepository;

    /**
     * The accessor of the {@link com.mycheque.domain.Purchase Purchase}s persistence store.
     */
    private final PurchaseRepository purchaseRepository;

    /**
     * {@code PatchStateTracker} filtering support.
     */
    private final PatchStateTrackerFilter trackerFilter;

    /**
     * A {@link OncePerRequestIntegration} prototype bean factory.
     */
    private final OncePerRequestIntegrationProvider integrationProvider;

    /**
     * Constructs a {@code TransactionalReceiptService}.
     *
     * @param trackerFilter       a {@code PatchStateTracker} filtering support bean.
     * @param integrationProvider an integration prototypes bean factory.
     * @param receiptRepository   the {@code Receipt}s repository.
     * @param purchaseRepository  the {@code Purchase}s repository.
     */
    @Autowired
    public TransactionalReceiptService(PatchStateTrackerFilter trackerFilter,
                                       OncePerRequestIntegrationProvider integrationProvider,
                                       ReceiptRepository receiptRepository, PurchaseRepository purchaseRepository) {

        this.trackerFilter = trackerFilter;
        this.integrationProvider = integrationProvider;
        this.receiptRepository = receiptRepository;
        this.purchaseRepository = purchaseRepository;
    }

    /**
     * Establish a {@link OncePerRequestIntegration} within the current patchnotes context.
     *
     * @param context a patchnotes context to evaluate an integration within.
     * @return an integration prototype bean.
     */
    private OncePerRequestIntegration toIntegration(PatchnotesContext context) {
        final var attributesFactory = RequestBodyAttributes.factory(
                context.getCustomer().getThirdpartyToken()
        );

        return this.integrationProvider.getPrototype(attributesFactory);
    }

    /**
     * Convert the integration layer exception into a {@link ReceiptServiceException}.
     *
     * @param cte an exception thrown by the integration layer.
     * @return a service exception.
     */
    private ReceiptServiceException toServiceException(ClientTemplateException cte) {
        if (cte instanceof ResponseStatusCodeException.Unauthorized) {
            return new ObsoleteTokenException("Could not use the context customer's token.", cte);
        }

        return new IntegrationException(cte);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PatchnotesOutcome saveAllReceipts(PatchnotesWrapper wrapper) throws ReceiptServiceException {
        final var context = PatchnotesContextSetup.with(wrapper);

        /* Checks all the id-based definitions, possibly reducing the number of
         * calls to the external REST API, which is the most expensive operation atm. */
        beforeIntegration(context);

        /* Performs the integration for each tracker remaining and then
         * filters the entities such that each of them is worth to be saved. */
        executeIntegration(context);

        /* We do only save entities if there is no
         * 6xx Failures, following the method contract. */
        final boolean isSaveAllowed = !context.has6xxFailures();
        if (isSaveAllowed) {
            doSaveAllReceipts(context);
        }

        return context.toOutcome();
    }

    /**
     * Actually save all the {@link Receipt}s remaining within the patchnotes context.
     *
     * @param context the current {@link PatchnotesContext}.
     */
    private void doSaveAllReceipts(PatchnotesContext context) {
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
    private void beforeIntegration(PatchnotesContext context) {
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
     * @throws ReceiptServiceException in case of an internal error or if the context customer's token
     *         became obsolete.
     */
    private void executeIntegration(PatchnotesContext context) throws ReceiptServiceException {
        final var integration = toIntegration(context);

        for (var iterator = context.getStateTrackers().iterator(); iterator.hasNext(); ) {
            var tracker = iterator.next();

            try {
                integration.retrieve(tracker);
            }
            catch (ClientTemplateException cte) {
                context.cleanUp();
                integration.cleanUp();
                throw toServiceException(cte);
            }

            boolean isRemovableTracker = this.trackerFilter.afterIntegration(tracker, context);

            if (isRemovableTracker)
                iterator.remove();
        }

        context.stealAll(integration);
    }
}
