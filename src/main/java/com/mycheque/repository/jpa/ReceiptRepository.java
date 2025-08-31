package com.mycheque.repository.jpa;

import java.util.Optional;

import com.mycheque.domain.Receipt;
import com.mycheque.domain.id.FiscalDataRecord;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The repository interface for {@link Receipt} entities.
 *
 * @author resxnvnce
 */
@Repository
public interface ReceiptRepository extends JpaSpecificationExecutor<Receipt>, JpaRepository<Receipt, FiscalDataRecord> {

    /**
     * Retrieves an {@code Optional} describing the identifier of the {@code Customer},
     * possibly owning a {@link Receipt} with the given {@code id}.
     *
     * @param id the identifier of a {@code Receipt} which might be already persistent.
     * @return the owner's identifier or {@link Optional#empty()}
     *         if such a {@code Receipt} is indeed a new one.
     */
    @Query("select r.customer.id from Receipt r where r.id = :id")
    Optional<Long> toOwnerReference(FiscalDataRecord id);
}
