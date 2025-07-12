package com.mycheque.controller.i18n;

import java.util.Locale;

import com.mycheque.lang.Nullable;
import com.mycheque.util.core.ConditionalChained;

import com.mycheque.service.wrapper.PatchnotesOutcome;
import com.mycheque.datatransfer.result.GenericResult;

/**
 * The {@link PatchnotesOutcome}-to-{@link GenericResult} mapper interface.
 * <p>
 * Such mappers participate in an <i>execution chain</i>, with their
 * encounter order defined by the {@link #getPosition() position} value.
 *
 * @author resxnvnce
 */
public interface PatchnotesOutcomeResultExtractor extends ConditionalChained<PatchnotesOutcome> {

    /**
     * Transform the given {@link PatchnotesOutcome} into a
     * {@link GenericResult}, or return {@code null} if this kind
     * of outcome does not {@linkplain #matches(Object) match} the mapper.
     *
     * @param outcome the mapping target.
     * @param locale  the {@link Locale} to use for message resolving.
     * @return a {@code GenericResult} mapped or {@code null}
     *         if the {@code PatchnotesOutcome} does not match this mapper.
     */
    @Nullable
    GenericResult extract(PatchnotesOutcome outcome, Locale locale);
}
