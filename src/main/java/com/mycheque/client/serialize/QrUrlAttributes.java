package com.mycheque.client.serialize;

import com.mycheque.client.RequestBodyAttributes;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {@code RequestBodyAttributes} extension based on
 * the URL representation of the target entity's QR code.
 *
 * @author resxnvnce
 */
public interface QrUrlAttributes extends RequestBodyAttributes {

    /**
     * Returns the url representation of the target entity's QR.
     *
     * @return the QR code URL.
     */
    @JsonProperty("qrurl")
    String qrurl();
}
