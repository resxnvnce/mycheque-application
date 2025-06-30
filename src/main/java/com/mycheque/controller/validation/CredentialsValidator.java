package com.mycheque.controller.validation;

import org.springframework.validation.Errors;

import com.mycheque.datatransfer.profile.Credentials;
import com.mycheque.datatransfer.profile.CredentialsUpdate;

import com.mycheque.service.wrapper.AuthorizedWrapper;

/**
 * Interface for validating a {@code Customer} credentials.
 *
 * @author resxnvnce
 */
public interface CredentialsValidator {

    /**
     * Determines whether the given {@link Credentials} are valid.
     *
     * @param inspected the object to inspect.
     * @param errors    a validation error storage.
     * @return {@code true} if the given object is valid, {@code false} otherwise.
     */
    boolean validate(Credentials inspected, Errors errors);

    /**
     * Determines whether the given {@link CredentialsUpdate} is valid.
     *
     * @param inspected the object to inspect.
     * @param errors    a validation error storage.
     * @return {@code true} if the given object is valid, {@code false} otherwise.
     */
    boolean validate(AuthorizedWrapper<CredentialsUpdate> inspected, Errors errors);
}
