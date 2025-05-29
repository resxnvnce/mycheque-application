package com.mycheque.datatransfer.expose;

/**
 * The {@linkplain GenericResult#description() description}
 * to be returned after a registration or an update.
 *
 * @param username a customer's username, might be unchanged.
 * @param token    a customer's third party token, might be unchanged.
 * @author resxnvnce
 */
public record MutationDescription(String username, String token) {
}
