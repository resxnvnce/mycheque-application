package com.mycheque.mapping.strategy;

import org.springframework.stereotype.Component;

import com.mycheque.mapping.RequestBodyAttributesMapper;
import com.mycheque.datatransfer.query.ReceiptDefinition;

/**
 * A mapping strategy eligible to work with {@link ReceiptDefinition.ByQrUrl} definitions.
 *
 * @author resxnvnce
 */
@Component
public class QrUrlAttributesMappingStrategy extends AbstractRequestBodyAttributesMappingStrategy {

    @Override
    public ReceiptDefinition.By basedOn() {
        return ReceiptDefinition.By.QR_URL;
    }

    @Override
    public boolean supports(Class<? extends ReceiptDefinition> definitionClass) {
        return ReceiptDefinition.ByQrUrl.class == definitionClass;
    }

    @Override
    protected RequestBodyAttributesMapper getSupportedMapper(ReceiptDefinition definition) {
        String qrurl = ((ReceiptDefinition.ByQrUrl) definition).qrurl();
        return factory -> factory.byQrUrl(qrurl);
    }
}
