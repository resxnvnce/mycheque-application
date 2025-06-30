package com.mycheque.controller.support;

import java.util.Locale;

import org.springframework.stereotype.Component;

import com.mycheque.controller.i18n.MessageResolver;
import com.mycheque.datatransfer.result.GenericResult;
import com.mycheque.service.wrapper.PatchnotesOutcome;

/**
 * A {@code PatchnotesOutcomeResultExtractor} eligible to work with
 * outcomes having {@linkplain PatchnotesOutcome#isOk() no errors}.
 * <p>
 * Currently, this is the fallback result extractor.
 *
 * @author resxnvnce
 */
@Component
public final class BasicOutcomeResultExtractor extends AbstractPatchnotesOutcomeResultExtractor {

    /**
     * Constructs a {@code BasicOutcomeResultExtractor}.
     *
     * @param messageResolver the {@link GenericResult} messages supplier.
     */
    public BasicOutcomeResultExtractor(MessageResolver messageResolver) {
        super(messageResolver);
    }

    @Override
    public int getPosition() {
        return TAIL;
    }

    @Override
    public boolean matches(PatchnotesOutcome outcome) {
        return outcome.isOk();
    }

    @Override
    protected GenericResult extractMatching(PatchnotesOutcome outcome, Locale locale) {
        final var message = this.messageResolver.onOkOutcome(locale);

        return GenericResult.succeeded(message);
    }
}
