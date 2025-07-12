package com.mycheque.client.configure;

import com.mycheque.test.properties.ClientTestProperties;

import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * The client test infrastructure configuration class.
 *
 * @author resxnvnce
 */
@Import(ClientConfiguration.class)
@Profile("test")
@Configuration
@EnableConfigurationProperties(ClientTestProperties.class)
public class ClientTestsConfiguration {
}
