package com.mycheque.datatransfer.profile;

import java.util.function.Function;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A data transfer object record accessing a {@link com.mycheque.domain.Customer Customer} credentials.
 *
 * @param profile the customer {@linkplain Profile profile}.
 * @param token   the customer's third-party token.
 * @author resxnvnce
 * @see Profile
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Credentials(

        @JsonProperty("profile")
        @NotNull(message = "{@not-null#Credentials.profile}")
        @Valid Profile profile,

        @JsonProperty("token")
        @NotBlank(message = "{@not-blank#Credentials.token}")
        String token) {

    /**
     * Encode the {@link Profile#getPassword() password}
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
