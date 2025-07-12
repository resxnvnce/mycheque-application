package com.mycheque.lang;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Documented;

/**
 * An annotation to declare that the annotated element
 * cannot be {@code null}.
 * <p>
 * Should be used at the parameter, return value, and field level.
 * Method overrides should repeat parent {@code @NonNull}
 * annotations unless they behave differently.
 *
 * @author resxnvnce
 */
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface NonNull {
}
