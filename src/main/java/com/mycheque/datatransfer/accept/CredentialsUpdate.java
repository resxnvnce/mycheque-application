package com.mycheque.datatransfer.accept;

import java.util.function.Function;

import jakarta.validation.Valid;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A data transfer object record accessing <i>an updated version</i> of a {@link Credentials}.
 * <p>
 * <b>NOTE:</b> a {@code null}-valued field must be treated as <i>"do not update me"</i>.
 *
 * @param profile the customer's {@linkplain ProfileUpdate profile update}.
 * @param token   the customer's new third party token.
 * @author resxnvnce
 * @see ProfileUpdate
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CredentialsUpdate(

        @JsonProperty("profile")
        @Nullable
        @Valid ProfileUpdate profile,

        @JsonProperty("token")
        @Nullable
        String token) {

    /**
     * Encode the {@link ProfileUpdate#getPassword() password}
     * using the supplied password-encoding function.
     *
     * @param passwordEncoder a {@link Function} to use for encoding.
     */
    public void securePasswordUsing(Function<String, String> passwordEncoder) {
        if (this.profile != null) {
            this.profile.secureUsing(passwordEncoder);
        }
    }
}
