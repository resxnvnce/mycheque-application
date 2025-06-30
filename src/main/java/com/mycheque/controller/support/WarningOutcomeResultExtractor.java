package com.mycheque.controller.support;

import java.util.Locale;

import org.springframework.stereotype.Component;

import com.mycheque.controller.i18n.MessageResolver;
import com.mycheque.datatransfer.result.GenericResult;
import com.mycheque.service.wrapper.PatchnotesOutcome;

/**
 * A {@code PatchnotesOutcomeResultExtractor} eligible to work with {@code 1xx Warning} outcomes.
 *
 * @author resxnvnce
 */
@Component
public final class WarningOutcomeResultExtractor extends AbstractPatchnotesOutcomeResultExtractor {

    /**
     * Constructs a {@code WarningOutcomeResultExtractor}.
     *
     * @param messageResolver the {@link GenericResult} messages supplier.
     */
    public WarningOutcomeResultExtractor(MessageResolver messageResolver) {
        super(messageResolver);
    }

    @Override
    public int getPosition() {
        return 0;
    }

    @Override
    public boolean matches(PatchnotesOutcome outcome) {
        return outcome.has1xxWarnings();
    }

    @Override
    protected GenericResult extractMatching(PatchnotesOutcome outcome, Locale locale) {
        final var description = super.explain(outcome, locale);

        return GenericResult.succeeded(this.messageResolver.on1xxWarningOutcome(locale), description);
    }
}
