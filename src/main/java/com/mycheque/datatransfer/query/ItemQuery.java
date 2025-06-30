package com.mycheque.datatransfer.query;

import jakarta.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The query record for a single {@link com.mycheque.domain.Item Item}.
 *
 * @param nameContains the value that an item name should contain, case-insensitive.
 * @param minPrice     the minimal value for an item price, inclusive.
 * @param maxPrice     the maximum value for an item price, inclusive.
 * @param minCount     the minimal value for an item count, inclusive.
 * @param maxCount     the maximum value for an item count, inclusive.
 * @author resxnvnce
 * @apiNote Dynamic query object fields are allowed to be {@code null}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemQuery(
        @JsonProperty("name_contains")
        String nameContains,

        @Positive(message = "@positive#ItemQuery.(min/max)_price")
        @JsonProperty("min_price")
        Integer minPrice,

        @Positive(message = "@positive#ItemQuery.(min/max)_price")
        @JsonProperty("max_price")
        Integer maxPrice,

        @Positive(message = "@positive#ItemQuery.(min/max)_count")
        @JsonProperty("min_count")
        Integer minCount,

        @Positive(message = "@positive#ItemQuery.(min/max)_count")
        @JsonProperty("max_count")
        Integer maxCount) {

    /**
     * Defines the type of {@link ItemQuery}.
     */
    public enum Type {

        /**
         * There should be at least one item satisfying the query.
         */
        INCLUSIVE,

        /**
         * There should be not a single item satisfying the query.
         */
        EXCLUSIVE
    }
}
