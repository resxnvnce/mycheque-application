package com.mycheque.util.hibernate6;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Documented;

import org.hibernate.type.SqlTypes;
import org.hibernate.annotations.JdbcTypeCode;

/**
 * Indicates that Hibernate should use the {@link SqlTypes#NAMED_ENUM} type code for the column mapping.
 *
 * @author resxnvnce
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JdbcTypeCode(SqlTypes.NAMED_ENUM)
public @interface NamedEnum {
}
