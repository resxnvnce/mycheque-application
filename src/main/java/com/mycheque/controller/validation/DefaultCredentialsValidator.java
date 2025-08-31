package com.mycheque.controller.validation;

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

    private final CustomerTokenValidator customerTokenValidator;

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
                owner.getThirdpartyToken(), creds.token(),
                token -> this.customerTokenValidator.validate(token, errors)
        );

        acceptIfUpdatable(
                owner.getUsername(), applyOrNull(creds.profile(), ProfileUpdate::getUsername),
                username -> this.usernameNotTakenValidator.validate(username, errors)
        );

        return !errors.hasErrors();
    }
}
