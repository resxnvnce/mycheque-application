package com.mycheque.validation;

import org.springframework.validation.Validator;

/**
 * {@link Validator} extension capable of validating
 * {@linkplain com.mycheque.domain.Customer#getThirdpartyToken() tokens}.
 * <p>
 * In this specific case, a token is considered valid if
 * the third party API provider recognizes it.
 *
 * @author resxnvnce
 */
public interface CustomerTokenValidator extends Validator {

    @Override
    default boolean supports(Class<?> clazz) {
        return String.class == clazz;
    }
}
