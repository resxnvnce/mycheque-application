package com.mycheque.util;

import java.util.Objects;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.function.BiFunction;

import com.mycheque.lang.Nullable;

/**
 * Assertion utility class that assists in validating arguments.
 * <p>
 * Useful for identifying programmer errors early and clearly at runtime.
 *
 * @author resxnvnce
 */
public final class Assert {

    /**
     * Assert a boolean expression, throwing an exception
     * produced by the exception supplying function,
     * if the expression evaluates to {@code false}.
     * <pre>{@code Assert.that(getId() != null, IllegalStateException::new);}</pre>
     *
     * @param expression        a boolean expression.
     * @param exceptionSupplier the supplying function that
     *                          produces the exception to be thrown.
     * @throws T if {@code expression} is {@code false}.
     */
    public static <T extends Throwable> void that(
            boolean expression, Supplier<? extends T> exceptionSupplier) throws T {

        if (!expression) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Assert a boolean expression, throwing an {@code IllegalStateException}
     * if the expression evaluates to {@code false}.
     * <pre>{@code Assert.state(entity.getId() != null, "The entity identifier is missing.");}</pre>
     *
     * @param expression a boolean expression.
     * @param message    the exception message to use if the assertion fails.
     * @throws IllegalStateException if {@code expression} is {@code false}.
     */
    public static void state(boolean expression, String message) throws IllegalStateException {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * Assert a boolean expression, throwing an {@code IllegalStateException}
     * if the expression evaluates to {@code false}.
     * <pre>{@code Assert.state(entity.getId() != null, () -> entity.getName() + " doesn't exist.");}</pre>
     *
     * @param expression      a boolean expression.
     * @param messageSupplier a supplier for the exception message to use if the assertion fails.
     * @throws IllegalStateException if {@code expression} is {@code false}.
     */
    public static void state(boolean expression, Supplier<String> messageSupplier) throws IllegalStateException {
        if (!expression) {
            String message = messageSupplier.get();
            throw new IllegalStateException(message);
        }
    }

    /**
     * Assert a boolean expression, throwing an {@code IllegalArgumentException}
     * if the expression evaluates to {@code false}.
     * <pre>{@code Assert.args(id > 0, "The entity identifier must be a positive integer.");}</pre>
     *
     * @param expression a boolean expression.
     * @param message    the exception message to use if the assertion fails.
     * @throws IllegalArgumentException if {@code expression} is {@code false}.
     */
    public static void args(boolean expression, String message) throws IllegalArgumentException {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert a boolean expression, throwing an {@code IllegalArgumentException}
     * if the expression evaluates to {@code false}.
     * <pre>{@code Assert.args(id > 0, () -> "The entity identifier " + id + " is non-positive.");}</pre>
     *
     * @param expression      a boolean expression.
     * @param messageSupplier a supplier for the exception message to use if the assertion fails.
     * @throws IllegalArgumentException if {@code expression} is {@code false}.
     */
    public static void args(boolean expression, Supplier<String> messageSupplier) throws IllegalArgumentException {
        if (!expression) {
            String message = messageSupplier.get();
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert that the given object, {@code o}, is not {@code null}.
     *
     * @param o       a nullable object to inspect.
     * @param message the exception message to use if the assertion fails.
     * @throws IllegalArgumentException if {@code o} is {@code null}.
     */
    public static void notNull(Object o, String message) throws IllegalArgumentException {
        Assert.args(o != null, message);
    }

    /**
     * Assert that the given object, {@code o}, is not {@code null}.
     *
     * @param o               a nullable object to inspect.
     * @param messageSupplier a supplier for the exception message to use if the assertion fails.
     * @throws IllegalArgumentException if {@code o} is {@code null}.
     */
    public static void notNull(Object o, Supplier<String> messageSupplier) throws IllegalArgumentException {
        Assert.args(o != null, messageSupplier);
    }

    /**
     * Assert that the given collection, {@code coll}, is not blank. That is,
     * the collection is neither {@code null} nor {@linkplain Collection#isEmpty() empty}.
     *
     * @param coll    a {@link Collection} to inspect.
     * @param message the exception message to use if the assertion fails.
     * @throws IllegalArgumentException if {@code coll} is blank.
     */
    public static void notBlank(Collection<?> coll, String message) throws IllegalArgumentException {
        Assert.args(!Iterables.isBlank(coll), message);
    }

    /**
     * Assert that the given collection, {@code coll}, is not blank. That is,
     * the collection is neither {@code null} nor {@linkplain Collection#isEmpty() empty}.
     *
     * @param coll            a {@link Collection} to inspect.
     * @param messageSupplier a supplier for the exception message to use if the assertion fails.
     * @throws IllegalArgumentException if {@code coll} is blank.
     */
    public static void notBlank(Collection<?> coll, Supplier<String> messageSupplier) throws IllegalArgumentException {
        Assert.args(!Iterables.isBlank(coll), messageSupplier);
    }

    /**
     * Assert that the given object {@code o1} is
     * {@linkplain Object#equals equal} to {@code o2}, throwing
     * an exception produced by the exception supplying function if that's not the case.
     * <pre>{@code Assert.equals(id, entity.getId(), IllegalArgumentException::new);}</pre>
     *
     * @param o1                the object {@code o2} is expected to be equal to.
     *                          Must not be {@code null}.
     * @param o2                the assertion subject.
     * @param exceptionSupplier the supplying function that
     *                          produces the exception to be thrown.
     * @throws T if {@code o1.equals(o2)} is {@code false}.
     */
    public static <T extends Throwable> void equals(
            Object o1, @Nullable Object o2, Supplier<? extends T> exceptionSupplier) throws T {

        Assert.that(o1.equals(o2), exceptionSupplier);
    }

    /**
     * Assert that the given object {@code o1} is
     * {@linkplain Object#equals equal} to {@code o2}, throwing
     * an exception produced by the exception supplying function if that's not the case.
     * <pre>{@code Assert.equals(id, entity.getId(), IdentifierMismatchException::new);}</pre>
     *
     * @param o1                the object {@code o2} is expected to be equal to.
     *                          Must not be {@code null}.
     * @param o2                the assertion subject.
     * @param exceptionProducer the supplying function that
     *                          produces the exception to be thrown.
     * @throws T if {@code o1.equals(o2)} is {@code false}.
     */
    public static <T extends Throwable, E, U> void equals(
            E o1, @Nullable U o2, BiFunction<? super E, ? super U, ? extends T> exceptionProducer) throws T {

        Assert.equals(o1, o2,
                () -> exceptionProducer.apply(o1, o2)
        );
    }

    /**
     * Assert that the given objects are {@linkplain Object#equals equal}
     * to each other, throwing an exception produced by the
     * exception supplying function if that's not the case.
     * <pre>{@code Assert.nullSafeEquals(id, entity.getId(), IllegalArgumentException::new);}</pre>
     *
     * @param o1                the object {@code o2} is expected to be equal to.
     * @param o2                the assertion subject.
     * @param exceptionSupplier the supplying function that
     *                          produces the exception to be thrown.
     * @throws T if {@code Objects.equals(o1, o2)} is {@code false}.
     */
    public static <T extends Throwable> void nullSafeEquals(
            @Nullable Object o1, @Nullable Object o2, Supplier<? extends T> exceptionSupplier) throws T {

        Assert.that(Objects.equals(o1, o2), exceptionSupplier);
    }

    /**
     * Assert that the given objects are {@linkplain Object#equals equal}
     * to each other, throwing an exception produced by the
     * exception supplying function if that's not the case.
     * <pre>{@code Assert.nullSafeEquals(id, entity.getId(), IdentifierMismatchException::new);}</pre>
     *
     * @param o1 the object {@code o2} is expected to be equal to.
     * @param o2 the assertion subject.
     * @param exceptionProducer the supplying function that
     *                          produces the exception to be thrown.
     * @throws T if {@code Objects.equals(o1, o2)} is {@code false}.
     */
    public static <T extends Throwable, E, U> void nullSafeEquals(
            @Nullable E o1, @Nullable U o2, BiFunction<? super E, ? super U, ? extends T> exceptionProducer) throws T {

        Assert.nullSafeEquals(o1, o2,
                () -> exceptionProducer.apply(o1, o2)
        );
    }

    /**
     * Nah-uh.
     */
    private Assert() {
        throw new AssertionError("Utility class instantiation is not allowed!");
    }
}
