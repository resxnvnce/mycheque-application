package com.mycheque.client.serialize;

import com.mycheque.client.RequestBodyAttributes;

/**
 * The default {@link RequestBodyAttributes} implementation,
 * simply wrapping an authorization token inside.
 *
 * @param token the authorization token.
 * @author resxnvnce
 */
record TokenPlaceholder(String token) implements RequestBodyAttributes {
}
