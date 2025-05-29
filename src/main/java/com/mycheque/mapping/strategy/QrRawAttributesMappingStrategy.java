package com.mycheque.mapping.strategy;

import org.springframework.stereotype.Component;

import com.mycheque.mapping.RequestBodyAttributesMapper;
import com.mycheque.datatransfer.accept.ReceiptDefinition;

/**
 * A mapping strategy eligible to work with {@link ReceiptDefinition.ByQrRaw} definitions.
 *
 * @author resxnvnce
 */
@Component
public class QrRawAttributesMappingStrategy extends AbstractRequestBodyAttributesMappingStrategy {

    @Override
    public ReceiptDefinition.By basedOn() {
        return ReceiptDefinition.By.QR_RAW;
    }

    @Override
    public boolean supports(Class<? extends ReceiptDefinition> definitionClass) {
        return ReceiptDefinition.ByQrRaw.class == definitionClass;
    }

    @Override
    protected RequestBodyAttributesMapper getSupportedMapper(ReceiptDefinition definition) {
        String qrraw = ((ReceiptDefinition.ByQrRaw) definition).qrraw();
        return factory -> factory.byQrRaw(qrraw);
    }
}
