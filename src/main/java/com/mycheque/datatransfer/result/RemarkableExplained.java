package com.mycheque.datatransfer.result;

import com.mycheque.datatransfer.query.ReceiptDefinition;

/**
 * The record explaining a single {@code Remarkable} from a patchnotes outcome.
 *
 * @param subject     the {@link ReceiptDefinition} that couldn't be patched.
 * @param explanation a user-friendly message indicating what's wrong with the {@code subject}.
 * @author resxnvnce
 */
public record RemarkableExplained(ReceiptDefinition subject, String explanation) {
}
