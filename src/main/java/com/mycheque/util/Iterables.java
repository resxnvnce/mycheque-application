package com.mycheque.util;

import java.util.Collection;

import org.springframework.lang.Nullable;

/**
 * Miscellaneous {@link Iterable} utility methods.
 *
 * @author resxnvnce
 */
public final class Iterables {

    /**
     * Determines whether the given collection, {@code coll}, is blank. That is,
     * the collection is either {@code null} or {@linkplain Collection#isEmpty() empty}.
     *
     * @param coll a {@link Collection} to inspect.
     * @return {@code true} if {@code coll} is blank;
     *         {@code false} otherwise.
     */
    public static boolean isBlank(@Nullable Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    /**
     * Nah-uh.
     */
    private Iterables() {
        throw new AssertionError("Utility class instantiation is not allowed!");
    }
}
