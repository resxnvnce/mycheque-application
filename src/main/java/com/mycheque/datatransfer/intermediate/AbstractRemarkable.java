package com.mycheque.datatransfer.intermediate;

import java.util.Objects;
import java.util.StringJoiner;

import com.mycheque.datatransfer.query.ReceiptDefinition;

/**
 * Abstract base class for {@link Remarkable}s.
 *
 * @author resxnvnce
 */
public abstract class AbstractRemarkable implements Remarkable {

    private final ReceiptDefinition subject;

    private final PatchRemarkCode remarkCode;

    /**
     * The constructor for subclasses to call.
     * <p>
     * Ensures both {@code subject} and {@code remarkCode} are not {@code null}.
     *
     * @param subject    the remarkable subject.
     * @param remarkCode the remarkable code.
     */
    protected AbstractRemarkable(ReceiptDefinition subject, PatchRemarkCode remarkCode) {
        this.subject = Objects.requireNonNull(subject, "the remarkable subject must not be null");
        this.remarkCode = Objects.requireNonNull(remarkCode, "the remarkable code must not be null");
    }

    @Override
    public ReceiptDefinition getSubject() {
        return this.subject;
    }

    @Override
    public PatchRemarkCode getRemarkCode() {
        return this.remarkCode;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;

        return o instanceof AbstractRemarkable that
                && Objects.equals(this.subject, that.subject)
                && Objects.equals(this.remarkCode, that.remarkCode);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.subject, this.remarkCode);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass() + "[", "]")
                .add("subject=" + subject)
                .add("remarkCode=" + remarkCode)
                .toString();
    }
}
