package com.mycheque.domain.specs;

import java.time.LocalDateTime;

import jakarta.persistence.criteria.Path;

import com.mycheque.lang.Nullable;
import com.mycheque.domain.Receipt;
import com.mycheque.domain.Customer;
import com.mycheque.domain.id.FiscalDataRecord;

import org.springframework.data.jpa.domain.Specification;

/**
 * Helper class resolving the {@link Specification}s for a
 * partial search query involving the {@link Receipt} entities.
 *
 * @author resxnvnce
 */
public final class ReceiptSpecifications {

    /**
     * Returns a specification over an entity {@linkplain Receipt#getId() id}.
     *
     * @param id the {@link FiscalDataRecord} to involve into a specification.
     * @return a {@code Specification} involving the given identifier
     *         or {@code null} if it's not present.
     */
    @Nullable
    public static Specification<Receipt> byId(@Nullable FiscalDataRecord id) {
        return id != null ? id.toReceiptSpecification() : null;
    }

    /**
     * Returns a specification over an entity {@linkplain Receipt#getCustomer() customer}.
     *
     * @param customer the {@link Customer} to involve into a specification.
     * @return a {@code Specification} involving the given customer
     *         or {@code null} if it's not present.
     */
    @Nullable
    public static Specification<Receipt> byCustomer(@Nullable Customer customer) {
        return customer != null ? customer.toReceiptSpecification() : null;
    }

    /**
     * Returns a specification over an entity {@linkplain Receipt#getFoundation() foundation},
     * with the case-insensitive {@code LIKE %foundation%} pattern.
     *
     * @param foundation the foundation to involve into a specification.
     * @return a {@code Specification} involving the given foundation
     *         or {@code null} if it's not present.
     */
    @Nullable
    public static Specification<Receipt> byFoundation(@Nullable String foundation) {
        if (foundation == null) {
            return null;
        }

        return (root, query, cb) -> {
            Path<String> path = root.get("foundation");

            return cb.like(cb.lower(path), '%' + foundation.toLowerCase() + '%');
        };
    }

    /**
     * Returns a specification over an entity {@linkplain Receipt#getTotal() total},
     * with the lower bound at {@code min} and the upper one at {@code max}; both inclusive.
     *
     * @param min the lower bound for total. Can be {@code null}.
     * @param max the upper bound for total. Can be {@code null}.
     * @return a {@code Specification} involving the given bounds (or one of them)
     *         or {@code null} if none present.
     */
    @Nullable
    public static Specification<Receipt> byTotalBetween(@Nullable Integer min, @Nullable Integer max) {
        if (min != null && max != null) {
            return (root, query, cb) -> cb.between(root.get("total"), min, max);
        }

        if (max != null) {
            return (root, query, cb) -> cb.le(root.get("total"), max);
        }

        if (min != null) {
            return (root, query, cb) -> cb.ge(root.get("total"), min);
        }

        return null;
    }

    /**
     * Returns a specification over an entity {@linkplain Receipt#getTimestamp() timestamp},
     * with the lower bound at {@code min} and the upper one at {@code max}; both inclusive.
     *
     * @param min the lower bound for timestamp. Can be {@code null}.
     * @param max the upper bound for timestamp. Can be {@code null}.
     * @return a {@code Specification} involving the given bounds (or one of them)
     *         or {@code null} if none present.
     */
    @Nullable
    public static Specification<Receipt> byTimestampBetween(@Nullable LocalDateTime min, @Nullable LocalDateTime max) {
        if (min != null && max != null) {
            return (root, query, cb) -> cb.between(root.get("timestamp"), min, max);
        }

        if (max != null) {
            return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("timestamp"), max);
        }

        if (min != null) {
            return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("timestamp"), min);
        }

        return null;
    }

    /**
     * Nah-uh.
     */
    private ReceiptSpecifications() {
        throw new AssertionError("Utility class instantiation is not allowed!");
    }
}
