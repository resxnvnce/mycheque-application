package com.mycheque.client.deserialize;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

/**
 * Compound Jackson annotation for the
 * {@link com.mycheque.client.ResponseBodyAttributes ResponseBodyAttributes} interface.
 *
 * @author resxnvnce
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "code",
        defaultImpl = ErrorResponseBody.class,
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(
                value = GenericResponseBody.class,
                names = {"1"}
        )
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonAnnotationsInside
public @interface Polymorphic {
}
