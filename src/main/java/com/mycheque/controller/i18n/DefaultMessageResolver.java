package com.mycheque.controller.i18n;

import java.util.Locale;
import java.util.StringJoiner;

import org.springframework.stereotype.Component;

import org.springframework.validation.Errors;
import org.springframework.context.MessageSource;

import com.mycheque.validation.configure.BeanValidation;

import com.mycheque.datatransfer.intermediate.PatchRemark;
import com.mycheque.datatransfer.intermediate.PatchRemarkCode;

/**
 * The default {@link MessageResolver} implementation.
 *
 * @author resxnvnce
 */
@Component
public class DefaultMessageResolver implements MessageResolver {

    /**
     * The underlying {@code MessageSource}.
     */
    private final MessageSource source;

    /**
     * Constructs a {@code DefaultMessageResolver}.
     *
     * @param source the bean validation message source.
     */
    public DefaultMessageResolver(@BeanValidation MessageSource source) {
        this.source = source;
    }

    /**
     * Resolve the message code suffix (which comes after the dot).
     *
     * @param prc a {@link PatchRemarkCode} to get a message code suffix by.
     * @return the proper suffix for a message.
     */
    private String getMessageCodeSuffix(PatchRemarkCode prc) {
        return prc instanceof PatchRemark pr ? pr.name() : "UNEXPECTED";
    }

    /**
     * Resolve the message by its {@code code} and {@code locale}.
     *
     * @param code   the message code to look up.
     * @param locale the {@link Locale} in which to do the lookup.
     * @return the message resolved, never {@code null}.
     */
    private String getMessageNoArgs(String code, Locale locale) {
        return this.source.getMessage(code, null, locale);
    }

    /**
     * Resolve the localized message for each of
     * the {@linkplain Errors#getAllErrors() errors},
     * join them and then return the result {@code String}.
     *
     * @param errors a storage of the validation errors encountered.
     * @param locale the {@link Locale} in which to do the lookup.
     * @return a joined string describing all the validation errors found.
     */
    private String toJoinedString(Errors errors, Locale locale) {
        final var joiner = new StringJoiner("; ", "[ ", " ]");

        errors.getAllErrors().forEach(
                error -> {
                    final String message = this.source.getMessage(error, locale);
                    joiner.add(message);
                }
        );

        return joiner.toString();
    }

    /**
     * Resolve the message for a failed action, using the
     * {@link Errors} provided for interpolating <b>the only</b> argument.
     *
     * @param code   the message code to look up.
     * @param errors the validation errors storage to compute
     *               the only argument of an interpolated message.
     * @param locale the {@link Locale} in which to do the lookup.
     * @return the message resolved, never {@code null}.
     */
    private String onFailedAction(String code, Errors errors, Locale locale) {
        final Object[] args = {
                toJoinedString(errors, locale)
        };

        return this.source.getMessage(code, args, locale);
    }

    /* Customer endpoints */

    @Override
    public String onUpdate(Locale locale) {
        return getMessageNoArgs("@scenario.update", locale);
    }

    @Override
    public String onRegistration(Locale locale) {
        return getMessageNoArgs("@scenario.registration", locale);
    }

    @Override
    public String onFailedUpdate(Errors errors, Locale locale) {
        return onFailedAction("@scenario.failed-update", errors, locale);
    }

    @Override
    public String onFailedRegistration(Errors errors, Locale locale) {
        return onFailedAction("@scenario.failed-registration", errors, locale);
    }

    /* Receipt Endpoints */

    @Override
    public String onOkOutcome(Locale locale) {
        return getMessageNoArgs("@scenario.ok-outcome", locale);
    }

    @Override
    public String on1xxWarningOutcome(Locale locale) {
        return getMessageNoArgs("@scenario.warning-outcome", locale);
    }

    @Override
    public String on6xxFailureOutcome(Locale locale) {
        return getMessageNoArgs("@scenario.failure-outcome", locale);
    }

    @Override
    public String onIllegalPatchnotes(Errors errors, Locale locale) {
        return onFailedAction("@scenario.illegal-patchnotes", errors, locale);
    }

    @Override
    public String onRemarkCode(PatchRemarkCode prc, Locale locale) {
        return getMessageNoArgs("@remark." + getMessageCodeSuffix(prc), locale);
    }
}
