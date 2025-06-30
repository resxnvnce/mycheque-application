package com.mycheque.service.observer.support;

import org.springframework.lang.Nullable;
import org.springframework.data.mongodb.core.query.Criteria;

import com.mycheque.datatransfer.query.ItemQuery;

import com.mycheque.service.observer.CriteriaObserver;

/**
 * The list of {@link ItemQuery} property observers.
 * <p>
 * Retrievable via the {@linkplain #VALUES constant}.
 *
 * @author resxnvnce
 */
public enum ItemQueryObserver implements CriteriaObserver<ItemQuery> {

    /**
     * A {@link CriteriaObserver}, looking for the properties accessing
     * the {@linkplain com.mycheque.domain.Item#name() item name}.
     */
    NAME {
        @Override
        public @Nullable Criteria deriveIfNecessary(ItemQuery query) {
            if (query.nameContains() == null) {
                return null;
            }

            return Criteria.where("name").regex(".*" + query.nameContains() + ".*", "i");
        }
    },

    /**
     * A {@link CriteriaObserver}, looking for the properties accessing
     * the {@linkplain com.mycheque.domain.Item#price() item price}.
     */
    PRICE {
        @Override
        public @Nullable Criteria deriveIfNecessary(ItemQuery query) {
            return CriteriaObserver.between(query.minPrice(), query.maxPrice(), "price");
        }
    },

    /**
     * A {@link CriteriaObserver}, looking for the properties accessing
     * the {@linkplain com.mycheque.domain.Item#count() item count}.
     */
    COUNT {
        @Override
        public @Nullable Criteria deriveIfNecessary(ItemQuery query) {
            return CriteriaObserver.between(query.minCount(), query.maxCount(), "count");
        }
    };

    /**
     * The {@code ItemQuery} criteria observers in the order they are declared.
     */
    public static final ItemQueryObserver[] VALUES = values();
}
