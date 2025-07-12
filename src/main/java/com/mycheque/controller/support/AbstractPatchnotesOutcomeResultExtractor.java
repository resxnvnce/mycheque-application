package com.mycheque.controller.support;

import java.util.Locale;

import com.mycheque.lang.Nullable;

import com.mycheque.controller.i18n.MessageResolver;
import com.mycheque.controller.i18n.PatchnotesOutcomeResultExtractor;

import com.mycheque.datatransfer.result.GenericResult;
import com.mycheque.datatransfer.result.RemarkableExplained;

import com.mycheque.service.wrapper.PatchnotesOutcome;

/**
 * Base class for the {@link PatchnotesOutcomeResultExtractor} implementations.
 *
 * @author resxnvnce
 */
public abstract class AbstractPatchnotesOutcomeResultExtractor implements PatchnotesOutcomeResultExtractor {

    /**
     * Localized, interpolated messages supplier.
     */
    protected final MessageResolver messageResolver;

    /**
     * The constructor for subclasses to call.
     *
     * @param messageResolver the {@link GenericResult} messages supplier.
     */
    protected AbstractPatchnotesOutcomeResultExtractor(MessageResolver messageResolver) {
        this.messageResolver = messageResolver;
    }

    /**
     * Explain the given {@link PatchnotesOutcome}, returning an array of
     * explanation records for each of the {@code Remarkable}s found, in encounter order.
     *
     * @param outcome the {@code PatchnotesOutcome} which remarkables to explain.
     * @param locale  the {@link Locale} in which to do the lookup.
     * @return remarkables explained.
     */
    protected RemarkableExplained[] explain(PatchnotesOutcome outcome, Locale locale) {
        return outcome.remarkables().stream()
                .map(
                        r -> {
                            String explanation = this.messageResolver.onRemarkCode(r.getRemarkCode(), locale);
                            return new RemarkableExplained(r.getSubject(), explanation);
                        }
                )
                .toArray(RemarkableExplained[]::new);
    }

    @Override
    public final @Nullable GenericResult extract(PatchnotesOutcome outcome, Locale locale) {
        return matches(outcome) ? extractMatching(outcome, locale) : null;
    }

    /**
     * Perform the mapping of a {@link PatchnotesOutcome} matching this mapper.
     *
     * @param outcome the mapping target, matching this mapper.
     * @param locale  the {@link Locale} to use for message resolving.
     * @return a {@code GenericResult} mapped, never {@code null}.
     */
    protected abstract GenericResult extractMatching(PatchnotesOutcome outcome, Locale locale);
}
