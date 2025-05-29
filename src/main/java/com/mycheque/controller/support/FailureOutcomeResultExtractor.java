package com.mycheque.controller.support;

import java.util.Locale;

import org.springframework.stereotype.Component;

import com.mycheque.controller.i18n.MessageResolver;

import com.mycheque.datatransfer.expose.GenericResult;

import com.mycheque.service.wrapper.PatchnotesOutcome;

/**
 * A {@code PatchnotesOutcomeResultExtractor} eligible to work with {@code 6xx Failure} outcomes.
 *
 * @author resxnvnce
 */
@Component
public final class FailureOutcomeResultExtractor extends AbstractPatchnotesOutcomeResultExtractor {

    /**
     * Constructs a {@code FailureOutcomeResultExtractor}.
     *
     * @param messageResolver the {@link GenericResult} messages supplier.
     */
    public FailureOutcomeResultExtractor(MessageResolver messageResolver) {
        super(messageResolver);
    }

    @Override
    public int getPosition() {
        return HEAD;
    }

    @Override
    public boolean matches(PatchnotesOutcome outcome) {
        return outcome.has6xxFailures();
    }

    @Override
    protected GenericResult extractMatching(PatchnotesOutcome outcome, Locale locale) {
        final var description = super.explain(outcome, locale);

        return GenericResult.failed(this.messageResolver.on6xxFailureOutcome(locale), description);
    }
}
