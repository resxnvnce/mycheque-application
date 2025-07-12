package com.mycheque.datatransfer.result;

import com.mycheque.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the result of an action, modifying the state
 * of a {@link com.mycheque.domain.Customer Customer}.
 *
 * @author resxnvnce
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public interface GenericResult {

    /**
     * Determines whether the action represented by this {@code GenericResult} has failed.
     *
     * @return {@code true} if this result indicates a failure, {@code false} otherwise.
     */
    @JsonIgnore
    boolean isFailure();

    /**
     * Returns the localized message, shortly describing this {@code GenericResult}.
     *
     * @return a brief localized message.
     */
    @JsonProperty("message")
    String message();

    /**
     * <b>[Optional]</b> Returns the metadata
     * required for troubleshooting a failure or
     * some additional information about a successfully applied action.
     *
     * @return the metadata of this {@code GenericResult}.
     */
    @Nullable
    @JsonProperty("description")
    Object description();

    /**
     * Constructs a {@link GenericResult}, indicating the action has failed.
     *
     * @param message     a localized message.
     * @param description an optional result description.
     * @return a {@code GenericResult}, describing a failure.
     */
    static GenericResult failed(String message, @Nullable Object description) {
        return new GenericResultRecord(true, message, description);
    }

    /**
     * Constructs a {@link GenericResult}, indicating the action has failed.
     *
     * @param message a localized message.
     * @return a {@code GenericResult}, describing a failure.
     */
    static GenericResult failed(String message) {
        return failed(message, null);
    }

    /**
     * Constructs a {@link GenericResult}, indicating the action has succeeded.
     *
     * @param message     a localized message.
     * @param description an optional result description.
     * @return a {@code GenericResult}, describing a successfully applied action.
     */
    static GenericResult succeeded(String message, @Nullable Object description) {
        return new GenericResultRecord(false, message, description);
    }

    /**
     * Constructs a {@link GenericResult}, indicating the action has succeeded.
     *
     * @param message a localized message.
     * @return a {@code GenericResult}, describing a successfully applied action.
     */
    static GenericResult succeeded(String message) {
        return succeeded(message, null);
    }
}
