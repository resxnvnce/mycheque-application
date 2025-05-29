package com.mycheque.service.wrapper;

import com.mycheque.util.Assert;

import com.mycheque.domain.Customer;
import com.mycheque.datatransfer.accept.Patchnotes;

/**
 * Wraps a {@link Customer} respectively to the {@link Patchnotes} requested.
 * Retrieve the patchnotes by calling {@link #unwrap()}.
 *
 * @param customer the patchnotes target. Must have an {@linkplain Customer#getId() identifier}.
 * @param unwrap   the patchnotes.
 * @author resxnvnce
 */
public record PatchnotesWrapper(Customer customer, Patchnotes unwrap) {

    /**
     * Compact constructor, ensuring the {@code Customer} provided is updatable.
     *
     * @param customer the patchnotes target.
     * @param unwrap   the patchnotes.
     */
    public PatchnotesWrapper {
        Assert.state(customer.getId() != null, () -> "customer to be patched must have a non-null id");
    }
}
