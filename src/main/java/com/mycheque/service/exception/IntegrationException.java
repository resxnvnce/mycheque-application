package com.mycheque.service.exception;

import com.mycheque.client.ClientTemplateException;

import com.mycheque.service.ReceiptServiceException;

/**
 * Indicates that an internal {@link ClientTemplateException} has been thrown
 * or an uncommon
 * {@link com.mycheque.client.jsonstruct.ResponseStatusCode ResponseStatusCode}
 * is encountered.
 *
 * @author resxnvnce
 */
public class IntegrationException extends ReceiptServiceException {

    /**
     * Constructs a new {@code IntegrationException} with the specified
     * root cause and the default detail message.
     *
     * @param cause the root cause, never {@code null}.
     */
    public IntegrationException(ClientTemplateException cause) {
        this("An internal ClientTemplateException has been thrown: '" + cause.getMessage() + "'", cause);
    }

    /**
     * Constructs a new {@code IntegrationException} with the specified
     * detail message and root cause.
     *
     * @param message the detail message.
     * @param cause   the root cause, never {@code null}.
     */
    public IntegrationException(String message, ClientTemplateException cause) {
        super(message, cause);
    }
}
