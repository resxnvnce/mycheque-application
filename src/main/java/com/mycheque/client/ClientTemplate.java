package com.mycheque.client;

import com.mycheque.lang.Nullable;
import com.mycheque.client.jsonstruct.StatusCode;
import com.mycheque.client.jsonstruct.ResponseStatusCode;

import static com.mycheque.util.Lambdas.applyOrNull;

/**
 * Client to perform HTTP POST requests to the predefined external API provider,
 * exposing a simple, template method API over underlying HTTP client
 * implementation. {@code ClientTemplate} offers templates for common scenarios.
 *
 * @author resxnvnce
 */
public interface ClientTemplate {

    /**
     * Determines whether the given authorization token is valid. That is,
     * does the token exist and if so, has it not been expired yet.
     *
     * @param token the authorization token to inspect. Must not be {@code null}.
     * @return {@code true} if the given token is valid, {@code false} otherwise.
     * @throws ClientTemplateException if the response body extraction failed.
     */
    default boolean canAuthorizeWith(String token) throws ClientTemplateException {
        final var attrs = RequestBodyAttributes.factory(token).create();
        return postForStatus(attrs) != StatusCode.UNAUTHORIZED;
    }

    /**
     * Perform an HTTP POST request having the autoconfigured headers
     * and the request body of {@link RequestBodyAttributes} provided.
     * <p>
     * The response body of {@link ResponseBodyAttributes} returned
     * can either be {@code null}, error attributes or
     * the target entity generic type attributes.
     *
     * @param attrs      the request body attributes.
     * @param entityType the generic type argument of the response body
     *                   returned in case of a successful request.
     * @return the response body converted into
     *         a {@code ResponseBodyAttributes} instance.
     * @throws ClientTemplateException if the response body extraction failed.
     */
    @Nullable
    <T> ResponseBodyAttributes<?> postForAttributes(RequestBodyAttributes attrs, Class<T> entityType)
            throws ClientTemplateException;

    /**
     * Returns the {@link ResponseStatusCode} retrieved upon {@linkplain
     * #postForAttributes(RequestBodyAttributes, Class) POSTing}
     * the given request body attributes.
     *
     * @param attrs the request body attributes.
     * @return the {@code ResponseStatusCode} from the response body.
     * @throws ClientTemplateException if the response body extraction failed.
     */
    @Nullable
    default ResponseStatusCode postForStatus(RequestBodyAttributes attrs) throws ClientTemplateException {
        return applyOrNull(postForAttributes(attrs, Void.class), ResponseBodyAttributes::code);
    }

    /**
     * Perform an HTTP POST request having the autoconfigured headers
     * and the request body of {@link RequestBodyAttributes} provided.
     * <p>
     * The response body of {@link ResponseBodyAttributes} returned can
     * either be {@code null} or the target entity generic type attributes.
     * <p>
     * This method throws a corresponding {@link ResponseStatusCodeException}
     * in case the {@code ResponseBodyAttributes} received contain an error.
     *
     * @param attrs      the request body attributes.
     * @param entityType the generic type argument of the response body
     *                   returned in case of a successful request.
     * @return the response body converted into
     *         a generic {@code ResponseBodyAttributes} instance.
     * @throws ClientTemplateException if the response body extraction failed
     *         or the {@code ResponseBodyAttributes} extracted have an error.
     */
    @Nullable
    <T> ResponseBodyAttributes<T> postForTypedAttributes(RequestBodyAttributes attrs, Class<T> entityType)
            throws ClientTemplateException;

    /**
     * Returns the target entity retrieved upon {@linkplain
     * #postForTypedAttributes POSTing} the given request body attributes.
     *
     * @param attrs      the request body attributes.
     * @param entityType the type of target entity
     *                   returned in case of a successful request.
     * @return the target entity of the response.
     * @throws ClientTemplateException if the response body extraction failed
     *         or the {@code ResponseBodyAttributes} extracted have an error.
     */
    @Nullable
    default <T> T postForEntity(RequestBodyAttributes attrs, Class<T> entityType) throws ClientTemplateException {
        return applyOrNull(postForTypedAttributes(attrs, entityType), ResponseBodyAttributes::data);
    }
}
