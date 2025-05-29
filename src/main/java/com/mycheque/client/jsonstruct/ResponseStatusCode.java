package com.mycheque.client.jsonstruct;

import com.mycheque.util.Assert;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents a response status code being sent by the third party API provider.
 * Implemented by {@link StatusCode}, but defined as an interface
 * to allow for values not in that enumeration.
 *
 * @author resxnvnce
 */
public sealed interface ResponseStatusCode permits DefaultResponseStatusCode, StatusCode {

    /**
     * Returns the integer value of this response code.
     *
     * @return the corresponding integer value.
     */
    int value();

    /**
     * Indicates whether this {@code ResponseStatusCode} represents an error or not.
     *
     * @return {@code true} if this {@code ResponseStatusCode} is an error code;
     *         {@code false} otherwise.
     */
    boolean isError();

    /**
     * Indicates whether this {@code ResponseStatusCode} shares
     * the same {@linkplain #value() value} as the other response code.
     *
     * @param other the other {@code ResponseStatusCode} to compare.
     * @return {@code true} if the two response codes share the same integer value,
     *         {@code false} otherwise.
     */
    default boolean isSameCodeAs(ResponseStatusCode other) {
        return value() == other.value();
    }

    /**
     * Returns a {@code ResponseStatusCode} for the given integer value.
     *
     * @param code the response code as an integer.
     * @return the corresponding {@code ResponseStatusCode}.
     * @throws IllegalArgumentException if {@code code} is invalid.
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    static ResponseStatusCode valueOf(int code) {
        Assert.args(code >= 1 && code <= 999, () ->
                "Response status code should be an integer in range [1, 999]. Given: '" + code + "'.");
        final var status = StatusCode.resolve(code);
        return status != null ? status : new DefaultResponseStatusCode(code);
    }
}
