package com.mycheque.service.integrate;

import java.util.Map;

import org.springframework.lang.Nullable;

import com.mycheque.client.ClientTemplate;
import com.mycheque.client.RequestBodyAttributes;
import com.mycheque.client.ResponseBodyAttributes;
import com.mycheque.client.ClientTemplateException;
import com.mycheque.client.jsonstruct.StatusCode;
import com.mycheque.client.jsonstruct.ResponseStatusCode;

import com.mycheque.domain.Receipt;

import com.mycheque.datatransfer.ClientEntity;
import com.mycheque.datatransfer.query.ReceiptDefinition;
import com.mycheque.datatransfer.intermediate.Remarkable;
import com.mycheque.datatransfer.intermediate.PatchRemark;
import com.mycheque.datatransfer.intermediate.PatchRemarkCode;

import com.mycheque.mapping.ClientEntityExtractor;
import com.mycheque.mapping.RequestBodyAttributesMappingStrategy;

import com.mycheque.service.commons.PatchStateTracker;

/**
 * The default {@link OncePerRequestIntegration} implementation.
 *
 * @author resxnvnce
 */
public class OncePerHttpRequestIntegration extends AbstractOncePerRequestIntegration {

    /**
     * Map of {@code RequestBodyAttributesMappingStrategy} beans respectively to their definition method.
     */
    private final Map<ReceiptDefinition.By, RequestBodyAttributesMappingStrategy> strategies;

    /**
     * The mapper supporting with resolving a {@link ClientEntity} to the actual {@link Receipt}.
     */
    private final ClientEntityExtractor extractor;

    /**
     * Constructs a new prototype of {@code OncePerHttpRequestIntegration}.
     *
     * @param strategies the {@code RequestBodyAttributes} mapping strategies, mapped.
     * @param factory    the factory to create request bodies.
     * @param template   the actual client logic implementor.
     * @param extractor  the <b>JSON</b> entities extractor.
     */
    public OncePerHttpRequestIntegration(Map<ReceiptDefinition.By, RequestBodyAttributesMappingStrategy> strategies,
                                         RequestBodyAttributes.Factory factory, ClientTemplate template,
                                         ClientEntityExtractor extractor) {
        super(factory, template);
        this.extractor = extractor;
        this.strategies = strategies;
    }

    @Override
    public void retrieve(PatchStateTracker tracker) throws ClientTemplateException {
        ResponseBodyAttributes<?> response = postForTracker(tracker);
        if (response == null) {
            remarkWith(tracker, PatchRemark.UNRECOGNIZABLE);
        }

        else if (response.data() instanceof ClientEntity entity) {
            final var receipt = this.extractor.toReceipt(entity);
            tracker.setEntityState(receipt);
        }

        else {
            final ResponseStatusCode status = response.code();

            boolean isHandleableStatus = this.shouldHandle(status);
            if (!isHandleableStatus) {
                throw AbstractOncePerRequestIntegration.iHaveTriedMyBest(response);
            }

            remarkWith(tracker, status == StatusCode.TIMEOUT ? PatchRemark.TIMEOUT : PatchRemark.UNRECOGNIZABLE);
        }
    }

    /**
     * Populate the remarkables {@linkplain #toRemarkables() collector} with a brand-new record.
     *
     * @param tracker a patch state tracker.
     * @param code    a code to remark the {@code tracker} source with.
     */
    private void remarkWith(PatchStateTracker tracker, PatchRemarkCode code) {
        final var remarkable = Remarkable.of(tracker.toSource(), code);
        super.collect(remarkable);
    }

    /**
     * Map the given {@code ReceiptDefinition} to a {@code RequestBodyAttributes} instance.
     *
     * @param definition the definition to map into a request body.
     * @return a {@link RequestBodyAttributes} container prepared to be sent.
     */
    private RequestBodyAttributes mapToRequest(ReceiptDefinition definition) {
        final ReceiptDefinition.By byMethod = definition.by();
        return this.strategies.get(byMethod).getMapper(definition).apply(this.factory);
    }

    /**
     * Retrieve a raw typed {@code ResponseBodyAttributes}, sending the {@linkplain
     * PatchStateTracker#getIntermediateState() intermediate state}
     * of the given tracker, mapped to a request body.
     *
     * @param tracker a patch state tracker to be sent.
     * @return the response received.
     * @throws ClientTemplateException in case of an internal I/O error.
     */
    @Nullable
    private ResponseBodyAttributes<?> postForTracker(PatchStateTracker tracker) {
        final ReceiptDefinition intermediate = tracker.getIntermediateState();
        return this.template.postForAttributes(mapToRequest(intermediate), ClientEntity.class);
    }
}
