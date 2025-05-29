package com.mycheque.mapping;

import com.mycheque.datatransfer.accept.ReceiptDefinition;

/**
 * The strategy interface capable of mapping {@code ReceiptDefinition}s to
 * corresponding {@code RequestBodyAttributes} instances for further usage
 * by the {@link com.mycheque.client.ClientTemplate ClientTemplate}.
 *
 * @author resxnvnce
 */
public interface RequestBodyAttributesMappingStrategy {

    /**
     * Returns the receipt definition {@linkplain ReceiptDefinition.By method}
     * this strategy based on.
     *
     * @return the definition method this strategy operates on.
     */
    ReceiptDefinition.By basedOn();

    /**
     * Is this strategy suitable for instances of the supplied {@code definitionClass}?
     *
     * @param definitionClass the {@link Class} which may or may not
     *                        be supported by this strategy.
     * @return {@code true} if this {@link RequestBodyAttributesMappingStrategy} is
     *         indeed suitable for instances of the supplied {@code definitionClass}.
     */
    boolean supports(Class<? extends ReceiptDefinition> definitionClass);

    /**
     * Returns a {@link RequestBodyAttributesMapper} for the given {@code ReceiptDefinition},
     * which runtime class should be {@linkplain #supports(Class) supported} by this strategy.
     * <p>
     * <b>NOTE:</b> If the runtime class of {@code definition} is unsupported for some reason,
     * the fallback mapper, which does only use a third-party token, is returned.
     *
     * @param definition a receipt definition to be mapped into
     *                   a {@code RequestBodyAttributes} instance.
     * @return the corresponding {@link RequestBodyAttributesMapper}.
     */
    RequestBodyAttributesMapper getMapper(ReceiptDefinition definition);
}
