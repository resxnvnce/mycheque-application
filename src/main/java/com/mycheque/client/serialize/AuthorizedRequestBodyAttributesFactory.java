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
public final class AuthorizedRequestBodyAttributesFactory implements RequestBodyAttributes.Factory {

    /**
     * A unique user authorization token.
     */
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

    /**
     * The default {@link TargetDetails.Builder} implementation.
     */
    static class TargetDetailsBuilder implements TargetDetails.Builder {

        /**
         * @see TargetDetails#token()
         */
        private final String token;

        /**
         * @see TargetDetails#id()
         */
        private final FiscalIdentifier id;

        /**
         * @see TargetDetails#total()
         */
        @Nullable
        private Float total;

        /**
         * @see TargetDetails#timestamp()
         */
        @Nullable
        private LocalDateTime timestamp;

        /**
         * Constructs a {@code TargetDetailsBuilder} for
         * the user with the given authorization token.
         *
         * @param token the authorization token.
         *              Must not be {@code null}.
         * @param id    the target entity identifier.
         *              Must not be {@code null}.
         */
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

    /**
     * The default {@link RequestBodyAttributes}.
     */
    record ByToken(String token) implements RequestBodyAttributes {
    }

    /**
     * The default {@link QrRawAttributes}.
     */
    record ByQrRaw(String token, String qrraw) implements QrRawAttributes {
    }

    /**
     * The default {@link QrUrlAttributes}.
     */
    record ByQrUrl(String token, String qrurl) implements QrUrlAttributes {
    }

    /**
     * The default {@link TargetDetails}.
     */
    record ByDetails(String token, FiscalIdentifier id, @Nullable Float total, @Nullable LocalDateTime timestamp)
            implements TargetDetails {
    }
}
