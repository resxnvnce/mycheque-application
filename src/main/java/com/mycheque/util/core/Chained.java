package com.mycheque.util.core;

/**
 * An interface that can be implemented by objects
 * that wish to participate in an <i>execution chain</i>.
 * <p>
 * The actual {@link #getPosition() position} can be interpreted as prioritization,
 * with the first object (with the lowest order value) having the highest priority.
 *
 * @author resxnvnce
 */
public interface Chained extends Comparable<Chained> {

    /**
     * Useful constant for the highest precedence value.
     *
     * @see java.lang.Integer#MIN_VALUE
     */
    int HEAD = Integer.MIN_VALUE;

    /**
     * Useful constant for the lowest precedence value.
     *
     * @see java.lang.Integer#MAX_VALUE
     */
    int TAIL = Integer.MAX_VALUE;

    /**
     * Get this object' chain position.
     * <p>
     * Higher values are interpreted as lower priority.
     * As a consequence, the object with the lowest value has the highest priority.
     * <p>
     * Same position values will result in arbitrary sort indices for the affected objects.
     *
     * @return the position value.
     * @see #HEAD
     * @see #TAIL
     */
    int getPosition();

    /**
     * Compares two {@code Chained} objects by their {@link #getPosition() position} values.
     *
     * @param other the object to be compared.
     * @return the position values comparison result, fulfilling the
     *         {@link Comparable#compareTo(Object)} method contract.
     */
    @Override
    default int compareTo(Chained other) {
        int otherPosition = other.getPosition();
        return Integer.compare(getPosition(), otherPosition);
    }
}
