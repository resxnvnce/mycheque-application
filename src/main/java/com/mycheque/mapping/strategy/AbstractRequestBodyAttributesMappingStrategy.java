package com.mycheque.mapping.strategy;

import com.mycheque.datatransfer.query.ReceiptDefinition;

import com.mycheque.mapping.RequestBodyAttributesMapper;
import com.mycheque.mapping.RequestBodyAttributesMappingStrategy;

/**
 * Base class for the {@link RequestBodyAttributesMappingStrategy} implementations.
 *
 * @author resxnvnce
 */
public abstract class AbstractRequestBodyAttributesMappingStrategy implements RequestBodyAttributesMappingStrategy {

    @Override
    public final RequestBodyAttributesMapper getMapper(ReceiptDefinition definition) {
        final Class<? extends ReceiptDefinition> definitionClass = definition.getClass();

        return this.supports(definitionClass) ? this.getSupportedMapper(definition) :
                RequestBodyAttributesMapper.withDefaults(); // the fallback mapper
    }

    /**
     * Returns the corresponding {@code RequestBodyAttributesMapper} for
     * a {@code ReceiptDefinition}, which is <i>guaranteed to</i>
     * <i>be supported</i> by this strategy.
     *
     * @param definition a {@link ReceiptDefinition} with
     *                   a runtime class supported by this strategy.
     * @return the mapper to be used for the given receipt definition.
     */
    protected abstract RequestBodyAttributesMapper getSupportedMapper(ReceiptDefinition definition);
}
