package com.mycheque.service;

import java.util.List;
import java.util.Collection;

import com.mycheque.lang.Nullable;

import com.mycheque.domain.Purchase;
import com.mycheque.domain.id.FiscalDataRecord;

import com.mycheque.datatransfer.query.PurchaseQuery;

/**
 * The service interface for {@link Purchase} collection.
 *
 * @author resxnvnce
 */
public interface PurchaseService {

    /**
     * Returns all {@link Purchase}s such that each of them:
     * <ul>
     *     <li>has an {@linkplain Purchase#id() identifier} strictly inside the given pool;</li>
     *     <li>satisfies the {@code PurchaseQuery} specified, if present.</li>
     * </ul>
     * <p>A {@code List} returned might be blank.</p>
     *
     * @param pool the pool. Must not be blank.
     * @param query a query.
     * @return a list of {@code Purchase} records found.
     */
    List<Purchase> findAll(Collection<FiscalDataRecord> pool, @Nullable PurchaseQuery query);
}
