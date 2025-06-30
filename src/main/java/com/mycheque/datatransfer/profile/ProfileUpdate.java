package com.mycheque.datatransfer.profile;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;

import org.springframework.lang.Nullable;

import jakarta.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.mycheque.util.local.RegExp;

/**
 * A data transfer object accessing <i>an updated version</i> of a {@link Profile}.
 * <p>
 * Its structure is the same, except for the fact any field is allowed to be {@code null}.
 * However, the validation is still performed as for a {@code Profile} record.
 * <p>
 * <b>NOTE:</b> a {@code null}-valued field must be treated as <i>"do not update me"</i>.
 *
 * @author resxnvnce
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileUpdate {

    /**
     * A new username to be used for an update.
     */
    @JsonProperty("username")
    @Pattern(regexp = RegExp.USERNAME, message = "{@pattern#Profile.username}")
    private @Nullable String username;

    /**
     * A new password to be used for an update.
     */
    @JsonProperty("password")
    @Pattern(regexp = RegExp.PASSWORD, message = "{@pattern#Profile.password}")
    private @Nullable String password;

    /**
     * Constructs a {@link ProfileUpdate}.
     *
     * @param username the customer's new username.
     * @param password the customer's new password, probably encoded.
     */
    public ProfileUpdate(@Nullable String username, @Nullable String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Encode this profile update {@linkplain #getPassword() password}
     * using the supplied password-encoding function.
     *
     * @param passwordEncoder a {@link Function} to encode the password.
     */
    public void secureUsing(Function<String, String> passwordEncoder) {
        if (this.password != null) {
            this.password = passwordEncoder.apply(this.password);
        }
    }

    /**
     * Returns the {@code ProfileUpdate} username.
     *
     * @return the customer's new username.
     */
    @Nullable
    public String getUsername() {
        return username;
    }

    /**
     * Set the {@linkplain #getUsername() username} of this profile update.
     *
     * @param username a new username.
     */
    public void setUsername(@Nullable String username) {
        this.username = username;
    }

    /**
     * Returns the {@code ProfileUpdate} password.
     *
     * @return the customer's new password.
     */
    @Nullable
    public String getPassword() {
        return password;
    }

    /**
     * Set the {@linkplain #getPassword() password} of this profile update.
     *
     * @param password a new password.
     */
    public void setPassword(@Nullable String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        return obj instanceof ProfileUpdate update
                && Objects.equals(this.username, update.username) && Objects.equals(this.password, update.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.username, this.password);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass() + "[", "]")
                .add("username=" + this.username).add("password=" + this.password)
                .toString();
    }
}
