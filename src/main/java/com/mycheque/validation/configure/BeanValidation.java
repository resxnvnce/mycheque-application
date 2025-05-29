package com.mycheque.validation.configure;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Inherited;
import java.lang.annotation.Documented;

import org.springframework.beans.factory.annotation.Qualifier;

/**
 * This annotation indicates an element is a part of the bean validation infrastructure.
 *
 * @author resxnvnce
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Qualifier
public @interface BeanValidation {

    /**
     * @see Qualifier#value()
     */
    String value() default "";
}
