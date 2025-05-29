package com.mycheque.validation;

import com.mycheque.client.ClientTemplate;

import org.springframework.validation.Errors;

/**
 * The default {@link CustomerTokenValidator} implementation,
 * delegating the condition evaluation to a {@link ClientTemplate}.
 *
 * @author resxnvnce
 */
public class DelegatingCustomerTokenValidator implements CustomerTokenValidator {

    /**
     * The template delegate.
     */
    private final ClientTemplate delegate;

    /**
     * Constructs a {@code DelegatingCustomerTokenValidator}.
     *
     * @param delegate the template to delegate the condition evaluation to.
     */
    public DelegatingCustomerTokenValidator(ClientTemplate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void validate(Object target, Errors errors) {
        final String token = (String) target;
        boolean isTokenValid = delegate.canAuthorizeWith(token);

        if (!isTokenValid)
            errors.rejectValue("token", "@constraint.token#should-exist");
    }
}
