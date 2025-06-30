package com.mycheque.datatransfer.query;

import java.util.Set;

import jakarta.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.mycheque.util.Iterables;

/**
 * The query record for a single {@link com.mycheque.domain.Purchase Purchase}.
 *
 * @param include the queries representing the items that <i>MUST</i>
 *                be present in a purchase to be returned upon executing this query.
 * @param exclude the queries representing the items that, on the contrary, <i>MUST NOT</i>
 *                be present in a purchase to be returned upon executing this query.
 * @author resxnvnce
 * @apiNote Dynamic query object fields are allowed to be {@code null}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PurchaseQuery(
        @Valid
        @JsonProperty("include")
        Set<ItemQuery> include,

        @Valid
        @JsonProperty("exclude")
        Set<ItemQuery> exclude) {

    /**
     * Determines whether this query has at least one inclusive {@link ItemQuery}.
     *
     * @return {@code true} if the {@link #include()} set is not blank;
     *         {@code false} otherwise.
     */
    public boolean hasInclusiveQueries() {
        return !Iterables.isBlank(this.include);
    }

    /**
     * Determines whether this query has at least one exclusive {@link ItemQuery}.
     *
     * @return {@code true} if the {@link #exclude()} set is not blank;
     *         {@code false} otherwise.
     */
    public boolean hasExclusiveQueries() {
        return !Iterables.isBlank(this.exclude);
    }
}
