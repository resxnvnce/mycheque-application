package com.mycheque.repository;

import com.mycheque.domain.Purchase;
import com.mycheque.domain.id.FiscalDataRecord;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * The repository interface for {@link Purchase} entities collection.
 *
 * @author resxnvnce
 */
@Repository
public interface PurchaseRepository extends MongoRepository<Purchase, FiscalDataRecord> {
}
