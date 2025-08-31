package com.mycheque.controller.validation;

import java.util.function.Consumer;

import org.springframework.validation.Errors;

import com.mycheque.lang.Nullable;

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
     * Perform an {@code action} over a {@code newValue} if
     * the value is <i>updatable</i>, which means it's not {@code null}
     * and not {@linkplain Object#equals(Object) equal} to the {@code oldValue}.
     *
     * @param oldValue the old value of some property.
     * @param newValue a new value of the same property.
     * @param action   an action to perform.
     */
    default <T> void acceptIfUpdatable(@Nullable Object oldValue, @Nullable T newValue, Consumer<T> action) {
        boolean isUpdatable = newValue != null && !newValue.equals(oldValue);

        if (isUpdatable) {
            action.accept(newValue);
        }
    }

    /**
     * Determines whether the given {@link Credentials} are valid,
     * populating the validation {@code errors} storage if needed.
     *
     * @param inspected the object to inspect.
     * @param errors    a validation error storage.
     * @return {@code true} if the given object is valid, {@code false} otherwise.
     */
    boolean validate(Credentials inspected, Errors errors);

    /**
     * Determines whether the given {@link CredentialsUpdate} is valid,
     * populating the validation {@code errors} storage if needed.
     *
     * @param inspected the object to inspect.
     * @param errors    a validation error storage.
     * @return {@code true} if the given object is valid, {@code false} otherwise.
     */
    boolean validate(AuthorizedWrapper<CredentialsUpdate> inspected, Errors errors);
}
