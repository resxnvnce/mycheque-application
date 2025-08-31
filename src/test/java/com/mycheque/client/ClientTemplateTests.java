package com.mycheque.client;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.mycheque.test.configure.ClientTestProperties;
import com.mycheque.test.configure.ClientTestConfiguration;

/**
 * The {@link ClientTemplate} contract tests.
 *
 * @author resxnvnce
 */
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = ClientTestConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientTemplateTests {

    @Autowired
    private ClientTemplate clientTemplate;

    @Autowired
    private ClientTestProperties testProperties;

    /**
     * Returns a {@code List} of trusted third-party tokens.
     *
     * @return a list of non-expired third-party tokens.
     * @see ClientTestProperties#getTokens()
     */
    private List<String> getTokens() {
        return this.testProperties.getTokens();
    }

    /* #canAuthorizeWith(String) */

    @Test
    @DisplayName("#canAuthorizeWith(String) must throw a RuntimeException if null passed")
    void test00() {
        Assertions.assertThrows(
                RuntimeException.class,
                () -> this.clientTemplate.canAuthorizeWith(null)
        );
    }

    @Test
    @DisplayName("#canAuthorizeWith(String) must return false if the token is invalid")
    void test01() {
        boolean result = this.clientTemplate.canAuthorizeWith("<INVALID_TOKEN>");

        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("getTokens")
    @DisplayName("#canAuthorizeWith(String) must return true if the token is valid")
    void test02(String token) {
        boolean result = this.clientTemplate.canAuthorizeWith(token);

        Assertions.assertTrue(result);
    }
}
