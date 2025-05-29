package com.mycheque.repository;

import java.util.Optional;

import com.mycheque.domain.Customer;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The repository interface for {@link Customer} entities.
 *
 * @author resxnvnce
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * The <b>JPQL</b> query template to use within the {@link #forceUpdate(Customer)} method.
     */
    String FORCE_UPDATE_JPQL = "update Customer c set c.thirdpartyToken = :#{#updated.thirdpartyToken},"
            + " c.username = :#{#updated.username}, c.password = :#{#updated.password},"
            + " c.role = :#{#updated.role} where c.id = :#{#updated.id}";

    /**
     * Retrieves a {@code Customer} by its {@linkplain Customer#getUsername() username}.
     *
     * @param username the username to find a customer by.
     * @return the {@code Customer} with the given username or {@link Optional#empty()}
     *         if none found.
     */
    Optional<Customer> findByUsername(String username);

    /**
     * Returns whether a {@code Customer} with the given
     * {@linkplain Customer#getUsername() username} exists.
     *
     * @param username the username to find a customer by.
     * @return {@code true} if a {@code Customer} with the given username exists;
     *         {@code false} otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Forces the repository to update the given {@link Customer} with no
     * need to look up for its presence in the persistence context,
     * avoiding an extra <i>SQL SELECT</i> query execution.
     * <p>
     * Automatically clears the persistence context afterward.
     *
     * @param updated a {@code Customer} whose state has to be synchronized with the persistence store.
     */
    @Query(FORCE_UPDATE_JPQL)
    @Modifying(clearAutomatically = true)
    void forceUpdate(Customer updated);
}
