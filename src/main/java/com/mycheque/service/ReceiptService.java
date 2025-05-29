package com.mycheque.service;

import com.mycheque.service.wrapper.PatchnotesWrapper;
import com.mycheque.service.wrapper.PatchnotesOutcome;

/**
 * The service interface for {@link com.mycheque.domain.Receipt Receipt} entities.
 *
 * @author resxnvnce
 */
public interface ReceiptService {

    /**
     * Save all the receipts in a patchnotes wrapper supplied.<p>
     * It is <i>guaranteed</i> not a single receipt would be saved to the persistence store if there's at least
     * one {@linkplain com.mycheque.datatransfer.accept.Patchnotes#patches() patch} that results in a
     * {@code 6xx Failure} {@linkplain com.mycheque.datatransfer.intermediate.Remarkable remarkable}.</p>
     * <p>This method generally tries to apply all the patches, forming a full outcome.</p>
     *
     * @param wrapper a wrapper, containing the patchnotes and the {@code Customer} requested it.
     * @return the outcome of the attempt to apply patches.
     * @throws ReceiptServiceException in case of an internal error.
     */
    PatchnotesOutcome saveAllReceipts(PatchnotesWrapper wrapper) throws ReceiptServiceException;
}
