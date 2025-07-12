package com.mycheque.domain;

import java.util.StringJoiner;
import java.time.LocalDateTime;

import com.mycheque.lang.Nullable;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.AttributeOverride;

import com.mycheque.mapping.support.Default;

import com.mycheque.domain.id.FiscalDataRecord;

import com.mycheque.util.hibernate6.HibernateProxiesAware;

import org.springframework.data.domain.Persistable;

/**
 * An entity representing a receipt, either a paper one or an electronic one.
 * Each one has the unique compound {@linkplain #getId() id}
 * and the {@linkplain #getCustomer() customer} who owns it.
 * <p><b>NOTE</b>: most of the {@code Receipt} attributes are considered immutable.</p>
 *
 * @author resxnvnce
 */
@Table(name = "receipt", schema = "mycheque")
public @Entity class Receipt implements HibernateProxiesAware, Persistable<FiscalDataRecord> {

    /**
     * The receipt immutable compound identifier, acting as a 3-element set:
     * the fiscal drive number "fn", the fiscal document number "fd" and the fiscal sign "fp".
     */
    @EmbeddedId
    @AttributeOverride(
            name = "fn", column = @Column(name = "fn", nullable = false, updatable = false, length = 31)
    )
    @AttributeOverride(
            name = "fd", column = @Column(name = "fd", nullable = false, updatable = false, length = 31)
    )
    @AttributeOverride(
            name = "fp", column = @Column(name = "fp", nullable = false, updatable = false, length = 31)
    )
    private FiscalDataRecord id;

    /**
     * The total purchase income, <strong>in kopecks</strong>.
     */
    @Column(name = "total", nullable = false, updatable = false)
    private Integer total;

    /**
     * The network of retail outlets. In other words, the
     * foundation owning the place where the purchase was made.
     */
    @Column(name = "foundation", nullable = false, updatable = false)
    private String foundation;

    /**
     * The purchase transaction timestamp without a time-zone.
     */
    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;

    /**
     * The customer or the owner of this receipt. The {@code Customer} might
     * be changed over time, but this isn't expected to happen too often.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private @Nullable Customer customer;

    /**
     * The purchase transient object itself.
     */
    @Transient
    private @Nullable Purchase purchase;

    /**
     * The transient flag indicating whether this {@code Receipt}
     * should be treated as a new one at this moment.
     */
    @Transient
    private boolean isNew = false;

    /* Constructors */

    /**
     * The protected constructor only to be invoked by
     * the persistence provider or by Spring framework.
     */
    protected Receipt() {
    }

    /**
     * Constructs a {@link Receipt} entity. Each of the properties must not be {@code null}.
     *
     * @param id         the fiscal identifier.
     * @param total      the total money spent.
     * @param foundation the foundation.
     * @param timestamp  the transaction timestamp.
     */
    @Default // the primary constructor for MapStruct to use
    public Receipt(FiscalDataRecord id, Integer total, String foundation, LocalDateTime timestamp) {
        this.id = id;
        this.total = total;
        this.foundation = foundation;
        this.timestamp = timestamp;
    }

    /* Getters */

    /**
     * Returns the compound identifier of this {@code Receipt}.
     *
     * @return the identifier, never {@code null}.
     */
    @Override
    public FiscalDataRecord getId() {
        return this.id;
    }

    /**
     * Returns the total income, <strong>in kopecks</strong>.
     *
     * @return this {@code Receipt} income.
     */
    public Integer getTotal() {
        return this.total;
    }

    /**
     * Returns this {@code Receipt} foundation.
     *
     * @return the foundation.
     */
    public String getFoundation() {
        return this.foundation;
    }

    /**
     * Returns the purchase transaction date-time.
     *
     * @return the date-time this {@code Receipt} appeared.
     */
    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    /**
     * Returns the {@code Customer}, i.e. the owner of this receipt.
     *
     * @return the owner of this {@code Receipt}.
     */
    @Nullable
    public Customer getCustomer() {
        return this.customer;
    }

    /**
     * Returns the purchase details of this receipt.
     *
     * @return the purchase details.
     */
    @Nullable
    public Purchase getPurchase() {
        return this.purchase;
    }

    /**
     * Returns {@code true} if this receipt should be treated as a new one.
     *
     * @return {@code true} if this receipt is considered a new one at this moment,
     *         {@code false} otherwise.
     */
    @Override
    @Transient
    public boolean isNew() {
        return this.isNew;
    }

    /* Setters */

    /**
     * Set the {@code Customer} owning this receipt.
     *
     * @param customer the receipt owner.
     */
    public void setCustomer(@Nullable Customer customer) {
        this.customer = customer;
    }

    /**
     * Set the purchase details of this receipt.
     *
     * @param purchase the purchase details record.
     */
    public void setPurchase(@Nullable Purchase purchase) {
        this.purchase = purchase;
    }

    /**
     * Set the transient boolean flag indicating whether
     * this {@code Receipt} should be treated as a new one.
     *
     * @param isNew is this receipt considered a new one?
     */
    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    /* java.lang.Object */

    /**
     * Proxy-aware {@code #hashCode()} method.
     *
     * @see HibernateProxiesAware#proxySafeHashCode()
     */
    @Override
    public final int hashCode() {
        return this.proxySafeHashCode();
    }

    /**
     * Proxy-aware {@code #equals(Object)} method.
     *
     * @see HibernateProxiesAware#proxySafeEquals(Object)
     */
    @Override
    public final boolean equals(Object o) {
        return this.proxySafeEquals(o);
    }

    /**
     * <b>This method is NOT proxy-aware</b>.
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", Receipt.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("total=" + total)
                .add("purchase=" + purchase)
                .add("timestamp=" + timestamp)
                .add("foundation='" + foundation + "'")
                .toString();
    }
}
