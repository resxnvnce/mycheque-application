package com.mycheque.datatransfer;

import java.util.Set;
import java.time.LocalDateTime;

import com.mycheque.domain.id.FiscalDataRecord;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A data transfer object record acting as a target entity
 * for the {@code ClientTemplate} interface retrieval methods.
 *
 * @param id         an entity identifier.
 * @param total      the total cash sum, <strong>in kopecks</strong>.
 * @param foundation the retail outlets network.
 * @param timestamp  the purchase transaction date-time, no time-zone.
 * @param items      a set of the {@linkplain Item items} acquired.
 * @author resxnvnce
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ClientEntity(
        FiscalDataRecord id, Integer total, String foundation, LocalDateTime timestamp, Set<Item> items) {

    /**
     * A purchase position.
     *
     * @param name  the position name.
     * @param price the price per one unit.
     * @param total the position cost.
     */
    public record Item(
            @JsonProperty("name")
            String name,
            @JsonProperty("price")
            Integer price,
            @JsonProperty("sum")
            Integer total) {
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    static ClientEntity fromJsonProperties(
            @JsonProperty("fiscalDriveNumber")
            String fn,
            @JsonProperty("fiscalDocumentNumber")
            String fd,
            @JsonProperty("fiscalSign")
            String fp,

            @JsonProperty("totalSum")
            Integer total,

            @JsonProperty("user")
            String foundation,

            @JsonProperty("dateTime")
            LocalDateTime timestamp,

            @JsonProperty("items")
            Set<Item> items) {

        return new ClientEntity(new FiscalDataRecord(fn, fd, fp), total, foundation, timestamp, items);
    }
}
