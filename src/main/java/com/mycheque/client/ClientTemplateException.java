package com.mycheque.client;

import org.springframework.lang.Nullable;

/**
 * Base class for exceptions thrown by the {@link ClientTemplate} in case a request fails.
 *
 * @author resxnvnce
 */
public abstract class ClientTemplateException extends RuntimeException {

    /**
     * Constructs a new {@code ClientTemplateException} with the specified
     * detail message.
     *
     * @param message the detail message.
     */
    protected ClientTemplateException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ClientTemplateException} with the specified
     * detail message and root cause.
     *
     * @param message the detail message.
     * @param cause   the root cause.
     */
    protected ClientTemplateException(String message, @Nullable Throwable cause) {
        super(message, cause);
    }
}
