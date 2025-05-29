package com.mycheque.client.serialize;

import java.time.LocalDateTime;

import org.springframework.lang.Nullable;

import com.mycheque.client.RequestBodyAttributes;
import com.mycheque.client.jsonstruct.FiscalIdentifier;

import static java.util.Objects.requireNonNull;

/**
 * The default {@link RequestBodyAttributes.Factory} implementation.
 *
 * @author resxnvnce
 */
public final class RequestBodyAttributesFactory implements RequestBodyAttributes.Factory {

    /**
     * A unique user authorization token.
     */
    private final String token;

    /**
     * Constructs an {@code RequestBodyAttributesFactory} for
     * the user with the given authorization token.
     *
     * @param token the authorization token. Must not be {@code null}.
     */
    public RequestBodyAttributesFactory(String token) {
        this.token = requireNonNull(token, "token must not be null");
    }

    @Override
    public RequestBodyAttributes create() {
        return new TokenPlaceholder(token);
    }

    @Override
    public QrRawAttributes byQrRaw(String qrraw) {
        return new QrRawRecord(token, qrraw);
    }

    @Override
    public QrUrlAttributes byQrUrl(String qrurl) {
        return new QrUrlRecord(token, qrurl);
    }

    /**
     * The default {@link TargetDetails.Builder} implementation.
     */
    static class TargetDetailsBuilder implements TargetDetails.Builder {

        private final String token;
        private final FiscalIdentifier id;

        private @Nullable Float total;
        private @Nullable LocalDateTime timestamp;

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
            return new TargetDetailsRecord(token, id, total, timestamp);
        }
    }

    @Override
    public TargetDetails.Builder byDetails(FiscalIdentifier id) {
        return new TargetDetailsBuilder(token, id);
    }
}
