package com.mycheque.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.mycheque.client.jsonstruct.FiscalIdentifier;

import com.mycheque.client.serialize.TargetDetails;
import com.mycheque.client.serialize.QrRawAttributes;
import com.mycheque.client.serialize.QrUrlAttributes;
import com.mycheque.client.serialize.RequestBodyAttributesFactory;

/**
 * Base container interface for entities being sent within a request body.
 * <p>
 * Obtain instances by using the static {@link #factory(String)} method.
 *
 * @author resxnvnce
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public interface RequestBodyAttributes {

    /**
     * Returns the authorization token each authenticated user has.
     *
     * @return the authorization token.
     */
    @JsonProperty("token")
    String token();

    /**
     * {@link RequestBodyAttributes} factory interface.
     */
    interface Factory {

        /**
         * Create a simple {@code RequestBodyAttributes} instance,
         * wrapping the given authorization token.
         *
         * @return the authorization token wrapper.
         */
        RequestBodyAttributes create();

        /**
         * Create a {@link QrRawAttributes} instance using
         * the raw representation of the target entity's QR code.
         *
         * @param qrraw the raw QR code.
         * @return the {@code QrRawAttributes}.
         */
        QrRawAttributes byQrRaw(String qrraw);

        /**
         * Create a {@link QrUrlAttributes} instance using
         * the URL representation of the target entity's QR code.
         *
         * @param qrurl the QR code URL.
         * @return the {@code QrUrlAttributes}.
         */
        QrUrlAttributes byQrUrl(String qrurl);

        /**
         * Start building the {@link TargetDetails} using
         * the given {@link FiscalIdentifier}.
         *
         * @param id a target entity {@code FiscalIdentifier}.
         *           Must not be {@code null}.
         * @return the {@link TargetDetails.Builder}.
         */
        TargetDetails.Builder byDetails(FiscalIdentifier id);
    }

    /**
     * Start creating a request body using the given authorization token.
     *
     * @param token the authorization token. Must not be {@code null}.
     * @return the {@link Factory} to create
     *         a {@code RequestBodyAttributes} instance.
     */
    static Factory factory(String token) {
        return new RequestBodyAttributesFactory(token);
    }
}
