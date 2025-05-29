package com.mycheque.datatransfer.intermediate;

import com.mycheque.datatransfer.accept.ReceiptDefinition;

/**
 * {@code Remarkable} is the interface for placeholders containing the
 * {@link ReceiptDefinition} that might be processed incorrectly with the
 * corresponding {@link PatchRemarkCode} explaining the problems encountered
 * while patching the given receipt.
 * <p>
 * For now, instances are created by the {@linkplain #of factory method}.
 *
 * @author resxnvnce
 */
public interface Remarkable {

    /**
     * Returns the <i>subject</i> of this {@code Remarkable}, i.e.
     * the {@link ReceiptDefinition} that might be patched incorrectly.
     *
     * @return the {@code Remarkable} subject.
     */
    ReceiptDefinition getSubject();

    /**
     * Returns the {@link PatchRemarkCode} corresponding
     * to the {@linkplain #getSubject() subject}.
     *
     * @return the patch remark code.
     */
    PatchRemarkCode getRemarkCode();

    /**
     * Creates an instance of {@code Remarkable}.
     *
     * @param subject    the {@code Remarkable} {@linkplain #getSubject() subject}.
     * @param remarkCode the patch remark {@linkplain #getRemarkCode() code}.
     * @return the default {@code Remarkable} implementation instance.
     */
    static Remarkable of(ReceiptDefinition subject, PatchRemarkCode remarkCode) {
        return new SimpleRemarkable(subject, remarkCode);
    }
}
