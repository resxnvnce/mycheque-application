package com.mycheque.mapping.support;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Documented;

/**
 * Annotation being used by <i>MapStruct</i> to resolve the target constructor to use.
 * <p>
 * Should be placed above the primary constructor to break
 * <a href=https://mapstruct.org/documentation/stable/reference/html/#non-shipped-annotations>the ambiguity</a>.
 *
 * @author resxnvnce
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface Default {
}
