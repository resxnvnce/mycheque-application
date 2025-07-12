package com.mycheque.client.configure;

import com.mycheque.client.ClientTemplate;
import com.mycheque.client.DefaultClientTemplate;

import org.springframework.web.client.RestClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The client module configuration class.
 *
 * @author resxnvnce
 */
@Configuration
public class ClientConfiguration {

    /**
     * Returns the underlying HTTP client component for the {@link ClientTemplate}
     * bean. Base URL is configured externally and the "Content-Type" header value
     * is hardcoded to be the "application/json".
     * <p>
     * Has a {@code ClientHttpRequestInterceptor}
     * that provides the "Content-Length" header value with each request,
     * so there's no need to serialize the body into a byte array manually.
     *
     * @return the {@code RestClient} to be used by the default {@code ClientTemplate}.
     */
    @Bean
    RestClient restClient() {
        return RestClient.builder()
                .baseUrl("https://proverkacheka.com/api/v1/check/get")
                .defaultHeader("Content-Type", "application/json")
                .requestInterceptor(
                        (request, body, execution) -> {
                            request.getHeaders().setContentLength(body.length);
                            return execution.execute(request, body);
                        }
                )
                .build();
    }

    /**
     * Returns the base {@link ClientTemplate} implementation bean
     * with the autoconfigured HTTP client component inside.
     *
     * @param restClient the underlying HTTP client component.
     * @return the base {@code ClientTemplate} bean.
     */
    @Bean
    ClientTemplate clientTemplate(RestClient restClient) {
        return new DefaultClientTemplate(restClient);
    }
}
