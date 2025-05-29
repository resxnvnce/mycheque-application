package com.mycheque.client.deserialize;

import com.mycheque.client.ResponseBodyAttributes;

import com.mycheque.client.jsonstruct.ResponseStatusCode;

/**
 * The error {@link ResponseBodyAttributes} implementation.
 *
 * @param data the error message from a server.
 * @param code the response status code.
 * @author resxnvnce
 */
record ErrorResponseBody(String data, ResponseStatusCode code) implements ResponseBodyAttributes<String> {
}
