package com.mycheque.util;

import java.util.Map;
import java.util.Arrays;

import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.function.Function;

import org.springframework.lang.Nullable;

/**
 * Miscellaneous {@link Map} utility methods.
 *
 * @author resxnvnce
 */
public final class Maps {

    /**
     * Returns {@code true} if the given {@link Map} is not {@code null}
     * and contains a mapping for each of the specified keys.
     *
     * @param map  a {@code Map} to be asked if it's not {@code null}
     *             and does contain a mapping for all the given keys.
     * @param keys keys whose presence in {@code map} to be tested.
     * @return {@code true} if the map is not {@code null}
     *         and indeed contains a mapping for all the keys.
     * @throws ClassCastException if some of the specified
     *         {@code keys} is of an inappropriate type for {@code map}.
     * @throws NullPointerException if some of the specified
     *         {@code keys} is {@code null} and {@code map}
     *         does not permit {@code null} keys.
     * @see Map#containsKey(Object)
     */
    public static boolean containsKeys(@Nullable Map<?, ?> map, Object... keys) {
        return map != null && Arrays.stream(keys).allMatch(map::containsKey);
    }

    /**
     * Accumulates the given values into a {@link Map} whose values
     * are the {@code values} themselves and keys are the result of
     * applying the provided mapping function to the values.
     * <p>
     * If the mapped keys contain duplicates,
     * according to {@link Object#equals(Object)},
     * an {@code IllegalStateException} is thrown.
     *
     * @param values    a source of values.
     * @param keyMapper a mapping function to produce keys.
     * @return a {@link Map} whose keys are the result of applying
     *         mapping function to the given values and values are
     *         in the input source.
     * @throws IllegalStateException if the keys, produced by
     *         the mapping function, contain duplicates.
     * @see Collectors#toMap(Function, Function)
     */
    public static <K, V> Map<K, V> mapToIdentity(Stream<V> values, Function<? super V, K> keyMapper) {
        return values.collect(
                Collectors.toMap(keyMapper, t -> t)
        );
    }

    /**
     * Accumulates the values of the given {@link Enum} type into
     * a {@link Map} whose values are the enum elements and keys are
     * the result of applying the provided mapping function to the values.
     * <p>
     * If the mapped keys contain duplicates,
     * according to {@link Object#equals(Object)},
     * an {@code IllegalStateException} is thrown.
     *
     * @param enumType  the type of the source {@code Enum}.
     * @param keyMapper a mapping function to produce keys.
     * @return a {@link Map} whose keys are the result of applying
     *         mapping function to the given values and values are
     *         the given enum type elements.
     * @throws IllegalStateException if the keys, produced by
     *         the mapping function, contain duplicates.
     * @see Collectors#toMap(Function, Function)
     */
    public static <V extends Enum<V>, K> Map<K, V> mapToIdentity(Class<V> enumType, Function<? super V, K> keyMapper) {
        final V[] values = enumType.getEnumConstants();
        return Maps.mapToIdentity(Arrays.stream(values), keyMapper);
    }

    /**
     * Nah-uh.
     */
    private Maps() {
        throw new AssertionError("Utility class instantiation is not allowed!");
    }
}
