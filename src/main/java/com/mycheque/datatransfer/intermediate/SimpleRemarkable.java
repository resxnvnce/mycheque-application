package com.mycheque.datatransfer.intermediate;

import com.mycheque.datatransfer.accept.ReceiptDefinition;

/**
 * The default {@link Remarkable} implementation.
 *
 * @author resxnvnce
 */
public final class SimpleRemarkable extends AbstractRemarkable {

    /**
     * Constructs a {@code SimpleRemarkable}.
     *
     * @param subject    the remarkable subject.
     * @param remarkCode the remarkable code.
     */
    public SimpleRemarkable(ReceiptDefinition subject, PatchRemarkCode remarkCode) {
        super(subject, remarkCode);
    }
}
