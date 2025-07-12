package com.mycheque.client;

import com.mycheque.util.Assert;

import com.mycheque.lang.Nullable;
import com.mycheque.client.jsonstruct.StatusCode;
import com.mycheque.client.jsonstruct.ResponseStatusCode;

import static java.util.Objects.requireNonNull;

/**
 * Exception thrown in case the retrieved {@code ResponseBodyAttributes} contain an error.
 *
 * @author resxnvnce
 */
public class ResponseStatusCodeException extends ResponseAttributesException {

    /**
     * Constructs a new {@code ResponseStatusCodeException} with the specified
     * {@link ResponseBodyAttributes} and default message.
     *
     * @param attrs the attributes of the response body. Must not be {@code null}.
     */
    public ResponseStatusCodeException(ResponseBodyAttributes<String> attrs) {
        this(attrs, null);
    }

    /**
     * Constructs a new {@code ResponseStatusCodeException} with the specified
     * {@link ResponseBodyAttributes} and detail message.
     *
     * @param attrs   the attributes of the response body. Must not be {@code null}.
     * @param message the detail message.
     */
    public ResponseStatusCodeException(ResponseBodyAttributes<String> attrs, String message) {
        super(requireNonNull(attrs, "attrs must not be null"), message == null ? initMessage(attrs) : message);
    }

    /**
     * Returns the {@link ResponseStatusCode} of the error response.
     *
     * @return the error status code.
     */
    public ResponseStatusCode getStatusCode() {
        return getAttrs().code();
    }

    /**
     * Constructs the default message using the given error response attributes.
     *
     * @param attrs the {@code ResponseBodyAttributes} with an error.
     * @return the exception message.
     */
    private static String initMessage(ResponseBodyAttributes<String> attrs) {
        final ResponseStatusCode rsc = attrs.code();
        return rsc + (rsc instanceof StatusCode sc ? ": " + sc.reason() : "") + ". The server: " + attrs.data();
    }

    /**
     * Create a {@code ResponseStatusCodeException} or a status code specific subclass.
     *
     * @param attrs the attributes of the response body. Must not be {@code null}.
     * @return a {@code ResponseStatusCodeException} or its subclass instance.
     */
    public static ResponseStatusCodeException create(ResponseBodyAttributes<String> attrs) {
        return create(attrs, null);
    }

    /**
     * Create a {@code ResponseStatusCodeException} or a status code specific subclass.
     *
     * @param attrs   the attributes of the response body. Must not be {@code null}.
     * @param message an optional prepared message.
     * @return a {@code ResponseStatusCodeException} or its subclass instance.
     */
    public static ResponseStatusCodeException create(ResponseBodyAttributes<String> attrs, @Nullable String message) {
        Assert.notNull(attrs, () -> "attrs must not be null");
        Assert.state(attrs.code().isError(), () -> attrs.code() + " is not an error response code");

        if (attrs.code() instanceof StatusCode sc) {
            return switch (sc) {
                case TIMEOUT -> new Timeout(attrs, message == null ? initMessage(attrs) : message);
                case BAD_REQUEST -> new BadRequest(attrs, message == null ? initMessage(attrs) : message);
                case UNAUTHORIZED -> new Unauthorized(attrs, message == null ? initMessage(attrs) : message);
                case NOT_AN_ENTITY -> new NotAnEntity(attrs, message == null ? initMessage(attrs) : message);

                /* never happens. */
                default -> new ResponseStatusCodeException(attrs, message == null ? initMessage(attrs) : message);
            };
        }
        else {
            return new ResponseStatusCodeException(attrs, message == null ? initMessage(attrs) : message);
        }
    }

    /* Subclasses for specific StatusCode's */

    /**
     * {@link ResponseStatusCodeException} for StatusCode 3 Not An Entity.
     */
    public static final class NotAnEntity extends ResponseStatusCodeException {

        private NotAnEntity(ResponseBodyAttributes<String> attrs, String message) {
            super(attrs, message);
        }
    }

    /**
     * {@link ResponseStatusCodeException} for StatusCode 4 Timeout.
     */
    public static final class Timeout extends ResponseStatusCodeException {

        private Timeout(ResponseBodyAttributes<String> attrs, String message) {
            super(attrs, message);
        }
    }

    /**
     * {@link ResponseStatusCodeException} for StatusCode 5 Bad Request.
     */
    public static final class BadRequest extends ResponseStatusCodeException {

        private BadRequest(ResponseBodyAttributes<String> attrs, String message) {
            super(attrs, message);
        }
    }

    /**
     * {@link ResponseStatusCodeException} for StatusCode 401 Unauthorized.
     */
    public static final class Unauthorized extends ResponseStatusCodeException {

        private Unauthorized(ResponseBodyAttributes<String> attrs, String message) {
            super(attrs, message);
        }
    }
}
