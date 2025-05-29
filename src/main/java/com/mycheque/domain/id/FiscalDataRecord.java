package com.mycheque.domain.id;

import jakarta.persistence.Embeddable;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.annotation.Immutable;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import com.mycheque.client.jsonstruct.FiscalIdentifier;

/**
 * The default {@link FiscalIdentifier} implementation,
 * used both for serialization and deserialization.
 *
 * @param drive    the fiscal drive.
 * @param document the fiscal document.
 * @param sign     the fiscal sign.
 * @author resxnvnce
 */
@Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
public @Embeddable record FiscalDataRecord(

        @JsonProperty("drive")
        @JsonAlias("fn")
        @NotBlank(message = "{@not-blank#FiscalDataRecord.fn}")
        @Pattern(regexp = "^\\d{4,31}$", message = "{@pattern#FiscalDataRecord.fn}")
        @Field(name = "fn", targetType = FieldType.STRING)
        String drive,

        @JsonProperty("document")
        @JsonAlias("fd")
        @NotBlank(message = "{@not-blank#FiscalDataRecord.fd}")
        @Pattern(regexp = "^\\d{4,31}$", message = "{@pattern#FiscalDataRecord.fd}")
        @Field(name = "fd", targetType = FieldType.STRING)
        String document,

        @JsonProperty("sign")
        @JsonAlias("fp")
        @NotBlank(message = "{@not-blank#FiscalDataRecord.fp}")
        @Pattern(regexp = "^\\d{4,31}$", message = "{@pattern#FiscalDataRecord.fp}")
        @Field(name = "fp", targetType = FieldType.STRING)
        String sign

) implements FiscalIdentifier {
    /* @_@ */
}
