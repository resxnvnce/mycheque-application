package com.mycheque.domain.id;

import jakarta.persistence.Embeddable;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.annotation.Immutable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import com.mycheque.domain.Receipt;

/**
 * The default {@code FiscalIdentifier} implementation,
 * used both for serialization and deserialization.
 *
 * @param fn the fiscal drive.
 * @param fd the fiscal document.
 * @param fp the fiscal sign.
 * @author resxnvnce
 */
@Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
public @Embeddable record FiscalDataRecord(

        @JsonProperty("fn")
        @NotBlank(message = "{@not-blank#FiscalDataRecord.fn}")
        @Pattern(regexp = "^\\d{4,31}$", message = "{@pattern#FiscalDataRecord.fn}")
        @Field(name = "fn", targetType = FieldType.STRING)
        String fn,

        @JsonProperty("fd")
        @NotBlank(message = "{@not-blank#FiscalDataRecord.fd}")
        @Pattern(regexp = "^\\d{4,31}$", message = "{@pattern#FiscalDataRecord.fd}")
        @Field(name = "fd", targetType = FieldType.STRING)
        String fd,

        @JsonProperty("fp")
        @NotBlank(message = "{@not-blank#FiscalDataRecord.fp}")
        @Pattern(regexp = "^\\d{4,31}$", message = "{@pattern#FiscalDataRecord.fp}")
        @Field(name = "fp", targetType = FieldType.STRING)
        String fp

) implements com.mycheque.client.jsonstruct.FiscalIdentifier {

    /**
     * Returns a matching {@link Specification} for the
     * partial search queries, using a fiscal identifier.
     *
     * @return a {@code Specification} that matches this identifier.
     */
    public Specification<Receipt> toReceiptSpecification() {
        Specification<Receipt> result = Specification.where(null);

        if (this.fn != null) {
            result.and((root, query, cb) -> cb.equal(root.get("fn"), this.fn));
        }

        if (this.fd != null) {
            result.and((root, query, cb) -> cb.equal(root.get("fd"), this.fd));
        }

        if (this.fp != null) {
            result.and((root, query, cb) -> cb.equal(root.get("fp"), this.fp));
        }

        return result;
    }
}
