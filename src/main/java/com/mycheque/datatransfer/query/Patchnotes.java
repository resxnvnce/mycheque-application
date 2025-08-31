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
 * @param shouldDisplay1xxWarnings whether warning remarkables should be displayed or not.
 *                                 Defaults to {@code false}.
 * @param patches                  the {@link ReceiptDefinition}s to be transformed into
 *                                 receipt entities and then added to the persistence store.
 * @author resxnvnce
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Patchnotes(
        @JsonFormat(shape = JsonFormat.Shape.BOOLEAN)
        @JsonProperty("should_display_1xx_warnings")
        Boolean shouldDisplay1xxWarnings,

        @JsonProperty("patches")
        @NotEmpty(message = "{@not-empty#Patchnotes.patches}")
        List<@Valid ReceiptDefinition> patches) {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    static Patchnotes fromJsonProperties(Boolean shouldDisplay1xxWarnings, List<ReceiptDefinition> patches) {
        return new Patchnotes(shouldDisplay1xxWarnings != null && shouldDisplay1xxWarnings, patches);
    }
}
