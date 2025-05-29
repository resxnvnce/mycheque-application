package com.mycheque.service.commons;

import java.util.Set;
import java.util.HashSet;

import com.mycheque.datatransfer.intermediate.Remarkable;

/**
 * Abstract base class for the {@link RemarkablesCollector} interface implementations.
 * <p>
 * Its {@link #toRemarkables()} method implementation
 * returns an {@linkplain Set#copyOf unmodifiable} set,
 * but in specific scenarios this behavior might be overriden.
 *
 * @author resxnvnce
 */
public abstract class AbstractRemarkablesCollector implements RemarkablesCollector {

    /**
     * Set of the {@link Remarkable}s found.
     */
    protected final Set<Remarkable> remarkables = new HashSet<>();

    /**
     * Should be set to {@code true} whenever the collector encounters a {@code 1xx Warning}.
     */
    protected boolean has1xxWarnings = false;

    /**
     * Should be set to {@code true} whenever the collector encounters a {@code 6xx Failure}.
     */
    protected boolean has6xxFailures = false;

    /**
     * Collect the given {@code Remarkable}.
     *
     * @param remarkable to be collected.
     */
    protected void collect(Remarkable remarkable) {
        this.remarkables.add(remarkable);

        if (!this.has1xxWarnings) {
            has1xxWarnings = remarkable.getRemarkCode().is1xxWarning();
        }

        if (!this.has6xxFailures) {
            has6xxFailures = remarkable.getRemarkCode().is6xxFailure();
        }
    }

    @Override
    public boolean has1xxWarnings() {
        return this.has1xxWarnings;
    }

    @Override
    public boolean has6xxFailures() {
        return this.has6xxFailures;
    }

    @Override
    public Set<Remarkable> toRemarkables() {
        return Set.copyOf(this.remarkables);
    }

    @Override
    public void cleanUp() {
        this.remarkables.clear();
        this.has1xxWarnings = false;
        this.has6xxFailures = false;
    }

    @Override
    public void stealAll(RemarkablesCollector collector) {
        collector.toRemarkables().forEach(this::collect);
        collector.cleanUp();
    }
}
