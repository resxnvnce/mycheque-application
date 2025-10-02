package com.mycheque.datatransfer.result;

import java.util.Set;
import java.util.stream.Collectors;

import com.mycheque.util.Assert;

import com.mycheque.domain.Item;
import com.mycheque.domain.Purchase;

/**
 * A data transfer object record representing an {@link Item} returned upon a retrieval request.
 *
 * @param name  the source item {@linkplain Item#name() name}.
 * @param price the source item {@linkplain Item#price() price}.
 * @param count the source item {@linkplain Item#count() count}.
 * @author resxnvnce
 */
public record RequestedItem(String name, Integer price, Integer count) {

    /**
     * Construct a {@code RequestedItem} from a document.
     *
     * @param document the item document.
     *                 Must not be {@code null}.
     * @return a requested item record.
     */
    public static RequestedItem mappedFrom(Item document) {
        Assert.notNull(document, "document must not be null");

        final var price = document.price();
        final var count = document.count();

        return new RequestedItem(document.name(), price, count);
    }

    /**
     * Construct a set of {@code RequestedItem}s from a document.
     *
     * @param document the purchase document.
     *                 Must not be {@code null}.
     * @return a {@link Set} of requested items, which might be empty.
     */
    public static Set<RequestedItem> mappedFrom(Purchase document) {
        Assert.notNull(document, "document must not be null");

        return document.items().stream().map(RequestedItem::mappedFrom)
                .collect(
                        Collectors.toUnmodifiableSet()
                );
    }
}