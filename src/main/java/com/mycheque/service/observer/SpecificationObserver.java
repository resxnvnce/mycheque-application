package com.mycheque.service.observer;

import org.springframework.lang.Nullable;
import org.springframework.data.jpa.domain.Specification;

/**
 * An interface for objects participating in {@link Specification} creation.
 * <p>
 * Each of the implementations look for the exact properties
 * of an object the {@code Specification} based on.
 *
 * @param <T> the type of objects the specification is being built from.
 * @param <R> the type of objects described by the specification.
 * @author resxnvnce
 */
@FunctionalInterface
public interface SpecificationObserver<T, R> {

    /**
     * Returns a {@link Specification} describing some properties or {@code null}
     * if there's no properties this observer can build a specification from.
     *
     * @param t the object from which the {@code Specification} is being built.
     * @return a specification matching one or more properties (if present).
     */
    @Nullable
    Specification<R> deriveIfNecessary(T t);
}
