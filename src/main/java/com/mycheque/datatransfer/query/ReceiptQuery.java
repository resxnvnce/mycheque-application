package com.mycheque.datatransfer.query;

import java.time.LocalDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The query record for a single {@link com.mycheque.domain.Receipt Receipt}.
 *
 * @param foundationContains the value that a receipt foundation should contain, case-insensitive.
 * @param minTotal           the minimal value for a receipt total, inclusive.
 * @param maxTotal           the maximum value for a receipt total, inclusive.
 * @param minTimestamp       the minimal value for a receipt timestamp, inclusive.
 * @param maxTimestamp       the maximum value for a receipt timestamp, inclusive.
 * @param purchase           the query for a receipt purchase.
 * @author resxnvnce
 * @apiNote Dynamic query object fields are allowed to be {@code null}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ReceiptQuery(
        @JsonProperty("foundation_contains")
        String foundationContains,

        @Positive(message = "@positive#ReceiptQuery.(min/max)_total")
        @JsonProperty("min_total")
        Integer minTotal,

        @Positive(message = "@positive#ReceiptQuery.(min/max)_total")
        @JsonProperty("max_total")
        Integer maxTotal,

        @Past(message = "@past#ReceiptQuery.(min/max)_timestamp")
        @JsonProperty("min_timestamp")
        LocalDateTime minTimestamp,

        @Past(message = "@past#ReceiptQuery.(min/max)_timestamp")
        @JsonProperty("max_timestamp")
        LocalDateTime maxTimestamp,

        @Valid
        @JsonProperty("purchase")
        PurchaseQuery purchase) {
}
