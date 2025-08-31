package com.mycheque.datatransfer.query;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

/**
 * Compound Jackson annotation for the {@link ReceiptDefinition} interface.
 *
 * @author resxnvnce
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "definition",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(
                value = ReceiptDefinition.ByQrRaw.class,
                name = "qr_raw"
        ),
        @JsonSubTypes.Type(
                value = ReceiptDefinition.ByQrUrl.class,
                name = "qr_url"
        ),
        @JsonSubTypes.Type(
                value = ReceiptDefinition.ByDetails.class,
                name = "details"
        )
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonAnnotationsInside
public @interface ReceiptDefinitionTypeInfo {
}
