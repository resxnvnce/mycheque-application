package com.mycheque.client.serialize;

/**
 * The default {@link QrUrlAttributes} implementation.
 *
 * @param token the authorization token.
 * @param qrurl the QR code URL.
 * @author resxnvnce
 */
record QrUrlRecord(String token, String qrurl) implements QrUrlAttributes {
}
