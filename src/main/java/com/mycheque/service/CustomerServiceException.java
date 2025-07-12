package com.mycheque.service;

import com.mycheque.lang.Nullable;

/**
 * Base class for exceptions thrown by the {@link CustomerService} in several cases.
 *
 * @author resxnvnce
 */
public abstract class CustomerServiceException extends RuntimeException {

    /**
     * Constructs a new {@code CustomerServiceException} with the specified
     * detail message.
     *
     * @param message the detail message.
     */
    protected CustomerServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code CustomerServiceException} with the specified
     * detail message and root cause.
     *
     * @param message the detail message.
     * @param cause   the root cause.
     */
    protected CustomerServiceException(String message, @Nullable Throwable cause) {
        super(message, cause);
    }
}
