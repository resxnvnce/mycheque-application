package com.mycheque.service.integrate;

import java.util.Set;

import com.mycheque.client.ClientTemplate;
import com.mycheque.client.RequestBodyAttributes;
import com.mycheque.client.ResponseBodyAttributes;
import com.mycheque.client.ResponseStatusCodeException;
import com.mycheque.client.jsonstruct.ResponseStatusCode;

import com.mycheque.datatransfer.intermediate.Remarkable;

import com.mycheque.service.commons.AbstractRemarkablesCollector;

/**
 * Abstract base class for the {@link OncePerRequestIntegration} implementors.
 * <p>
 * Its {@link #toRemarkables()} method implementation returns the original {@code Set}.
 *
 * @author resxnvnce
 */
public abstract class AbstractOncePerRequestIntegration extends AbstractRemarkablesCollector
        implements OncePerRequestIntegration {

    /**
     * The factory, creating {@code RequestBodyAttributes} for each request.
     */
    protected final RequestBodyAttributes.Factory factory;

    /**
     * The actual client logic implementor, interacting with the third-party API provider.
     */
    protected final ClientTemplate template;

    /**
     * The constructor for subclasses to call.
     *
     * @param factory the {@code RequestBodyAttributes} factory instance.
     * @param template  the client logic implementor.
     */
    protected AbstractOncePerRequestIntegration(RequestBodyAttributes.Factory factory, ClientTemplate template) {
        this.factory = factory;
        this.template = template;
    }

    /**
     * Convenient method to determine if the specified code
     * matches a scenario to be handled by this integration.
     *
     * @param code a {@code ResponseStatusCode} to inspect.
     * @return {@code true} if this integration should handle the specified status, {@code false} otherwise.
     */
    protected boolean shouldHandle(ResponseStatusCode code) {
        return !getUnhandledScenarios().test(code);
    }

    @Override
    public final Set<Remarkable> toRemarkables() {
        return this.remarkables;
    }

    /**
     * Returns an exception to be thrown on an {@linkplain #getUnhandledScenarios() unhandled scenario}.
     * <p>
     * <i>Don't ask me why this method is called like that.</i>
     *
     * @param response an error response which can't be handled any further.
     * @return a {@code RuntimeException} to throw if you've tried your best.
     */
    @SuppressWarnings("unchecked")
    protected static RuntimeException iHaveTriedMyBest(ResponseBodyAttributes<?> response) {
        Class<?> dataType = response.data().getClass();

        if (dataType == String.class) {
            final var errorResponse = (ResponseBodyAttributes<String>) response;
            return ResponseStatusCodeException.create(errorResponse);
        }

        /* The probability of this happening is not equal to zero, unfortunately. */
        return new ClassCastException(dataType + " is not a resolvable error response body type.");
    }
}
