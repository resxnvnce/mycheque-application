package com.mycheque.domain;

import java.util.Set;

import com.mycheque.domain.id.FiscalDataRecord;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;

/**
 * {@link Receipt} entity component, representing a set of acquired items.
 * <p>
 * Note: the purchase total is obtained by
 * the {@link Receipt#getTotal()} instance method.
 *
 * @param id    the reference to the actual {@code Receipt}.
 * @param items the {@link Set} of items acquired.
 * @author resxnvnce
 */
@Document(collection = "purchase")
public record Purchase(@MongoId(FieldType.IMPLICIT) FiscalDataRecord id, @Field("items") Set<Item> items) {
}
