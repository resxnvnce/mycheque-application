package com.mycheque.datatransfer.intermediate;

import java.util.Map;

import org.springframework.lang.Nullable;

import static com.mycheque.util.Maps.mapToIdentity;

/**
 * Enumeration of {@link com.mycheque.domain.Customer Customer}'s single patch remarks.
 * <p>
 * The remark series can be retrieved via {@link #series()}.
 *
 * @author resxnvnce
 */
public enum PatchRemark implements PatchRemarkCode {

    /* 1xx Warning */

    /**
     * {@code 100 Already Present}.
     */
    ALREADY_PRESENT(100, Series.WARNING),

    /**
     * {@code 120 Duplicated}.
     */
    DUPLICATED(120, Series.WARNING),

    /* 6xx Failure */

    /**
     * {@code 600 Occupied}.
     */
    OCCUPIED(600, Series.FAILURE),

    /**
     * {@code 660 Unrecognizable}.
     */
    UNRECOGNIZABLE(660, Series.FAILURE),

    /**
     * {@code 661 Timeout}.
     */
    TIMEOUT(661, Series.FAILURE);

    /**
     * Enumeration of patch remark series.
     * <p>
     * Retrievable via {@link PatchRemark#series()}.
     */
    public enum Series {

        /**
         * {@code 1xx}-Warning series.
         */
        WARNING,

        /**
         * {@code 6xx}-Failure series.
         */
        FAILURE;
    }

    /**
     * The {@linkplain #value() value}-to-{@code PatchRemark} mapping.
     */
    private static final Map<Integer, PatchRemark> RESOLVER = mapToIdentity(PatchRemark.class, PatchRemark::value);

    /**
     * The integer value of this patch remark.
     */
    private final int value;

    /**
     * The remark series.
     */
    private final Series series;

    @Override
    public boolean is1xxWarning() {
        return series() == Series.WARNING;
    }

    @Override
    public boolean is6xxFailure() {
        return series() == Series.FAILURE;
    }

    @Override
    public int value() {
        return value;
    }

    /**
     * Returns the series of this {@code PatchRemark}.
     *
     * @return the patch remark series.
     * @see PatchRemark.Series
     */
    public Series series() {
        return series;
    }

    /**
     * Resolve the given remark code to a {@code PatchRemark}, if possible.
     *
     * @param code the patch remark code (potentially non-standard).
     * @return the corresponding {@code PatchRemark}, or {@code null} if not found.
     */
    @Nullable
    public static PatchRemark resolve(int code) {
        return RESOLVER.get(code);
    }

    /**
     * Constructs a new patch remark that must have a unique code to be properly mapped.
     *
     * @param value  the corresponding unique code for this instance.
     * @param series the patch remark series.
     */
    PatchRemark(int value, Series series) {
        this.value = value;
        this.series = series;
    }
}
