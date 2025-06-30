package com.mycheque.controller.validation;

import java.util.function.Consumer;

import org.springframework.validation.Errors;
import org.springframework.stereotype.Component;

import com.mycheque.validation.CustomerTokenValidator;
import com.mycheque.validation.UsernameNotTakenValidator;

import com.mycheque.datatransfer.profile.ProfileUpdate;
import com.mycheque.datatransfer.profile.Credentials;
import com.mycheque.datatransfer.profile.CredentialsUpdate;

import com.mycheque.service.wrapper.AuthorizedWrapper;

import static com.mycheque.util.Lambdas.applyOrNull;

/**
 * The default {@link CredentialsValidator} implementation.
 *
 * @author resxnvnce
 */
@Component
public class DefaultCredentialsValidator implements CredentialsValidator {

    /**
     * Rejects non-existing tokens.
     */
    private final CustomerTokenValidator customerTokenValidator;

    /**
     * Rejects already existing usernames.
     */
    private final UsernameNotTakenValidator usernameNotTakenValidator;

    /**
     * Constructs a {@code DefaultCredentialsValidator}.
     *
     * @param customerTokenValidator    a validator rejecting non-existing third party API tokens.
     * @param usernameNotTakenValidator a validator rejecting already existing customer usernames.
     */
    public DefaultCredentialsValidator(CustomerTokenValidator customerTokenValidator,
                                       UsernameNotTakenValidator usernameNotTakenValidator) {

        this.customerTokenValidator = customerTokenValidator;
        this.usernameNotTakenValidator = usernameNotTakenValidator;
    }

    @Override
    public boolean validate(Credentials inspected, Errors errors) {
        this.customerTokenValidator
                .validate(inspected.token(), errors);
        this.usernameNotTakenValidator
                .validate(inspected.profile().getUsername(), errors);

        return !errors.hasErrors();
    }

    @Override
    public boolean validate(AuthorizedWrapper<CredentialsUpdate> inspected, Errors errors) {
        final var creds = inspected.object();
        final var owner = inspected.customer();

        acceptIfUpdatable(
                owner.getThirdpartyToken(),
                creds.token(),
                token -> this.customerTokenValidator.validate(token, errors)
        );

        acceptIfUpdatable(
                owner.getUsername(),
                applyOrNull(creds.profile(), ProfileUpdate::getUsername),
                username -> this.usernameNotTakenValidator.validate(username, errors)
        );

        return !errors.hasErrors();
    }

    /**
     * Performs the {@code action} over a new value in case it's not {@code null}
     * and isn't {@linkplain Object#equals equal} to the corresponding old value.
     *
     * @param oldValue the old value.
     * @param newValue the new value.
     * @param action   the action to be performed over {@code newValue}.
     */
    private <T> void acceptIfUpdatable(Object oldValue, T newValue, Consumer<? super T> action) {
        boolean isUpdatable = newValue != null && !newValue.equals(oldValue);

        if (isUpdatable) {
            action.accept(newValue);
        }
    }
}
