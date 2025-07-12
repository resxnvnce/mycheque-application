package com.mycheque.service.observer;

import com.mycheque.lang.Nullable;

import org.springframework.data.mongodb.core.query.Criteria;

/**
 * An interface for objects participating in {@link Criteria} creation.
 * <p>
 * Each of the implementations look for the exact properties
 * of an object the {@code Criteria} based on.
 *
 * @param <T> the type of objects the {@code Criteria} is being built from.
 * @author resxnvnce
 */
@FunctionalInterface
public interface CriteriaObserver<T> {

    /**
     * Returns a {@link Criteria} describing some properties or {@code null}
     * if there's no properties this observer can build a criteria from.
     *
     * @param t the object from which the {@code Criteria} is being built.
     * @return a criteria matching one or more properties (if present).
     */
    @Nullable
    Criteria deriveIfNecessary(T t);

    /**
     * Build a {@link Criteria} for a lower bound and an upper one.
     *
     * @param min the minimum value, may be {@code null}.
     * @param max the maximum value, may be {@code null}.
     * @param key the property or field name.
     * @return a {@code Criteria} involving the given non-{@code null} bounds.
     * @see Criteria#gte(Object)
     * @see Criteria#lte(Object)
     */
    @Nullable
    static <N extends Number> Criteria between(@Nullable N min, @Nullable N max, String key) {
        if (min == null && max == null) {
            return null;
        }

        var criteria = Criteria.where(key);

        if (min != null) {
            criteria.gte(min);
        }
        if (max != null) {
            criteria.lte(max);
        }

        return criteria;
    }
}
