package com.mycheque.util;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.Predicate;

import org.springframework.lang.Nullable;

/**
 * Handy utility class providing convenience methods to work
 * with {@linkplain FunctionalInterface functional interfaces}.
 *
 * @author resxnvnce
 */
public final class Lambdas {

    /**
     * Get a result from the given {@link Supplier}
     * or return {@code null} if it's not present.
     *
     * @param supplier the supplying function to get a result from.
     * @return {@code supplier.get()} or {@code null} if the supplying function is not present.
     */
    @Nullable
    public static <T> T nullSafeGet(@Nullable Supplier<T> supplier) {
        return supplier != null ? supplier.get() : null;
    }

    /**
     * Evaluate the given {@link Predicate} on the given object
     * in case it's not {@code null}; otherwise, return {@code true}.
     *
     * @param obj       a nullable object to be {@linkplain Predicate#test(Object) tested}.
     * @param predicate the predicate to test with. Must not be {@code null} itself!
     * @return {@code predicate.test(obj)} or {@code true}.
     */
    public static <T> boolean testOrTrue(@Nullable T obj, Predicate<? super T> predicate) {
        return obj == null || predicate.test(obj);
    }

    /**
     * Evaluate the given {@link Predicate} on the given object
     * in case it's not {@code null}; otherwise, return {@code false}.
     *
     * @param obj       a nullable object to be {@linkplain Predicate#test(Object) tested}.
     * @param predicate the predicate to test with. Must not be {@code null} itself!
     * @return {@code predicate.test(obj)} or {@code false}.
     */
    public static <T> boolean testOrFalse(@Nullable T obj, Predicate<? super T> predicate) {
        return obj != null && predicate.test(obj);
    }

    /**
     * Apply the given {@link Function} to the nullable {@code Object}
     * in case it isn't {@code null}; otherwise return {@code null}.
     *
     * @param obj      a nullable object to be returned or to be
     *                 used as the argument of the function.
     * @param function the function to apply on the object if it isn't {@code null}.
     *                 Must not be {@code null} itself!
     * @return {@code function.apply(obj)} or {@code null}.
     */
    @Nullable
    public static <T, R> R applyOrNull(@Nullable T obj, Function<? super T, R> function) {
        return obj != null ? function.apply(obj) : null;
    }

    /**
     * Nah-uh.
     */
    private Lambdas() {
        throw new AssertionError("Utility class instantiation is not allowed!");
    }
}
