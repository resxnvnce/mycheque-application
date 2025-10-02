package com.mycheque.datatransfer.result;

import java.util.Set;

import java.time.LocalDateTime;

import com.mycheque.util.Assert;
import com.mycheque.util.Lambdas;

import com.mycheque.domain.Receipt;
import com.mycheque.domain.id.FiscalDataRecord;

/**
 * A data transfer object record representing a {@link Receipt} returned upon a retrieval request.
 *
 * @param fn         the fiscal drive number.
 * @param fd         the fiscal document number.
 * @param fp         the fiscal sign number.
 * @param items      {@linkplain RequestedItem items} acquired.
 * @param total      {@linkplain Receipt#getTotal() total} spent, <b>in kopecks</b>.
 * @param timestamp  {@linkplain Receipt#getTimestamp() date-time} of the transaction.
 * @param foundation the {@linkplain Receipt#getFoundation() retail network}.
 * @author resxnvnce
 */
public record RequestedReceipt(
        String fn, String fd, String fp,
        Set<RequestedItem> items, Integer total, LocalDateTime timestamp, String foundation) {

    /**
     * Construct a {@code RequestedReceipt} from an entity.
     *
     * @param entity the receipt entity.
     *               Must have an {@linkplain Receipt#getId() identifier}.
     * @return a requested receipt record.
     */
    public static RequestedReceipt mappedFrom(Receipt entity) {
        Assert.notNull(entity, "entity must not be null");
        Assert.notNull(entity.getId(), "entity.id must not be null");

        final var id = entity.getId();
        final var requestedItems = Lambdas.applyOrNull(entity.getPurchase(), RequestedItem::mappedFrom);

        return new RequestedReceipt(
                id.fn(), id.fd(), id.fp(),
                requestedItems, entity.getTotal(), entity.getTimestamp(), entity.getFoundation()
        );
    }

    /**
     * Returns the {@linkplain FiscalDataRecord identifier} of this {@code RequestedReceipt}.
     *
     * @return the fiscal data record identifier.
     */
    public FiscalDataRecord id() {
        return new FiscalDataRecord(this.fn, this.fd, this.fp);
    }
}
