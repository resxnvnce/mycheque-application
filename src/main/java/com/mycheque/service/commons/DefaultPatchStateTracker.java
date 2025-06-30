package com.mycheque.service.commons;

import java.util.Objects;

import org.springframework.lang.Nullable;

import com.mycheque.domain.Receipt;
import com.mycheque.datatransfer.query.ReceiptDefinition;

/**
 * The {@link PatchStateTracker} default implementation.
 *
 * @author resxnvnce
 */
public final class DefaultPatchStateTracker implements PatchStateTracker {

    /**
     * @see PatchStateTracker#toSource()
     */
    private final ReceiptDefinition source;

    /**
     * @see PatchStateTracker#isPersistent()
     */
    private boolean isPersistent = false;

    /**
     * @see PatchStateTracker#getIntermediateState()
     */
    private @Nullable ReceiptDefinition intermediate;

    /**
     * @see PatchStateTracker#getEntityState()
     */
    private @Nullable Receipt entity;

    /**
     * Constructs a {@code DefaultPatchStateTracker}.
     *
     * @param source the patch source.
     */
    public DefaultPatchStateTracker(ReceiptDefinition source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        return o instanceof PatchStateTracker tracker && tracker.toSource().equals(this.source)
                && Objects.equals(tracker.getIntermediateState(), this.intermediate)
                && Objects.equals(tracker.getEntityState(), this.entity);
    }

    @Override
    public int hashCode() {
        return this.source.hashCode();
    }

    @Override
    public ReceiptDefinition toSource() {
        return this.source;
    }

    @Override
    public void setPersistent() {
        this.isPersistent = true;
    }

    @Override
    public boolean isPersistent() {
        return this.isPersistent;
    }

    @Override
    public void setIntermediateState(ReceiptDefinition intermediate) {
        this.intermediate = intermediate;
    }

    @Override
    public @Nullable ReceiptDefinition getIntermediateState() {
        return this.intermediate;
    }

    @Override
    public void setEntityState(Receipt entity) {
        this.entity = entity;
    }

    @Override
    public @Nullable Receipt getEntityState() {
        return this.entity;
    }
}
