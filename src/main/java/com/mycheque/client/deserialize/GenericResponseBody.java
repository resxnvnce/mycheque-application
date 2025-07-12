package com.mycheque.client.deserialize;

import com.fasterxml.jackson.annotation.JsonCreator;

import com.mycheque.client.ResponseBodyAttributes;
import com.mycheque.client.jsonstruct.DataWrapper;
import com.mycheque.client.jsonstruct.ResponseStatusCode;

/**
 * The {@link ResponseBodyAttributes} implementation
 * guaranteed to contain a generic target entity instance.
 *
 * @param data the target entity itself.
 * @param code the response status code.
 * @author resxnvnce
 */
record GenericResponseBody<T>(T data, ResponseStatusCode code) implements ResponseBodyAttributes<T> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    GenericResponseBody(DataWrapper<T> data, ResponseStatusCode code) {
        this(data.unwrap(), code);
    }
}
