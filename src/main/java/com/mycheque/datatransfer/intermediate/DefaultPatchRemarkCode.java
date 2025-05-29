package com.mycheque.datatransfer.intermediate;

/**
 * Default implementation of {@link PatchRemarkCode}.
 *
 * @author resxnvnce
 */
record DefaultPatchRemarkCode(int value) implements PatchRemarkCode {

    @Override
    public boolean is1xxWarning() {
        return this.value / 100 == 1;
    }

    @Override
    public boolean is6xxFailure() {
        return this.value / 100 == 6;
    }
}
