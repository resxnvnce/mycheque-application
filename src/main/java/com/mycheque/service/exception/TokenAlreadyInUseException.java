package com.mycheque.service.exception;

import com.mycheque.lang.Nullable;

import com.mycheque.service.CustomerServiceException;

/**
 * Exception thrown if a {@code Customer}'s {@linkplain
 * com.mycheque.domain.Customer#getThirdpartyToken() token} provided is already
 * being used by another {@code Customer}, which does not allow for further operations.
 *
 * @author resxnvnce
 */
public class TokenAlreadyInUseException extends CustomerServiceException {

    private final String token;

    /**
     * Constructs a new {@code TokenAlreadyInUseException} with the specified
     * token and detail message.
     *
     * @param token   the token already in use.
     * @param message the detail message.
     */
    public TokenAlreadyInUseException(String token, String message) {
        super(message);
        this.token = token;
    }

    /**
     * Constructs a new {@code TokenAlreadyInUseException} with the specified
     * token, detail message and root cause.
     *
     * @param token   the token already in use.
     * @param message the detail message.
     * @param cause   the root cause.
     */
    public TokenAlreadyInUseException(String token, String message, @Nullable Throwable cause) {
        super(message, cause);
        this.token = token;
    }

    /**
     * Returns the token rejected due it is already being used by another {@code Customer}.
     *
     * @return a token already in use.
     */
    public String getToken() {
        return this.token;
    }
}
