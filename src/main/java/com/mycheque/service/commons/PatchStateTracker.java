package com.mycheque.service.commons;

import org.springframework.lang.Nullable;

import com.mycheque.domain.Receipt;
import com.mycheque.datatransfer.accept.ReceiptDefinition;

/**
 * Interface for tracking a {@link ReceiptDefinition}'s lifecycle.
 * <p>
 * The lifecycle starts from {@linkplain #toSource a source} (never {@code null}), which is
 * the definition originally sent by a customer. Then a definition might change its appearance.
 * Such changes are reflected by using the intermediate state {@linkplain #setIntermediateState
 * setter method}. And finally, a valid definition can be transformed into a {@link Receipt} entity,
 * which is obtainable via the entity state {@linkplain #getEntityState getter method}.
 *
 * @author resxnvnce
 * @apiNote By default, the tracker is not responsible for argument validation in setter methods.
 */
public interface PatchStateTracker {

    /**
     * Returns the source of the patch lifecycle,
     * i.e. the original {@link ReceiptDefinition} sent by a customer.
     *
     * @return the patch lifecycle source.
     */
    ReceiptDefinition toSource();

    /**
     * Marks this patch as a {@linkplain #isPersistent() persistent} one.
     */
    void setPersistent();

    /**
     * Has this patch been participating in a <i>transaction</i> already?
     *
     * @return {@code true} if this patch has been in a <i>transaction</i> at least once,
     *         {@code false} otherwise.
     */
    boolean isPersistent();

    /**
     * Set the intermediate state of this patch, which can be later retrieved
     * via the {@link #getIntermediateState()} instance method.
     *
     * @param intermediate the current data transfer state of the patch.
     */
    void setIntermediateState(ReceiptDefinition intermediate);

    /**
     * Returns the latest data transfer state of this patch, which is modifiable
     * via the {@linkplain #setIntermediateState setter method}.
     *
     * @return the latest data transfer state. May be {@code null} if the patch
     *         hasn't been processed yet.
     */
    @Nullable
    ReceiptDefinition getIntermediateState();

    /**
     * Set the entity state of this patch, which can be later retrieved
     * via the {@link #getEntityState()} instance method.
     *
     * @param entity the current entity state of the patch.
     */
    void setEntityState(Receipt entity);

    /**
     * Returns the latest entity state of this patch.
     * <p>
     * Note that the {@link Receipt} returned is not a copy,
     * so it's allowed to be modified after calling this method.
     * </p>
     *
     * @return the latest entity state. May be {@code null} if the entity representation
     *         hasn't been resolved yet.
     */
    @Nullable
    Receipt getEntityState();

    /**
     * Encapsulate the given {@code ReceiptDefinition} into a new tracker.
     *
     * @param source the receipt definition originally sent by a customer.
     * @return a default {@code PatchStateTracker} instance.
     */
    static PatchStateTracker ofSource(ReceiptDefinition source) {
        return new DefaultPatchStateTracker(source);
    }
}
