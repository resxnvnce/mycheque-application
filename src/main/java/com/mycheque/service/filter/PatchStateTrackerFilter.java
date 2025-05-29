package com.mycheque.service.filter;

import com.mycheque.service.commons.PatchStateTracker;
import com.mycheque.service.context.PatchnotesContext;

/**
 * Interface supporting with filtering the {@link PatchStateTracker}s
 * that are no longer should be processed by whatever reason.
 *
 * @author resxnvnce
 */
public interface PatchStateTrackerFilter {

    /**
     * Inspect the given {@code PatchStateTracker} after an integration.
     * <p>
     * If the tracker's {@linkplain PatchStateTracker#getEntityState() entity state}
     * is present, look for its identifier in the {@code context}, putting a duplicate-indicating
     * remarkable if necessary. If the identifier is a new one, determine if it's already present
     * in the persistence store, putting a proper remarkable if that's the case.
     *
     * @param tracker a {@link PatchStateTracker} to inspect.
     * @param context the current patchnotes context.
     * @return {@code true} if the {@code tracker} is <b>not</b> worth processing any further,
     *         {@code false} otherwise.
     */
    boolean afterIntegration(PatchStateTracker tracker, PatchnotesContext context);

    /**
     * Inspect the given {@code PatchStateTracker} before an integration.
     * <p>
     * If the tracker's {@linkplain PatchStateTracker#getIntermediateState() intermediate state}
     * has an identifier, add it to the {@code context} and determine if it's already present
     * in the persistence store, putting a proper remarkable if that's the case.
     *
     * @param tracker a {@link PatchStateTracker} to inspect.
     * @param context the current patchnotes context.
     * @return {@code true} if the {@code tracker} is <b>not</b> worth processing any further,
     *         {@code false} otherwise.
     */
    boolean beforeIntegration(PatchStateTracker tracker, PatchnotesContext context);
}
