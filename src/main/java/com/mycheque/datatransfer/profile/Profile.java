package com.mycheque.datatransfer.profile;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.mycheque.util.local.RegExp;

/**
 * A data transfer object accessing a {@link com.mycheque.domain.Customer Customer} <i>profile</i>,
 * i.e. the pair of a {@linkplain #getUsername() username} and a {@linkplain #getPassword() password}.
 *
 * @author resxnvnce
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Profile {

    /**
     * A username, which is unique among the {@code Customer}s.
     */
    @JsonProperty("username")
    @NotNull(message = "{@not-null#Profile.username}")
    @Pattern(regexp = RegExp.USERNAME, message = "{@pattern#Profile.username}")
    private String username;

    /**
     * A {@code Customer} password, probably in an encoded version.
     */
    @JsonProperty("password")
    @NotNull(message = "{@not-null#Profile.password}")
    @Pattern(regexp = RegExp.PASSWORD, message = "{@pattern#Profile.password}")
    private String password;

    /**
     * Constructs a new customer {@link Profile}. None of the arguments may be {@code null}.
     *
     * @param username the customer username.
     * @param password the customer password, probably encoded.
     */
    public Profile(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Encode this profile {@linkplain #getPassword() password}
     * using the supplied password-encoding function.
     *
     * @param passwordEncoder a {@link Function} to encode the password.
     */
    public void secureUsing(Function<String, String> passwordEncoder) {
        this.password = passwordEncoder.apply(this.password);
    }

    /**
     * Returns the {@code Profile} username, which is unique among the {@code Customer}s.
     *
     * @return the customer username.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Set the {@linkplain #getUsername() username} of this profile.
     *
     * @param username a new username. Must not be {@code null}.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the {@code Profile} password, probably encoded.
     *
     * @return the customer password.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Set the {@linkplain #getPassword() password} of this profile.
     *
     * @param password a new password. Must not be {@code null}.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        return obj instanceof Profile profile &&
                this.username.equals(profile.username) && this.password.equals(profile.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.username, this.password);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass() + "[", "]")
                .add("username=" + username)
                .add("password=" + password)
                .toString();
    }
}
