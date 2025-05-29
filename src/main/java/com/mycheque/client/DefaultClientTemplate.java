package com.mycheque.client;

import org.springframework.lang.Nullable;

import org.springframework.core.ResolvableType;
import org.springframework.core.ParameterizedTypeReference;

import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.ResourceAccessException;

import static com.mycheque.util.Lambdas.applyOrNull;

/**
 * The default {@link ClientTemplate} implementation.
 *
 * @author resxnvnce
 */
public class DefaultClientTemplate implements ClientTemplate {

    /**
     * The underlying HTTP client component.
     */
    private final RestClient client;

    /**
     * Constructs a {@code DefaultClientTemplate}.
     *
     * @param client the underlying HTTP client component to perform API requests.
     */
    public DefaultClientTemplate(RestClient client) {
        this.client = client;
    }

    /**
     * Perform an HTTP POST request with the given {@code RequestBodyAttributes}
     * as the request body and extract the response body using the entity type provided.
     *
     * @param attrs      the request body attributes.
     * @param entityType the generic type argument of the response body returned in case
     *                   of a successful request.
     * @return the response body converted into a {@code ResponseBodyAttributes} instance.
     */
    @Nullable
    protected <T> ResponseBodyAttributes<?> doPost(RequestBodyAttributes attrs, Class<T> entityType) {
        return client.post().body(attrs).retrieve().body(ParameterizedTypeReference.
                forType(ResolvableType.
                        forClassWithGenerics(ResponseBodyAttributes.class, entityType).getType()
                )
        );
    }

    @Nullable
    @Override
    public <T> ResponseBodyAttributes<?> postForAttributes(RequestBodyAttributes attrs, Class<T> entityType)
            throws ClientTemplateException {

        try {
            return doPost(attrs, entityType);
        }
        catch (ResourceAccessException rae) {
            throw new RequestAttributesException(attrs, "Failed to convert the request body.", rae);
        }
        catch (RestClientException rce) {
            throw new ResponseAttributesException("Failed to extract the response body.", rce);
        }
    }

    @Nullable
    @Override
    public <T> ResponseBodyAttributes<T> postForTypedAttributes(RequestBodyAttributes attrs, Class<T> entityType)
            throws ClientTemplateException {

        return applyOrNull(postForAttributes(attrs, entityType), this::rejectAnyError);
    }

    /**
     * Reject the given response in case it {@linkplain ResponseBodyAttributes#haveError() has an error},
     * throwing an appropriate exception, or return the response, casting to the requested type, otherwise.
     *
     * @param attrs the response to inspect.
     * @return the response, downcasted to the requested type.
     * @throws ResponseStatusCodeException if the response indeed contains an error.
     */
    @SuppressWarnings("unchecked")
    private <T> ResponseBodyAttributes<T> rejectAnyError(ResponseBodyAttributes<?> attrs) {
        final boolean isRejected = attrs.haveError();

        if (!isRejected) {
            return (ResponseBodyAttributes<T>) attrs;
        }

        Class<?> dataType = attrs.data().getClass();

        if (dataType == String.class) {
            final var errorAttrs = (ResponseBodyAttributes<String>) attrs;
            throw ResponseStatusCodeException.create(errorAttrs);
        }

        /* Should never happen. */
        throw new ClassCastException(dataType + " is not a resolvable error response body type.");
    }
}
