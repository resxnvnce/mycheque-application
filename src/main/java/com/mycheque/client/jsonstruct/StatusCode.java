package com.mycheque.client.jsonstruct;

import java.util.Map;

import com.mycheque.lang.Nullable;

import static com.mycheque.util.Maps.mapToIdentity;

/**
 * Enum describing the status of an HTTP response,
 * sent by the external API provider.
 *
 * @author resxnvnce
 */
public enum StatusCode implements ResponseStatusCode {

    /**
     * Indicates a successful request and, therefore,
     * the response received contains the expected content.
     */
    OK(1, "OK"),

    /**
     * Indicates that a request body contains
     * no data to identify a target entity.
     */
    NOT_AN_ENTITY(3, "Not An Entity"),

    /**
     * Indicates a timeout on the content that has been searched for.
     */
    TIMEOUT(4, "Content Timeout"),

    /**
     * Indicates that the request body sent contains
     * insufficient data or the data provided has an illegal format.
     */
    BAD_REQUEST(5, "Bad Request"),

    /**
     * Indicates a failed authorization attempt.
     */
    UNAUTHORIZED(401, "Invalid Authorization Token");

    private static final Map<Integer, StatusCode> RESOLVER = mapToIdentity(StatusCode.class, StatusCode::value);

    private final int value;

    private final String reason;

    @Override
    public boolean isError() {
        return this != OK;
    }

    @Override
    public int value() {
        return this.value;
    }

    /**
     * Returns the reason this {@code StatusCode} appears.
     * May be used to construct a proper exception.
     *
     * @return the short phrase describing
     *         the reason this {@code StatusCode} appears.
     */
    public String reason() {
        return this.reason;
    }

    /**
     * Resolve the given status code to a {@code StatusCode}, if possible.
     *
     * @param code the response status code (potentially non-standard).
     * @return the corresponding {@code StatusCode}, or {@code null} if not found.
     */
    @Nullable
    public static StatusCode resolve(int code) {
        return RESOLVER.get(code);
    }

    StatusCode(int value, String reason) {
        this.value = value;
        this.reason = reason;
    }
}
