package com.mycheque.validation;

import com.mycheque.service.CustomerService;

import org.springframework.validation.Errors;

/**
 * The default {@link UsernameNotTakenValidator} implementation,
 * delegating the condition evaluation to a {@link CustomerService}.
 *
 * @author resxnvnce
 */
public class DelegatingUsernameNotTakenValidator implements UsernameNotTakenValidator {

    /**
     * The service delegate.
     */
    private final CustomerService delegate;

    /**
     * Constructs a {@code DelegatingUsernameNotTakenValidator}.
     *
     * @param delegate the service to delegate the condition evaluation to.
     */
    public DelegatingUsernameNotTakenValidator(CustomerService delegate) {
        this.delegate = delegate;
    }

    @Override
    public void validate(Object target, Errors errors) {
        final String username = (String) target;
        boolean isUsernameTaken = delegate.existsByUsername(username);

        if (isUsernameTaken)
            errors.rejectValue("profile.username", "@constraint.username#should-not-be-taken");
    }
}
