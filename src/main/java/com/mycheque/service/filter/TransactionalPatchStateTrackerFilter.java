package com.mycheque.service.filter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mycheque.domain.Receipt;
import com.mycheque.domain.Customer;
import com.mycheque.domain.id.FiscalDataRecord;

import com.mycheque.repository.ReceiptRepository;

import com.mycheque.service.commons.PatchStateTracker;
import com.mycheque.service.context.PatchnotesContext;

import com.mycheque.datatransfer.query.ReceiptDefinition;
import com.mycheque.datatransfer.intermediate.Remarkable;
import com.mycheque.datatransfer.intermediate.PatchRemark;
import com.mycheque.datatransfer.intermediate.PatchRemarkCode;

import static com.mycheque.util.Lambdas.applyOrNull;

/**
 * The default {@link PatchStateTrackerFilter} implementation.
 *
 * @author resxnvnce
 */
@Component
public class TransactionalPatchStateTrackerFilter implements PatchStateTrackerFilter {

    /**
     * The accessor of the {@link Receipt}s persistence store.
     */
    private final ReceiptRepository repository;

    /**
     * Constructs a {@code TransactionalPatchStateTrackerFilter}.
     *
     * @param repository the {@code Receipt}s repository.
     */
    public TransactionalPatchStateTrackerFilter(ReceiptRepository repository) {
        this.repository = repository;
    }

    /**
     * Resolve a {@link PatchRemarkCode} based on an {@linkplain Customer#getId() identifier}
     * of the current patchnotes context customer and an identifier found,
     * which might even be different.
     *
     * @param contextId the identifier of the current context customer.
     * @param foundId   an identifier found as a result of some persistence check.
     * @return a proper patch remark code.
     */
    private PatchRemarkCode toRemarkCode(long contextId, long foundId) {
        return contextId == foundId ? PatchRemark.ALREADY_PRESENT : PatchRemark.OCCUPIED;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    public boolean afterIntegration(PatchStateTracker tracker, PatchnotesContext context) {
        final var fiscal = applyOrNull(tracker.getEntityState(), Receipt::getId);
        if (fiscal == null) {
            return true;
        }

        final boolean isNewIdentifier = context.getIdentifiers().add(fiscal);
        if (isNewIdentifier) {
            return filterIfExists(fiscal, tracker, context);
        }

        return !tracker.isPersistent() && filterDuplicated(tracker, context);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    public boolean beforeIntegration(PatchStateTracker tracker, PatchnotesContext context) {
        if (tracker.getIntermediateState() instanceof ReceiptDefinition.ByDetails manual) {
            final var fiscal = manual.id();

            /* The first clause should never evaluate to true. */
            return !context.getIdentifiers().add(fiscal) || filterIfExists(fiscal, tracker, context);
        }

        return false;
    }

    /**
     * Populate the current {@code PatchnotesContext}
     * with a brand-new {@link Remarkable}, indicating
     * that a duplicate entity state has been encountered.
     * <p>
     * This method does always return {@code true}.
     *
     * @param tracker a tracker with an {@linkplain
     *                PatchStateTracker#getEntityState()
     *                entity state} that happens to be a duplicate.
     * @param context the current patchnotes context.
     * @return {@code true}, indicating that the {@code tracker} is <b>not</b> worth processing any further.
     */
    private boolean filterDuplicated(PatchStateTracker tracker, PatchnotesContext context) {
        var duplicated = Remarkable.of(tracker.toSource(), PatchRemark.DUPLICATED);
        context.remark(duplicated);
        return true;
    }

    /**
     * Is the {@code fiscal} identifier already persistent?
     *
     * @param fiscal  a {@code FiscalDataRecord} identifier to inspect.
     * @param tracker a tracker to use for building a {@code Remarkable}
     *                in case the {@code fiscal} is indeed already persistent.
     * @param context the current patchnotes context.
     * @return {@code true} if the {@code tracker} is <b>not</b> worth processing any further,
     *         {@code false} otherwise.
     */
    private boolean filterIfExists(FiscalDataRecord fiscal, PatchStateTracker tracker, PatchnotesContext context) {
        tracker.setPersistent();

        final Optional<Long> ownerIdentifier = this.repository.toOwnerReference(fiscal);
        ownerIdentifier.ifPresent(
                id -> {
                    var remarkCode = toRemarkCode(context.getCustomer().getId(), id);
                    var persistent = Remarkable.of(tracker.toSource(), remarkCode);

                    context.remark(persistent);
                }
        );

        return ownerIdentifier.isPresent();
    }
}
