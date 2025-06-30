package com.mycheque.service.wrapper;

import com.mycheque.util.Assert;
import com.mycheque.domain.Customer;

/**
 * Wraps a {@link Customer} entity respectively to some object.
 *
 * @param <T>      the type of wrapped object.
 * @param customer an entity.
 * @param object   an object.
 * @author resxnvnce
 */
public record AuthorizedWrapper<T>(Customer customer, T object) {

    /**
     * Constructs an {@code AuthorizedWrapper}.
     * <p>
     * Both {@code customer} and {@code customer.getId()} must be present.
     *
     * @param customer the entity.
     * @param object   the object.
     */
    public AuthorizedWrapper {
        Assert.notNull(customer, () -> "customer must not be null");
        Assert.notNull(customer.getId(), () -> "customer.id must not be null");
    }
}
