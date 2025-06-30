package com.mycheque.service.observer.support;

import org.springframework.lang.Nullable;
import org.springframework.data.jpa.domain.Specification;

import com.mycheque.datatransfer.query.ReceiptQuery;

import com.mycheque.domain.Receipt;
import com.mycheque.domain.specs.ReceiptSpecifications;

import com.mycheque.service.observer.SpecificationObserver;

/**
 * The list of {@link ReceiptQuery} property observers.
 * <p>
 * Retrievable via the {@linkplain #VALUES constant}.
 *
 * @author resxnvnce
 */
public enum ReceiptQueryObserver implements SpecificationObserver<ReceiptQuery, Receipt> {

    /**
     * A {@link SpecificationObserver}, looking for the properties accessing
     * the {@linkplain Receipt#getFoundation() receipt foundation}.
     */
    FOUNDATION {
        @Override
        public @Nullable Specification<Receipt> deriveIfNecessary(ReceiptQuery query) {
            return ReceiptSpecifications.byFoundation(
                    query.foundationContains()
            );
        }
    },

    /**
     * A {@link SpecificationObserver}, looking for the properties accessing
     * the {@linkplain Receipt#getTotal() receipt total}.
     */
    TOTAL {
        @Override
        public @Nullable Specification<Receipt> deriveIfNecessary(ReceiptQuery query) {
            return ReceiptSpecifications.byTotalBetween(
                    query.minTotal(),
                    query.maxTotal()
            );
        }
    },

    /**
     * A {@link SpecificationObserver}, looking for the properties accessing
     * the {@linkplain Receipt#getTimestamp() receipt timestamp}.
     */
    TIMESTAMP {
        @Override
        public @Nullable Specification<Receipt> deriveIfNecessary(ReceiptQuery query) {
            return ReceiptSpecifications.byTimestampBetween(
                    query.minTimestamp(),
                    query.maxTimestamp()
            );
        }
    };

    /**
     * The {@code ReceiptQuery} specification observers in the order they are declared.
     */
    public static final ReceiptQueryObserver[] VALUES = values();
}
