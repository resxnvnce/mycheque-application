package com.mycheque.client.jsonstruct;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The target entity wrapper record. Retrieve the entity by calling {@link #unwrap()}.
 *
 * @param unwrap the deserialized target entity itself.
 * @param <T>    the target entity type.
 * @author resxnvnce
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record DataWrapper<T>(@JsonProperty("json") T unwrap) {
}
