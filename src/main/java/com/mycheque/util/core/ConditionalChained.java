package com.mycheque.util.core;

/**
 * Extension of the {@link Chained} interface, which is meant
 * to be executed within an <i>execution chain</i> if and only if a
 * passed object {@linkplain #matches(Object) matches} this chain element.
 *
 * @param <T> the type of the objects passed into an <i>execution chain</i> methods.
 * @author resxnvnce
 */
public interface ConditionalChained<T> extends Chained {

    /**
     * Is this chain element applicable for the given object?
     *
     * @param t the object to be passed into an <i>execution chain</i> method.
     * @return {@code true} if this {@code Chained} is applicable for the argument passed,
     *         {@code false} otherwise.
     */
    boolean matches(T t);
}
