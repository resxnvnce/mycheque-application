package com.mycheque.client.serialize;

/**
 * The default {@link QrRawAttributes} implementation.
 *
 * @param token the authorization token.
 * @param qrraw the raw QR code.
 * @author resxnvnce
 */
record QrRawRecord(String token, String qrraw) implements QrRawAttributes {
}
