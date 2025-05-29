package com.mycheque.mapping;

import java.util.function.Function;

import com.mycheque.client.RequestBodyAttributes;

/**
 * Shortcut interface for a {@code Function} that accepts
 * a {@link RequestBodyAttributes.Factory} and produces
 * a {@code RequestBodyAttributes} instance.
 *
 * @author resxnvnce
 */
@FunctionalInterface
public interface RequestBodyAttributesMapper extends Function<RequestBodyAttributes.Factory, RequestBodyAttributes> {

    /**
     * Returns a {@link RequestBodyAttributesMapper}
     * that does only use a third-party token for mapping.
     *
     * @return a no-op mapper.
     */
    static RequestBodyAttributesMapper withDefaults() {
        return RequestBodyAttributes.Factory::create;
    }
}
