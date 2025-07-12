package com.mycheque.client.serialize;

import java.time.LocalDateTime;

import com.mycheque.lang.Nullable;
import com.mycheque.client.RequestBodyAttributes;
import com.mycheque.client.jsonstruct.FiscalIdentifier;

import static java.util.Objects.requireNonNull;

/**
 * The default {@link RequestBodyAttributes.Factory} implementation.
 *
 * @author resxnvnce
 */
public final class AuthorizedRequestBodyAttributesFactory implements RequestBodyAttributes.Factory {

    private final String token;

    /**
     * Constructs an {@code AuthorizedRequestBodyAttributesFactory}
     * for the user with the given authorization token.
     *
     * @param token the authorization token. Must not be {@code null}.
     */
    public AuthorizedRequestBodyAttributesFactory(String token) {
        this.token = requireNonNull(token, "token must not be null");
    }

    static class TargetDetailsBuilder implements TargetDetails.Builder {

        private final String token;

        private final FiscalIdentifier id;

        @Nullable
        private Float total;

        @Nullable
        private LocalDateTime timestamp;

        TargetDetailsBuilder(String token, FiscalIdentifier id) {
            this.token = token;
            this.id = requireNonNull(id, "id must not be null");
        }

        @Override
        public TargetDetails.Builder total(Float total) {
            this.total = total;
            return this;
        }

        @Override
        public TargetDetails.Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @Override
        public TargetDetails build() {
            return new ByDetails(this.token, this.id, this.total, this.timestamp);
        }
    }

    @Override
    public RequestBodyAttributes create() {
        return new ByToken(this.token);
    }

    @Override
    public QrRawAttributes byQrRaw(String qrraw) {
        return new ByQrRaw(this.token, qrraw);
    }

    @Override
    public QrUrlAttributes byQrUrl(String qrurl) {
        return new ByQrUrl(this.token, qrurl);
    }

    @Override
    public TargetDetails.Builder byDetails(FiscalIdentifier id) {
        return new TargetDetailsBuilder(this.token, id);
    }

    record ByToken(String token) implements RequestBodyAttributes {
    }

    record ByQrRaw(String token, String qrraw) implements QrRawAttributes {
    }

    record ByQrUrl(String token, String qrurl) implements QrUrlAttributes {
    }

    record ByDetails(String token, FiscalIdentifier id, @Nullable Float total, @Nullable LocalDateTime timestamp)
            implements TargetDetails {
    }
}
