package com.mycheque.controller.validation;

import org.springframework.validation.Errors;

import com.mycheque.datatransfer.accept.Credentials;

import com.mycheque.service.wrapper.UpdatesWrapper;

/**
 * Interface for validating the {@code Customer}'s credential records.
 *
 * @author resxnvnce
 */
public interface CredentialsValidator {

    /**
     * Determines whether the given {@link Credentials} record is valid.
     *
     * @param credentials the record to inspect.
     * @param errors      a validation error storage.
     * @return {@code true} if these {@code Credentials} are valid, {@code false} otherwise.
     */
    boolean validate(Credentials credentials, Errors errors);

    /**
     * Determines whether the given {@link UpdatesWrapper} record is valid.
     *
     * @param updates the record to inspect.
     * @param errors  a validation error storage.
     * @return {@code true} if this {@code UpdatesWrapper} is valid, {@code false} otherwise.
     */
    boolean validate(UpdatesWrapper updates, Errors errors);
}
