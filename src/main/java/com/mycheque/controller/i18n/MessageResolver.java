package com.mycheque.controller.i18n;

import java.util.Locale;

import org.springframework.validation.Errors;

import com.mycheque.datatransfer.intermediate.PatchRemarkCode;

import com.mycheque.service.exception.TokenAlreadyInUseException;

/**
 * Utility interface to resolve localized messages for common scenarios.
 *
 * @author resxnvnce
 */
public interface MessageResolver {

    /* General */

    /**
     * Get the message explaining that a query result is empty.
     *
     * @param locale the {@link Locale} in which to do the lookup.
     * @return a message describing that a query returned no result.
     */
    String onEmptyResult(Locale locale);

    /* Exceptions */

    /**
     * Resolve a message explaining the {@code TokenAlreadyInUseException} thrown.
     *
     * @param exception an {@code Exception} to transform into a user-friendly message.
     * @param locale    the {@link Locale} in which to do the lookup.
     * @return a displayable message explaining the exception encountered.
     */
    String onException(TokenAlreadyInUseException exception, Locale locale);

    /* Customer Endpoints */

    /**
     * Get the message for a {@code Customer} update success.
     *
     * @param locale the {@link Locale} in which to do the lookup.
     * @return a message describing a {@code Customer} update success.
     */
    String onUpdate(Locale locale);

    /**
     * Get the message for a {@code Customer} registration success.
     *
     * @param locale the {@link Locale} in which to do the lookup.
     * @return a message describing a {@code Customer} registration success.
     */
    String onRegistration(Locale locale);

    /**
     * Build a message for a scenario in which a {@code Customer} update fails.
     *
     * @param errors a storage of the validation errors encountered.
     * @param locale the {@link Locale} in which to do the lookup.
     * @return a message describing a {@code Customer} update failure.
     */
    String onFailedUpdate(Errors errors, Locale locale);

    /**
     * Build a message for a scenario in which a {@code Customer} registration fails.
     *
     * @param errors a storage of the validation errors encountered.
     * @param locale the {@link Locale} in which to do the lookup.
     * @return a message describing a {@code Customer} registration failure.
     */
    String onFailedRegistration(Errors errors, Locale locale);

    /* Receipt Endpoints */

    /**
     * Resolve a message explaining the given {@link PatchRemarkCode}.
     *
     * @param prc    a {@code PatchRemarkCode} to transform into a user-friendly message.
     * @param locale the {@link Locale} in which to do the lookup.
     * @return a displayable message explaining the patch remark code encountered.
     */
    String onRemarkCode(PatchRemarkCode prc, Locale locale);

    /**
     * Resolve a message explaining the patchnotes outcome
     * is fine.
     *
     * @param locale the {@link Locale} in which to do the lookup.
     * @return a displayable message explaining the patchnotes outcome.
     */
    String onOkOutcome(Locale locale);

    /**
     * Resolve a message explaining the patchnotes outcome
     * has {@code 1xx Warning} class remarkables.
     *
     * @param locale the {@link Locale} in which to do the lookup.
     * @return a displayable message explaining the patchnotes outcome.
     */
    String on1xxWarningOutcome(Locale locale);

    /**
     * Resolve a message explaining the patchnotes outcome
     * has {@code 6xx Failure} class remarkables.
     *
     * @param locale the {@link Locale} in which to do the lookup.
     * @return a displayable message explaining the patchnotes outcome.
     */
    String on6xxFailureOutcome(Locale locale);

    /**
     * Build a message for a scenario in which
     * the validation process for a {@code Patchnotes} record has failed.
     *
     * @param errors a storage of the validation errors encountered.
     * @param locale the {@link Locale} in which to do the lookup.
     * @return a message describing an illegal {@code Patchnotes} record has been provided.
     */
    String onIllegalPatchnotes(Errors errors, Locale locale);

    /**
     * Build a message for a scenario in which
     * the validation process for a {@code ReceiptQuery} record has failed.
     *
     * @param errors a storage of the validation errors encountered.
     * @param locale the {@link Locale} in which to do the lookup.
     * @return a message describing an illegal {@code ReceiptQuery} record has been provided.
     */
    String onIllegalReceiptQuery(Errors errors, Locale locale);
}
