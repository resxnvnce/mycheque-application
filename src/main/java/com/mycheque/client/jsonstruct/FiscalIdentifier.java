package com.mycheque.client.jsonstruct;

/**
 * The identifier of a target entity, consisting of the <strong>fiscal data</strong>
 * fields set: the {@link #drive()}, the {@link #document()} and the {@link #sign()}.
 *
 * @author resxnvnce
 */
public interface FiscalIdentifier {

    /**
     * Returns the fiscal drive number, <strong>fn</strong>.
     *
     * @return the {@code FiscalIdentifier} drive number.
     */
    String drive();

    /**
     * Returns the fiscal document number, <strong>fd</strong>.
     *
     * @return the {@code FiscalIdentifier} document number.
     */
    String document();

    /**
     * Returns the fiscal sign, <strong>fp</strong>.
     *
     * @return the {@code FiscalIdentifier} sign.
     */
    String sign();
}
