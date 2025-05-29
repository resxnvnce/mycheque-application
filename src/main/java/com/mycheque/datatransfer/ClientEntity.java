package com.mycheque.datatransfer;

import java.util.Set;
import java.time.LocalDateTime;

import com.mycheque.domain.id.FiscalDataRecord;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A data transfer object record acting as a target entity for the {@link
 * com.mycheque.client.ClientTemplate ClientTemplate} interface retrieval methods.
 *
 * @param id         the entity identifier.
 * @param items      a set of the {@linkplain Item items} acquired.
 * @param total      the total cash sum, <strong>in kopecks</strong>.
 * @param foundation the retail outlets network.
 * @param timestamp  the purchase transaction date-time, no time-zone.
 * @author resxnvnce
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ClientEntity(FiscalDataRecord id, Set<Item> items,
                           Integer total, String foundation, LocalDateTime timestamp) {

    /**
     * Part of a target entity, representing a single item from purchase.
     *
     * @param name  the item name, unchanged by any means.
     * @param price the item price per unit of measure.
     * @param total the item total cost, i.e. its price multiplied by the quantity.
     */
    public record Item(
            @JsonProperty("name")
            String name,

            @JsonProperty("price")
            Integer price,

            @JsonProperty("sum")
            Integer total) {

        /* Jackson should use the only available @JsonCreator: the canonical constructor. */
    }

    /**
     * The factory method used for deserialization.
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    static ClientEntity fromJsonProperties(
            @JsonProperty("fiscalDriveNumber")
            String fn,

            @JsonProperty("fiscalDocumentNumber")
            String fd,

            @JsonProperty("fiscalSign")
            String fp,

            @JsonProperty("items")
            Set<Item> items,

            @JsonProperty("totalSum")
            Integer total,

            @JsonProperty("user")
            String foundation,

            @JsonProperty("dateTime")
            LocalDateTime timestamp) {

        return new ClientEntity(new FiscalDataRecord(fn, fd, fp), items, total, foundation, timestamp);
    }
}
