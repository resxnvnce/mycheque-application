package com.mycheque.service.integrate;

import com.mycheque.client.RequestBodyAttributes;

/**
 * Shortcut interface for a {@link OncePerRequestIntegration} bean factory.
 *
 * @author resxnvnce
 */
@FunctionalInterface
public interface OncePerRequestIntegrationProvider {

    /**
     * Get an integration prototype bean by using the supplied {@code RequestBodyAttributes.Factory}.
     *
     * @param factory the factory to create request bodies.
     * @return a {@link OncePerRequestIntegration} prototype bean.
     */
    OncePerRequestIntegration getPrototype(RequestBodyAttributes.Factory factory);
}
