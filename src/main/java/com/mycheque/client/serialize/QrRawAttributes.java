package com.mycheque.client.serialize;

import com.mycheque.client.RequestBodyAttributes;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {@code RequestBodyAttributes} extension based on
 * the raw representation of the target entity's QR code.
 *
 * @author resxnvnce
 */
public interface QrRawAttributes extends RequestBodyAttributes {

    /**
     * Returns the raw representation of the target entity's QR.
     *
     * @return the raw QR code.
     */
    @JsonProperty("qrraw")
    String qrraw();
}
