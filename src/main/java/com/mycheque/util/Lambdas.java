package com.mycheque.util;

import java.util.function.Supplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.mycheque.lang.Nullable;

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
     * @return {@code supplier.get()} or {@code null}.
     */
    @Nullable
    public static <T> T nullSafeGet(@Nullable Supplier<T> supplier) {
        return supplier != null ? supplier.get() : null;
    }

    /**
     * Apply the given {@link Function} to a nullable object
     * in case it's present; otherwise return {@code null}.
     *
     * @param obj      an object to be used as the function argument.
     * @param function the function to apply.
     * @return {@code function.apply(obj)} or {@code null}.
     */
    @Nullable
    public static <T, R> R applyOrNull(@Nullable T obj, Function<? super T, R> function) {
        return obj != null ? function.apply(obj) : null;
    }

    /**
     * Use the given {@link Consumer} to accept a nullable object
     * in case it's present; otherwise do nothing.
     *
     * @param obj      an object to be used as the consumer argument.
     * @param consumer the consumer.
     */
    public static <T> void acceptIfPresent(@Nullable T obj, Consumer<? super T> consumer) {
        if (obj != null) {
            consumer.accept(obj);
        }
    }

    /**
     * Evaluate the given {@link Predicate} on the given object
     * in case it's not {@code null}; otherwise, return {@code true}.
     *
     * @param obj       a nullable object to be {@linkplain Predicate#test(Object) tested}.
     * @param predicate the predicate to test with.
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
     * @param predicate the predicate to test with.
     * @return {@code predicate.test(obj)} or {@code false}.
     */
    public static <T> boolean testOrFalse(@Nullable T obj, Predicate<? super T> predicate) {
        return obj != null && predicate.test(obj);
    }

    /**
     * Nah-uh.
     */
    private Lambdas() {
        throw new AssertionError("Utility class instantiation is not allowed!");
    }
}
