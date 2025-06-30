package com.mycheque.mapping;

import java.util.Set;
import java.util.Collection;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingConstants;

import com.mycheque.domain.Item;
import com.mycheque.domain.Receipt;
import com.mycheque.domain.Purchase;

import com.mycheque.datatransfer.ClientEntity;

/**
 * The mapper interface that assists in extracting a {@link Receipt} entity from
 * instances of the default target entity type, {@link ClientEntity}.
 *
 * @author resxnvnce
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING, implementationName = "Mapstruct<CLASS_NAME>")
public interface ClientEntityExtractor {

    /**
     * Convert the given {@code ClientEntity.Item} into an {@link Item}.
     *
     * @param entityItem a target entity purchase item.
     * @return the {@link Item} reproduced from the state of
     * the target entity item given.
     */
    @Mapping(target = "count", expression = "java( entityItem.total() / price )")
    Item toItem(ClientEntity.Item entityItem);

    /**
     * Returns all the given {@code ClientEntity.Item}s
     * converted to an unmodifiable set of {@code Item}s.
     *
     * @param entityItems the target entity purchase items.
     * @return the {@link Set} of {@code Item}s reproduced from the state of
     *         the target entity items given.
     * @throws NullPointerException if {@code entityItems.contains(null)}.
     * @see Collectors#toUnmodifiableSet()
     */
    default Set<Item> toItems(Collection<ClientEntity.Item> entityItems) {
        return entityItems.stream().map(this::toItem).collect(
                Collectors.toUnmodifiableSet()
        );
    }

    /**
     * Returns the {@code Purchase} record extracted from the given target entity.
     *
     * @param entity the target entity.
     * @return the extracted {@link Purchase} record.
     */
    Purchase toPurchase(ClientEntity entity);

    /**
     * Ensures a receipt is treated as a {@linkplain Receipt#isNew() new one}
     * after mapping from a {@code ClientEntity}.
     *
     * @param receipt a {@link Receipt} entity.
     */
    @AfterMapping
    default void afterEntityMapping(@MappingTarget Receipt receipt) {
        receipt.setNew(true);
    }

    /**
     * Returns the {@code Receipt} entity extracted from the given target entity.
     * <p>
     * NOTE: the {@linkplain Receipt#getCustomer() customer} value remains empty.
     *
     * @param entity the target entity.
     * @return the extracted {@link Receipt} entity.
     */
    @Mapping(target = "purchase", expression = "java( toPurchase(entity) )")
    Receipt toReceipt(ClientEntity entity);
}
