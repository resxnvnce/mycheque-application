package com.mycheque.service;

import java.util.Optional;

import com.mycheque.domain.Customer;

import com.mycheque.datatransfer.profile.Credentials;
import com.mycheque.datatransfer.profile.CredentialsUpdate;

import com.mycheque.service.wrapper.AuthorizedWrapper;

/**
 * The service interface for {@link Customer} entities.
 *
 * @author resxnvnce
 */
public interface CustomerService {

    /**
     * Removes the {@code Customer} with the given {@linkplain Customer#getId() identifier}.
     * <p>
     * If the customer is not found in the persistence store it is silently ignored.
     *
     * @param id the identifier of a customer to be removed from the persistence store.
     */
    void deleteById(long id);

    /**
     * Returns whether a {@code Customer} with the given {@linkplain Customer#getUsername() username} exists.
     *
     * @param username the username to find a customer by.
     * @return {@code true} if a {@code Customer} with the given username exists;
     *         {@code false} otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Retrieves a {@code Customer} by its {@linkplain Customer#getUsername() username}.
     *
     * @param username the username to find a customer by.
     * @return the {@code Customer} with the given username or {@link Optional#empty()}
     *         if none found.
     */
    Optional<Customer> findByUsername(String username);

    /**
     * Register a new customer using the {@link Credentials} specified and retrieve the entity created.
     * <p>
     * <b>NOTE:</b> This method does not perform any validation.
     *
     * @param credentials a new customer credentials.
     * @return the {@link Customer} entity saved, never {@code null}.
     * @throws CustomerServiceException if the {@linkplain Credentials#token() token}
     *         provided is already being used by another customer.
     */
    Customer register(Credentials credentials) throws CustomerServiceException;

    /**
     * Update the credentials of the given {@code Customer}.
     * Properties set to {@code null} are silently ignored, i.e. <i>not being updated at all</i>.
     * <p>
     * <b>NOTE:</b> This method does not perform any validation.
     *
     * @param wrapper an {@code AuthorizedWrapper} with the updates to apply.
     * @return the {@link Customer} entity updated, never {@code null}.
     * @throws CustomerServiceException if the {@linkplain Credentials#token() token}
     *         provided is already being used by another customer.
     */
    Customer applyUpdates(AuthorizedWrapper<CredentialsUpdate> wrapper) throws CustomerServiceException;
}
