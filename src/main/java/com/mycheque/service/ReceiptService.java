package com.mycheque.service;

import java.util.List;

import com.mycheque.service.wrapper.AuthorizedWrapper;
import com.mycheque.service.wrapper.PatchnotesOutcome;

import com.mycheque.datatransfer.query.Patchnotes;
import com.mycheque.datatransfer.query.ReceiptQuery;
import com.mycheque.datatransfer.result.RequestedReceipt;

/**
 * The service interface for {@link com.mycheque.domain.Receipt Receipt} entities.
 *
 * @author resxnvnce
 */
public interface ReceiptService {

    /**
     * Find all the receipts satisfying the given {@link ReceiptQuery},
     * associated exactly with the customer provided.
     *
     * @param wrapper an {@code AuthorizedWrapper} with the query to execute.
     * @return a {@code List} of {@linkplain RequestedReceipt records} found.
     */
    List<RequestedReceipt> findAll(AuthorizedWrapper<ReceiptQuery> wrapper);

    /**
     * Save all the receipts in a wrapper supplied.
     * <p>
     * It is <i>guaranteed</i> not a single receipt would be saved to the persistence store
     * if there's at least one patch that results in a {@code 6xx Failure} remarkable.
     * <p>
     * This method generally tries to apply each one of the patches, forming a full outcome.
     *
     * @param wrapper an {@code AuthorizedWrapper} with the patchnotes to apply.
     * @return the outcome of the attempt to save receipts.
     * @throws ReceiptServiceException in case of an internal error.
     */
    PatchnotesOutcome saveAll(AuthorizedWrapper<Patchnotes> wrapper) throws ReceiptServiceException;
}
