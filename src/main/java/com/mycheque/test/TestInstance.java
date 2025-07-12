package com.mycheque.test;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Documented;

import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * {@link org.junit.jupiter.api.TestInstance} shortcut annotations.
 *
 * @author resxnvnce
 */
public final class TestInstance {

    /**
     * A shortcut for {@code @TestInstance(Lifecycle.PER_CLASS)}.
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @org.junit.jupiter.api.TestInstance(Lifecycle.PER_CLASS)
    public @interface PerClass {
    }

    /**
     * A shortcut for {@code @TestInstance(Lifecycle.PER_METHOD)}.
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @org.junit.jupiter.api.TestInstance(Lifecycle.PER_METHOD)
    public @interface PerMethod {
    }

    /**
     * Nah-uh.
     */
    private TestInstance() {
        throw new AssertionError("Utility class instantiation is not allowed!");
    }
}
