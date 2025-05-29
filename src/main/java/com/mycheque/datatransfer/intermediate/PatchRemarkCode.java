package com.mycheque.datatransfer.intermediate;

import com.mycheque.util.Assert;

/**
 * Represents a {@link com.mycheque.domain.Customer Customer}'s
 * single patch remark code. Implemented by {@link PatchRemark}, but
 * defined as an interface to allow for values not in that enumeration.
 *
 * @author resxnvnce
 */
public sealed interface PatchRemarkCode permits DefaultPatchRemarkCode, PatchRemark {

    /**
     * Returns the integer value of this remark code.
     *
     * @return the corresponding integer value.
     */
    int value();

    /**
     * Indicates whether this patch remark is in the Warning class ({@code 1xx}).
     *
     * @return {@code true} if this remark is a warning remark,
     *         {@code false} otherwise.
     */
    boolean is1xxWarning();

    /**
     * Indicates whether this patch remark is in the Failure class ({@code 6xx}).
     *
     * @return {@code true} if this remark is a failure remark,
     *         {@code false} otherwise.
     */
    boolean is6xxFailure();

    /**
     * Indicates whether this {@code PatchRemarkCode} shares
     * the same {@linkplain #value() value} as the other remark code.
     *
     * @param other the other {@code PatchRemarkCode} to compare.
     * @return {@code true} if the two remark codes share the same integer value,
     *         {@code false} otherwise.
     */
    default boolean isSameCodeAs(PatchRemarkCode other) {
        return value() == other.value();
    }

    /**
     * Returns a {@code PatchRemarkCode} for the given integer value.
     *
     * @param code the remark code as an integer.
     * @return the corresponding {@code PatchRemarkCode}.
     * @throws IllegalArgumentException if {@code code} is not a three-digit
     *         positive number.
     */
    static PatchRemarkCode valueOf(int code) {
        Assert.args(code >= 100 && code <= 999, () ->
                "Patch remark code should be a three-digit positive integer. Given: '" + code + "'.");
        final var remark = PatchRemark.resolve(code);
        return remark != null ? remark : new DefaultPatchRemarkCode(code);
    }
}
