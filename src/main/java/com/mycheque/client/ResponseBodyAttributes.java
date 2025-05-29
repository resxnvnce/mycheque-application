package com.mycheque.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.mycheque.client.deserialize.Polymorphic;
import com.mycheque.client.jsonstruct.ResponseStatusCode;

/**
 * The server response body attributes container interface.
 * <p>
 * Currently, it is either a generic response body attributes container,
 * containing a target entity of the specified type, or a server error
 * container, as determined via {@link #haveError()} instance method.
 *
 * @param <T> the {@linkplain #data() content} type.
 * @author resxnvnce
 */
@Polymorphic
public interface ResponseBodyAttributes<T> {

    /**
     * Returns the server {@link ResponseStatusCode}.
     *
     * @return the server response description.
     */
    @JsonProperty("code")
    ResponseStatusCode code();

    /**
     * Indicates whether the server response body contains an error.
     * That is, no target entity {@linkplain #data() is inside}
     * if the request has failed.
     *
     * @return {@code false} if these attributes are not
     *         a part of the error response; {@code true} otherwise.
     */
    @JsonIgnore
    default boolean haveError() {
        return code().isError();
    }

    /**
     * Returns the content of the response, which is either a
     * server error message — just a regular {@link String} —
     * in case a request fails, or a target entity of the type
     * specified by the {@link ClientTemplate} to extract the response.
     *
     * @return a target entity or an error response message.
     */
    @JsonProperty("data")
    T data();
}
