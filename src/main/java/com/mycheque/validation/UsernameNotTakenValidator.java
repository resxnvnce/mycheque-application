package com.mycheque.validation;

import org.springframework.validation.Validator;

/**
 * {@link Validator} extension capable of validating
 * {@linkplain com.mycheque.domain.Customer#getUsername() usernames}.
 * <p>
 * In this specific case, a username is considered valid if it's not taken yet.
 *
 * @author resxnvnce
 */
public interface UsernameNotTakenValidator extends Validator {

    @Override
    default boolean supports(Class<?> clazz) {
        return String.class == clazz;
    }
}
