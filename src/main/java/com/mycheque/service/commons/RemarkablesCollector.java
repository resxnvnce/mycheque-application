package com.mycheque.service.commons;

import java.util.Set;

import com.mycheque.util.core.Cleanable;

import com.mycheque.datatransfer.intermediate.Remarkable;

/**
 * Container interface for objects that wish to collect
 * and maintain the state of {@link Remarkable} records.
 *
 * @author resxnvnce
 * @apiNote It's a good practice to {@link #cleanUp()} the collector upon usage.
 */
public interface RemarkablesCollector extends Cleanable {

    /**
     * Does the remarkables {@linkplain #toRemarkables() collector}
     * have a {@code 1xx Warning} remarkable inside?
     *
     * @return {@code true} if there is at least one <b>warning</b> collected,
     *         {@code false} otherwise.
     */
    boolean has1xxWarnings();

    /**
     * Does the remarkables {@linkplain #toRemarkables() collector}
     * have a {@code 6xx Failure} remarkable inside?
     *
     * @return {@code true} if there is at least one <b>failure</b> collected,
     *         {@code false} otherwise.
     */
    boolean has6xxFailures();

    /**
     * Returns the {@code Remarkable}s collected, accumulated into a {@link Set}.
     * <p>
     * There is no requirement that a {@code Set} returned must be unmodifiable.
     *
     * @return the remarkable records collected for now.
     */
    Set<Remarkable> toRemarkables();

    /**
     * Adds all of the {@linkplain #toRemarkables() remarkables}
     * in {@code collector} to this collector and then
     * calls {@code collector.cleanUp()}.
     *
     * @param collector a {@link RemarkablesCollector} to be "stealed" from.
     */
    void stealAll(RemarkablesCollector collector);
}
