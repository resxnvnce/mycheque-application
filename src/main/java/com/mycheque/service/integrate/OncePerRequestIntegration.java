package com.mycheque.service.integrate;

import java.util.function.Predicate;

import com.mycheque.client.ClientTemplateException;
import com.mycheque.client.jsonstruct.StatusCode;
import com.mycheque.client.jsonstruct.ResponseStatusCode;

import com.mycheque.service.commons.PatchStateTracker;
import com.mycheque.service.commons.RemarkablesCollector;

/**
 * Integration interface that serves as a bridge between
 * the {@link com.mycheque.service.ReceiptService ReceiptService}
 * and the {@link com.mycheque.client.ClientTemplate ClientTemplate}.
 *
 * @author resxnvnce
 * @apiNote It's a good practice to {@link #cleanUp()} the integration upon usage.
 */
public interface OncePerRequestIntegration extends RemarkablesCollector {

    /**
     * Returns a {@link Predicate} describing the {@code ResponseStatusCode}s of
     * client responses <i>not being handled</i> by this integration.
     * <p>
     * In a scenario <i>not being handled</i> the integration throws a corresponding
     * {@link ClientTemplateException} instead.
     * The remarkables {@linkplain #toRemarkables() collector} is not populated in that case.
     *
     * @return a {@code Predicate} describing the unhandled scenarios.
     */
    default Predicate<ResponseStatusCode> getUnhandledScenarios() {
        return code -> !(code instanceof StatusCode) || code == StatusCode.UNAUTHORIZED;
    }

    /**
     * Retrieve the fully populated (except for the
     * {@linkplain com.mycheque.domain.Receipt#getCustomer() owner} property)
     * entity of a {@code Receipt}, matching the {@link PatchStateTracker} specified.
     * <p>The {@linkplain PatchStateTracker#getEntityState() entity state} remains unset
     * <i>in several cases</i>:<ul>
     * <li>the {@code ClientTemplate} being used returns {@code null} for the request built;</li>
     * <li>the response received has a {@linkplain ResponseStatusCode status code}
     * indicating a scenario being handled by this integration.</li>
     * </ul><p>
     * In both cases the remarkables {@linkplain #toRemarkables() collector} is populated.
     *
     * @param tracker a patch state tracker to resolve to an actual entity.
     * @throws ClientTemplateException if the status code of the response
     *         {@linkplain #getUnhandledScenarios() is not handled} by this integration,
     *         or if an internal I/O error occured.
     */
    void retrieve(PatchStateTracker tracker) throws ClientTemplateException;
}
