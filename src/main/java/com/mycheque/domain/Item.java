package com.mycheque.domain;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * {@link Purchase} document component, representing a single item from a receipt.
 *
 * @param name  the item name.
 * @param price the item price per quantity measure unit, <strong>in kopecks</strong>.
 * @param total the item total cost. That is, the item quantity multiplied by its price.
 * @author resxnvnce
 */
public record Item(@Field("name") String name, @Field("price") Integer price, @Field("total") Integer total) {
}
