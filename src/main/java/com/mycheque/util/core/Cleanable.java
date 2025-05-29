package com.mycheque.util.core;

/**
 * Indicates that an object is capable of cleaning itself up,
 * releasing some resources and references to another objects.
 *
 * @author resxnvnce
 */
public interface Cleanable {

    /**
     * Release all the resources and/or references the object has been potentially held.
     */
    void cleanUp();
}
