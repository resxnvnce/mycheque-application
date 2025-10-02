package com.mycheque.datatransfer.query;

import java.time.LocalDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.mycheque.domain.id.FiscalDataRecord;

/**
 * A data transfer object record acting as a definition
 * for a new {@link com.mycheque.domain.Receipt Receipt}
 * or for an existing one.
 *
 * @author resxnvnce
 */
@ReceiptDefinitionTypeInfo // jackson
public interface ReceiptDefinition {

    /**
     * Is this {@code ReceiptDefinition} embedded in a QR code?
     *
     * @return {@code true} if this definition is represented
     *         by a QR code, {@code false} otherwise.
     */
    @JsonIgnore
    default boolean isQrBased() {
        return by().isQrBased();
    }

    /**
     * Returns an enum constant, describing the definition method.
     *
     * @return this receipt definition method.
     */
    @JsonProperty("definition")
    By by();

    /**
     * Enum describing the definition method.
     */
    enum By {

        /**
         * Indicates that this method operates on
         * the raw representation of a QR code.
         */
        QR_RAW(true),

        /**
         * Indicates that this method operates on
         * the URL representation of a QR code.
         */
        QR_URL(true),

        /**
         * Indicates that this method operates on
         * the manually specified details of a receipt.
         */
        DETAILS(false);

        private final boolean isQrBased;

        /**
         * Is this definition method based on a QR code?
         *
         * @return {@code true} if this definition method based on a QR code,
         *         {@code false} otherwise.
         */
        public boolean isQrBased() {
            return this.isQrBased;
        }

        /**
         * Returns the JSON value this definition method mapped to.
         *
         * @return a {@code String} value that matches this definition method.
         */
        @JsonValue
        public String toJsonProperty() {
            return this.name().toLowerCase();
        }

        By(boolean isQrBased) {
            this.isQrBased = isQrBased;
        }
    }

    /**
     * {@link ReceiptDefinition} extension based on
     * the raw representation of a receipt's QR code.
     *
     * @param qrraw the raw QR code.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    record ByQrRaw(

            @NotBlank(message = "{@not-blank#ReceiptDefinition.ByQrRaw.qrraw}")
            @JsonProperty("qr_raw")
            String qrraw

    ) implements ReceiptDefinition {

        @Override
        public By by() {
            return By.QR_RAW;
        }
    }

    /**
     * {@link ReceiptDefinition} extension based on
     * the URL representation of a receipt's QR code.
     *
     * @param qrurl the QR code URL.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    record ByQrUrl(

            @NotBlank(message = "{@not-blank#ReceiptDefinition.ByQrUrl.qrurl}")
            @JsonProperty("qr_url")
            String qrurl

    ) implements ReceiptDefinition {

        @Override
        public By by() {
            return By.QR_URL;
        }
    }

    /**
     * {@link ReceiptDefinition} extension based on
     * the manually specified details of a receipt.
     *
     * @param id        the receipt identifier record.
     * @param total     the total cash sum, in rubles.
     * @param timestamp the transaction date-time, with no time-zone.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    record ByDetails(

            @Valid
            @NotNull(message = "{@not-null#ReceiptDefinition.ByDetails.id}")
            @JsonProperty("id")
            FiscalDataRecord id,

            @NotNull(message = "{@not-null#ReceiptDefinition.ByDetails.total}")
            @Positive(message = "{@positive#ReceiptDefinition.ByDetails.total}")
            @JsonProperty("total")
            Float total,

            @Past(message = "{@past#ReceiptDefinition.ByDetails.timestamp}")
            @NotNull(message = "{@not-null#ReceiptDefinition.ByDetails.timestamp}")
            @JsonProperty("timestamp")
            LocalDateTime timestamp

    ) implements ReceiptDefinition {

        @Override
        public By by() {
            return By.DETAILS;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            return o instanceof ByDetails other && this.id.equals(other.id);
        }

        @Override
        public int hashCode() {
            return this.id.hashCode();
        }
    }
}
