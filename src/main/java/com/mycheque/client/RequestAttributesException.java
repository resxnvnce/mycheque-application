package com.mycheque.client;

import com.mycheque.lang.Nullable;

/**
 * Exception thrown by the {@link ClientTemplate} if it's unable to perform
 * an HTTP request because the provided {@code RequestBodyAttributes}
 * are considered invalid for some reason.
 *
 * @author resxnvnce
 */
public class RequestAttributesException extends ClientTemplateException {

    @Nullable
    private final RequestBodyAttributes attrs;

    /**
     * Constructs a new {@code RequestAttributesException} with the specified
     * {@link RequestBodyAttributes} and detail message.
     *
     * @param attrs   the attributes of the request body.
     * @param message the detail message.
     */
    public RequestAttributesException(
            @Nullable RequestBodyAttributes attrs, String message) {

        super(message);
        this.attrs = attrs;
    }

    /**
     * Constructs a new {@code RequestAttributesException} with the specified
     * {@link RequestBodyAttributes}, detail message and root cause.
     *
     * @param attrs   the attributes of the request body.
     * @param message the detail message.
     * @param cause   the root cause.
     */
    public RequestAttributesException(
            @Nullable RequestBodyAttributes attrs, String message, @Nullable Throwable cause) {

        super(message, cause);
        this.attrs = attrs;
    }

    /**
     * Returns the {@code TargetDetails}
     * which caused this exception to be thrown.
     *
     * @return the unaccepted request body attributes.
     */
    @Nullable
    public RequestBodyAttributes getAttrs() {
        return this.attrs;
    }
}
