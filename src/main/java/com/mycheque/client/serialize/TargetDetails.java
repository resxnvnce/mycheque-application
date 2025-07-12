package com.mycheque.client.serialize;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.mycheque.lang.Nullable;
import com.mycheque.client.RequestBodyAttributes;
import com.mycheque.client.jsonstruct.FiscalIdentifier;

/**
 * {@code RequestBodyAttributes} extension based on
 * manually specified details of a target entity.
 *
 * @author resxnvnce
 */
public interface TargetDetails extends RequestBodyAttributes {

    /**
     * Returns the {@link FiscalIdentifier} of a target entity.
     *
     * @return the fiscal data wrapper instance.
     */
    @JsonIgnore
    FiscalIdentifier id();

    /**
     * Returns the total cash sum, in rubles.
     *
     * @return the total cash sum.
     */
    @Nullable
    @JsonProperty("s")
    Float total();

    /**
     * Returns the date-time of the transaction.
     *
     * @return the date-time.
     */
    @Nullable
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonProperty("t")
    LocalDateTime timestamp();

    /**
     * A builder of {@link TargetDetails}.
     * <p>
     * Each of the setter methods modifies the state
     * of the builder and returns the same instance.
     */
    interface Builder {

        /**
         * Sets the total cash sum, in rubles.
         *
         * @param total the total cash sum.
         * @return this builder.
         */
        Builder total(Float total);

        /**
         * Sets the date-time of the transaction.
         *
         * @param timestamp the date-time.
         * @return this builder.
         */
        Builder timestamp(LocalDateTime timestamp);

        /**
         * Returns a new {@link TargetDetails} built from the current state of this builder.
         *
         * @return a new immutable {@code TargetDetails} instance.
         */
        TargetDetails build();
    }

    /* FiscalIdentifier data unwrapping methods */

    @JsonProperty("fn")
    private String fn() {
        return id().fn();
    }

    @JsonProperty("fd")
    private String fd() {
        return id().fd();
    }

    @JsonProperty("fp")
    private String fp() {
        return id().fp();
    }
}
