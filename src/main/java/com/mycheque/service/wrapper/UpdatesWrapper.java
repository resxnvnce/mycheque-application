package com.mycheque.service.wrapper;

import com.mycheque.util.Assert;

import com.mycheque.domain.Customer;
import com.mycheque.datatransfer.accept.CredentialsUpdate;

/**
 * Wraps a {@link Customer} respectively to the {@link CredentialsUpdate} requested.
 * Retrieve the updates by calling {@link #unwrap()}.
 *
 * @param customer the updates target. Must have an {@linkplain Customer#getId() identifier}.
 * @param unwrap   the updates.
 * @author resxnvnce
 */
public record UpdatesWrapper(Customer customer, CredentialsUpdate unwrap) {

    /**
     * Compact constructor, ensuring the {@code Customer} provided is updatable.
     *
     * @param customer the updates target.
     * @param unwrap   the updates.
     */
    public UpdatesWrapper {
        Assert.state(customer.getId() != null, () -> "customer to be updated must have a non-null id");
    }
}
