package com.mycheque.service.wrapper;

import java.util.Set;

import com.mycheque.datatransfer.intermediate.Remarkable;

/**
 * A data transfer object carrying all the {@link Remarkable}s created
 * by applying or <i>trying to apply</i> some customer patchnotes.
 *
 * @param remarkables    the {@code Remarkable}s created.
 * @param has1xxWarnings whether there is at least one warning class remarkable.
 * @param has6xxFailures whether there is at least one failure class remarkable.
 * @author resxnvnce
 */
public record PatchnotesOutcome(Set<Remarkable> remarkables, boolean has1xxWarnings, boolean has6xxFailures) {

    /**
     * Is this outcome OK, i.e. not having a single {@link Remarkable}?
     *
     * @return {@code true} if this outcome does not contain {@code Remarkable}s,
     *         {@code false} otherwise.
     */
    public boolean isOk() {
        return remarkables().isEmpty();
    }
}
