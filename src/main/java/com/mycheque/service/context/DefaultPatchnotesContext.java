package com.mycheque.service.context;

import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import com.mycheque.domain.Customer;
import com.mycheque.domain.id.FiscalDataRecord;

import com.mycheque.service.wrapper.PatchnotesOutcome;
import com.mycheque.service.wrapper.PatchnotesWrapper;
import com.mycheque.service.commons.PatchStateTracker;
import com.mycheque.service.commons.AbstractRemarkablesCollector;

import com.mycheque.datatransfer.accept.Patchnotes;
import com.mycheque.datatransfer.intermediate.Remarkable;

/**
 * The default {@link PatchnotesContext} implementation.
 * <p>
 * Its {@link #toOutcome()} method implementation filters out all the warnings,
 * if this behavior was requested by a customer.
 *
 * @author resxnvnce
 */
public final class DefaultPatchnotesContext extends AbstractRemarkablesCollector
        implements PatchnotesContext {

    /**
     * @see #getCustomer()
     */
    private final Customer customer;

    /**
     * @see #getPatchnotes()
     */
    private final Patchnotes patchnotes;

    /**
     * @see #getIdentifiers()
     */
    private Set<FiscalDataRecord> identifiers;

    /**
     * @see #getStateTrackers()
     */
    private Set<PatchStateTracker> stateTrackers;

    /**
     * Constructs a {@code DefaultPatchnotesContext}.
     *
     * @param wrapper a patchnotes wrapper record.
     */
    public DefaultPatchnotesContext(PatchnotesWrapper wrapper) {
        this.customer = wrapper.customer();
        this.patchnotes = wrapper.unwrap();
    }

    /**
     * Returns the {@code Remarkable}s collected, filtering out all the {@code 1xx Warning}s.
     *
     * @return all the {@code Remarkable}s collected, except for warnings.
     */
    private Set<Remarkable> toNon1xxWarningRemarkables() {
        return this.remarkables.stream()
                .filter(r -> !r.getRemarkCode().is1xxWarning())
                .collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
        this.identifiers.clear();
        this.stateTrackers.clear();
    }

    @Override
    public PatchnotesOutcome toOutcome() {
        final var rems = Set.copyOf(!has1xxWarningsEnabled() ? toNon1xxWarningRemarkables() : this.remarkables);
        final var outcome = new PatchnotesOutcome(rems, this.has1xxWarnings, this.has6xxFailures);

        this.cleanUp();
        return outcome;
    }

    @Override
    public void remark(Remarkable remarkable) {
        super.collect(remarkable);
    }

    @Override
    public Customer getCustomer() {
        return this.customer;
    }

    @Override
    public Patchnotes getPatchnotes() {
        return this.patchnotes;
    }

    @Override
    public Set<FiscalDataRecord> getIdentifiers() {
        if (this.identifiers == null) {
            this.identifiers = new HashSet<>();
        }

        return this.identifiers;
    }

    @Override
    public Set<PatchStateTracker> getStateTrackers() {
        if (this.stateTrackers == null) {
            this.stateTrackers = new HashSet<>();
        }

        return this.stateTrackers;
    }
}
