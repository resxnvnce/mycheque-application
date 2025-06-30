package com.mycheque.util.hibernate6;

import org.hibernate.proxy.HibernateProxy;

/**
 * Convenient utility interface Hibernate entities may extend in order
 * to get proxy-safe, {@linkplain #getId() id} based implementations
 * of {@code Object} class methods.
 *
 * @author resxnvnce
 */
public interface HibernateProxiesAware {

    /**
     * Returns the entity identifier, which can be {@code null}.
     *
     * @return the id of this entity.
     */
    Object getId();

    /**
     * Returns the {@linkplain #getId() id} based hash code of this entity.
     *
     * @return the entity hash code.
     */
    default int proxySafeHashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    /**
     * Returns {@code true} if and only if {@code this} and {@code o} are the same
     * object, or both have the same {@linkplain #getEntityClass()
     * entity class} and {@linkplain #getId() identifier}.
     *
     * @param o an object to be compared with {@code this} for equality.
     * @return {@code true} if the arguments are equal to each other.
     */
    default boolean proxySafeEquals(Object o) {
        if (this == o)
            return true;

        return o instanceof HibernateProxiesAware hpa
                && getEntityClass() == hpa.getEntityClass()
                && getId() != null && getId().equals(hpa.getId());
    }

    /**
     * Returns the entity runtime class,
     * whether it is the {@link HibernateProxy} subclass or not.
     *
     * @return the entity runtime class.
     */
    default Class<?> getEntityClass() {
        return this instanceof HibernateProxy hp ? hp.getHibernateLazyInitializer().getPersistentClass() : getClass();
    }
}
