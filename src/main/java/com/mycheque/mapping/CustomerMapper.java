package com.mycheque.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.mycheque.domain.Customer;

import com.mycheque.datatransfer.profile.Profile;
import com.mycheque.datatransfer.profile.Credentials;
import com.mycheque.datatransfer.profile.ProfileUpdate;
import com.mycheque.datatransfer.profile.CredentialsUpdate;

/**
 * The mapper interface that assists in mapping several {@link Customer}-related
 * data transfer objects to the actual entities and vice versa.
 *
 * @author resxnvnce
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING, implementationName = "Mapstruct<CLASS_NAME>",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

    /**
     * Apply the specified credentials updates to the given {@code Customer}.
     * Properties set to {@code null} are silently ignored.
     * <p>
     * <b>NOTE:</b> The updates are expected to be validated before mapping.
     *
     * @param updates  the updates to apply.
     * @param customer the mapping target.
     */
    default void applyUpdates(CredentialsUpdate updates, Customer customer) {
        String newToken = updates.token();
        if (newToken != null)
            customer.setThirdpartyToken(newToken);

        applyUpdates(updates.profile(), customer);
    }

    /**
     * Apply the specified profile updates to the given {@code Customer}.
     * Properties set to {@code null} are silently ignored.
     * <p>
     * <b>NOTE:</b> The updates are expected to be validated before mapping.
     *
     * @param updates  the updates to apply.
     * @param customer the mapping target.
     */
    void applyUpdates(ProfileUpdate updates, @MappingTarget Customer customer);

    /**
     * Convert the given {@code Profile} into a customer entity.
     * <p>
     * <b>NOTE:</b> The profile is expected to be validated before mapping.
     *
     * @param profile the customer's profile.
     * @return the {@link Customer} entity.
     */
    Customer toCustomer(Profile profile);

    /**
     * Convert the given {@code Credentials} into a customer entity.
     * <p>
     * <b>NOTE:</b> The credentials are expected to be validated before mapping.
     *
     * @param credentials the customer's credentials.
     * @return the {@link Customer} entity.
     */
    @Mapping(target = "username", expression = "java( credentials.profile().getUsername() )")
    @Mapping(target = "password", expression = "java( credentials.profile().getPassword() )")
    @Mapping(target = "thirdpartyToken", source = "token")
    Customer toCustomer(Credentials credentials);
}
