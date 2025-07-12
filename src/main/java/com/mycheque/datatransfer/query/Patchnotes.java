package com.mycheque.datatransfer.query;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A data transfer object record containing
 * the {@linkplain #patches() patches} to apply
 * and some external configuration.
 *
 * @param enable1xxWarnings whether warning remarkables should be displayed or not.
 *                          Defaults to {@code false}.
 * @param patches           the {@link ReceiptDefinition}s to be transformed into
 *                          receipt entities and then added to the persistence store.
 * @author resxnvnce
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Patchnotes(boolean enable1xxWarnings, List<ReceiptDefinition> patches) {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    static Patchnotes fromJsonProperties(

            @JsonFormat(shape = JsonFormat.Shape.BOOLEAN)
            @JsonProperty("enable_warnings")
            Boolean warningsEnabled,

            @JsonProperty("patches")
            @NotEmpty(message = "{@not-empty#Patchnotes.patches}")
            List<@Valid ReceiptDefinition> patches) {

        return new Patchnotes(warningsEnabled != null && warningsEnabled, patches);
    }
}
