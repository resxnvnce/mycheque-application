package com.mycheque.mapping.strategy;

import org.springframework.stereotype.Component;

import com.mycheque.mapping.RequestBodyAttributesMapper;
import com.mycheque.datatransfer.accept.ReceiptDefinition;

/**
 * A mapping strategy eligible to work with {@link ReceiptDefinition.ByDetails} definitions.
 *
 * @author resxnvnce
 */
@Component
public class TargetDetailsMappingStrategy extends AbstractRequestBodyAttributesMappingStrategy {

    @Override
    public ReceiptDefinition.By basedOn() {
        return ReceiptDefinition.By.DETAILS;
    }

    @Override
    public boolean supports(Class<? extends ReceiptDefinition> definitionClass) {
        return ReceiptDefinition.ByDetails.class == definitionClass;
    }

    @Override
    protected RequestBodyAttributesMapper getSupportedMapper(ReceiptDefinition definition) {
        var details = (ReceiptDefinition.ByDetails) definition;
        return factory -> factory.byDetails(details.id())
                .timestamp(details.timestamp())
                .total(details.total())
                .build();
    }
}
