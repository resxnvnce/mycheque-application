package com.mycheque.client.jsonstruct;

/**
 * The identifier of a target entity,
 * consisting of the <strong>fiscal data</strong>
 * fields set: {@link #fn()}, {@link #fd()} and {@link #fp()}.
 *
 * @author resxnvnce
 */
public interface FiscalIdentifier {

    /**
     * Returns the fiscal drive number.
     *
     * @return the {@code FiscalIdentifier} drive number.
     */
    String fn();

    /**
     * Returns the fiscal document number.
     *
     * @return the {@code FiscalIdentifier} document number.
     */
    String fd();

    /**
     * Returns the fiscal sign.
     *
     * @return the {@code FiscalIdentifier} sign.
     */
    String fp();
}
