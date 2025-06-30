package com.mycheque.datatransfer.result;

import java.util.Set;

import java.time.LocalDateTime;

import com.mycheque.util.Assert;
import com.mycheque.util.Lambdas;

import com.mycheque.domain.Item;
import com.mycheque.domain.Receipt;
import com.mycheque.domain.Purchase;
import com.mycheque.domain.id.FiscalDataRecord;

/**
 * A data transfer object record representing a {@link Receipt} returned upon a retrieval request.
 *
 * @param fn         the fiscal drive number.
 * @param fp         the fiscal document number.
 * @param fd         the fiscal sign number.
 * @param total      the purchase income, <b>in kopecks</b>.
 * @param foundation the retail outlets network.
 * @param timestamp  the purchase transaction timestamp, no time-zone.
 * @param items      the {@link Item}s acquired.
 * @author resxnvnce
 */
public record RequestedReceipt(
        String fn, String fp, String fd, Integer total, String foundation, LocalDateTime timestamp, Set<Item> items) {

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
        final var items = Lambdas.applyOrNull(entity.getPurchase(), Purchase::items);

        return new RequestedReceipt(id.fn(), id.fp(), id.fd(),
                entity.getTotal(), entity.getFoundation(), entity.getTimestamp(), items);
    }

    /**
     * Returns the {@linkplain FiscalDataRecord identifier} of this {@code RequestedReceipt}.
     *
     * @return the fiscal data record identifier.
     */
    public FiscalDataRecord id() {
        return new FiscalDataRecord(this.fn, this.fp, this.fd);
    }
}
