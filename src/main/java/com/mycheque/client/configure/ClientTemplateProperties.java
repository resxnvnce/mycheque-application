package com.mycheque.client.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuration properties for the {@link com.mycheque.client.ClientTemplate ClientTemplate}.
 *
 * @param url the HTTP POST request URL.
 * @author resxnvnce
 */
@ConfigurationProperties(prefix = "mycheque.client.template")
public record ClientTemplateProperties(@DefaultValue("https://proverkacheka.com/api/v1/check/get") String url) {
}
