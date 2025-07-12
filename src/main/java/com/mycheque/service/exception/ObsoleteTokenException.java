package com.mycheque.service.exception;

import com.mycheque.lang.Nullable;

import com.mycheque.service.ReceiptServiceException;

/**
 * Exception thrown if a {@code Customer}'s {@linkplain
 * com.mycheque.domain.Customer#getThirdpartyToken() token} has become obsolete or
 * has been changed without the application notice, which does not allow for further operations.
 *
 * @author resxnvnce
 */
public class ObsoleteTokenException extends ReceiptServiceException {

    /**
     * Constructs a new {@code ObsoleteTokenException} with the specified
     * detail message.
     *
     * @param message the detail message.
     */
    public ObsoleteTokenException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ObsoleteTokenException} with the specified
     * detail message and root cause.
     *
     * @param message the detail message.
     * @param cause   the root cause.
     */
    public ObsoleteTokenException(String message, @Nullable Throwable cause) {
        super(message, cause);
    }
}
