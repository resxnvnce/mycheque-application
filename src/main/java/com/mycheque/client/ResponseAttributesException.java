package com.mycheque.client;

import org.springframework.lang.Nullable;

/**
 * Exception thrown by the {@link ClientTemplate} if
 * {@code ResponseBodyAttributes} cannot be retrieved or the
 * received ones {@linkplain ResponseBodyAttributes#haveError() have error}.
 *
 * @author resxnvnce
 */
public class ResponseAttributesException extends ClientTemplateException {

    /**
     * Error response body attributes.
     */
    private final @Nullable ResponseBodyAttributes<String> attrs;

    /**
     * Constructs a new {@code ResponseAttributesException} with the specified
     * detail message.
     *
     * @param message the detail message.
     */
    public ResponseAttributesException(String message) {
        this(null, message);
    }

    /**
     * Constructs a new {@code ResponseAttributesException} with the specified
     * {@link ResponseBodyAttributes} and detail message.
     *
     * @param attrs   the attributes of the response body (optional).
     * @param message the detail message.
     */
    public ResponseAttributesException(@Nullable ResponseBodyAttributes<String> attrs,
                                       String message) {
        super(message);
        this.attrs = attrs;
    }

    /**
     * Constructs a new {@code ResponseAttributesException} with the specified
     * detail message and root cause.
     *
     * @param message the detail message.
     * @param cause   the root cause.
     */
    public ResponseAttributesException(String message, @Nullable Throwable cause) {
        this(null, message, cause);
    }

    /**
     * Constructs a new {@code ResponseAttributesException} with the specified
     * {@link ResponseBodyAttributes}, detail message and root cause.
     *
     * @param attrs   the attributes of the response body (optional).
     * @param message the detail message.
     * @param cause   the root cause.
     */
    public ResponseAttributesException(@Nullable ResponseBodyAttributes<String> attrs,
                                       String message, @Nullable Throwable cause) {
        super(message, cause);
        this.attrs = attrs;
    }

    /**
     * Returns the error {@code ResponseBodyAttributes} or {@code null}
     * in case the response body hasn't been extracted properly.
     *
     * @return the unacceptable response body attributes.
     */
    @Nullable
    public ResponseBodyAttributes<String> getAttrs() {
        return attrs;
    }
}
