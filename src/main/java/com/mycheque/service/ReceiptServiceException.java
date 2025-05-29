package com.mycheque.service;

import org.springframework.lang.Nullable;

/**
 * Base class for exceptions thrown by the {@link ReceiptService} in several cases.
 *
 * @author resxnvnce
 */
public abstract class ReceiptServiceException extends RuntimeException {

    /**
     * Constructs a new {@code ReceiptServiceException} with the specified
     * detail message.
     *
     * @param message the detail message.
     */
    protected ReceiptServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ReceiptServiceException} with the specified
     * detail message and root cause.
     *
     * @param message the detail message.
     * @param cause   the root cause.
     */
    protected ReceiptServiceException(String message, @Nullable Throwable cause) {
        super(message, cause);
    }
}
