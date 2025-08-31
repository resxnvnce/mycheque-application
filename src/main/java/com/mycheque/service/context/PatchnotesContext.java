package com.mycheque.service.context;

import java.util.Set;

import com.mycheque.domain.Customer;
import com.mycheque.domain.id.FiscalDataRecord;

import com.mycheque.service.wrapper.AuthorizedWrapper;
import com.mycheque.service.wrapper.PatchnotesOutcome;
import com.mycheque.service.commons.PatchStateTracker;
import com.mycheque.service.commons.RemarkablesCollector;

import com.mycheque.datatransfer.query.Patchnotes;
import com.mycheque.datatransfer.intermediate.Remarkable;

/**
 * Represents a context of some patchnotes application attempt.
 * <p>
 * {@code PatchnotesContext} allows to collect and maintain the {@linkplain #toRemarkables() remarkables}
 * encountered; to store and populate the {@linkplain #getIdentifiers() fiscal identifiers} already
 * processed; to utilize and possibly remove the {@linkplain #getStateTrackers() patch state trackers}.
 *
 * @author resxnvnce
 * @apiNote It's a good practice to {@link #cleanUp()} the context upon usage
 *         by implicitly calling this method or by invoking {@link #toOutcome()}.
 */
public interface PatchnotesContext extends RemarkablesCollector {

    /**
     * Get the {@code PatchnotesOutcome} and close
     * the context, which is not meant to be used afterward.
     *
     * @return the outcome of patchnotes application.
     */
    PatchnotesOutcome toOutcome();

    /**
     * Push the given {@code Remarkable} into the {@linkplain #toRemarkables() collector}.
     *
     * @param remarkable to be pushed.
     */
    void remark(Remarkable remarkable);

    /**
     * Push all the {@code Remarkable}s given into the {@linkplain #toRemarkables() collector}.
     *
     * @param remarkables to be pushed.
     */
    default void remarkAll(Iterable<Remarkable> remarkables) {
        remarkables.forEach(this::remark);
    }

    /**
     * Returns the {@code Customer} this context serves.
     *
     * @return the customer this patchnotes context is working with.
     */
    Customer getCustomer();

    /**
     * Returns the patchnotes this {@code PatchnotesContext} belongs to.
     *
     * @return the {@link Patchnotes} record used to this instantiate this context.
     */
    Patchnotes getPatchnotes();

    /**
     * Are {@code 1xx Warning} class {@code Remarkable}s enabled within this context?
     *
     * @return {@code true} if <b>warnings</b> should be displayed,
     *         {@code false} otherwise.
     */
    default boolean has1xxWarningsEnabled() {
        return getPatchnotes().shouldDisplay1xxWarnings();
    }

    /**
     * Returns a {@link Set} of identifiers that
     * have been already processed within this context.
     * <p>
     * <b>NOTE:</b> It's the caller responsibility to populate the output set.
     *
     * @return a mutable set of {@link FiscalDataRecord}s
     *         considered as already processed within this context.
     */
    Set<FiscalDataRecord> getIdentifiers();

    /**
     * Returns a {@link Set} of patch state trackers remaining.
     * <p>
     * <b>NOTE:</b> It's the caller responsibility to populate the output set.
     *
     * @return a set of {@code PatchStateTracker}s that haven't been removed yet.
     */
    Set<PatchStateTracker> getStateTrackers();

    /**
     * Create a {@link PatchnotesContext} within a request scope.
     *
     * @param wrapper an authorized wrapper with the patchnotes to be applied.
     * @return a patchnotes context.
     */
    static PatchnotesContext unwrapping(AuthorizedWrapper<Patchnotes> wrapper) {
        return new DefaultPatchnotesContext(wrapper);
    }
}
