package com.mycheque.test.configure;

import java.util.List;
import java.util.ArrayList;

import org.springframework.context.annotation.Profile;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The {@code mycheque.client} infrastructure test properties.
 *
 * @author resxnvnce
 */
@Profile("test")
@ConfigurationProperties(prefix = "mycheque.test.client")
public class ClientTestProperties {

    /**
     * A list of trusted third-party tokens, eligible for testing.
     */
    private List<String> tokens = new ArrayList<>();

    /**
     * Returns a {@code List} of trusted third-party tokens.
     *
     * @return a list of tokens valid for testing.
     */
    public List<String> getTokens() {
        return this.tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }
}
