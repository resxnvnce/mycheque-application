package com.mycheque.datatransfer.accept;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

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
@Polymorphic
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
    @JsonIgnore
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

        /**
         * Indicates that a method is based on a QR code representation.
         */
        private final boolean isQrBased;

        /**
         * Is this definition method based on a QR code?
         *
         * @return {@code true} if this definition method based on a QR code,
         *         {@code false} otherwise.
         */
        public boolean isQrBased() {
            return isQrBased;
        }

        /**
         * Constructs a new definition method.
         *
         * @param isQrBased indicates that this method {@link #isQrBased()}.
         */
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

            @JsonProperty("qr_raw")
            @NotBlank(message = "{@not-blank.ReceiptDefinition.ByQrRaw.qrraw}")
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

            @JsonProperty("qr_url")
            @NotBlank(message = "{@not-blank.ReceiptDefinition.ByQrUrl.qrurl}")
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

            @JsonProperty("id")
            @NotNull(message = "{@not-null#ReceiptDefinition.ByDetails.id}")
            FiscalDataRecord id,

            @JsonProperty("total")
            @NotNull(message = "{@not-null#ReceiptDefinition.ByDetails.total}")
            @Positive(message = "{@positive#ReceiptDefinition.ByDetails.total}")
            Float total,

            @JsonProperty("timestamp")
            @Past(message = "{@past#ReceiptDefinition.ByDetails.timestamp}")
            @NotNull(message = "{@not-null#ReceiptDefinition.ByDetails.timestamp}")
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
