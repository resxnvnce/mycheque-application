package com.mycheque.datatransfer.result;

import com.mycheque.lang.Nullable;

/**
 * The default {@link GenericResult} implementation.
 *
 * @param isFailure   has the action failed?
 * @param message     a localized message.
 * @param description an optional result description.
 * @author resxnvnce
 */
record GenericResultRecord(boolean isFailure, String message, @Nullable Object description) implements GenericResult {
}
