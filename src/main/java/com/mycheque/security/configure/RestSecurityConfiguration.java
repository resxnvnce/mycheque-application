package com.mycheque.security.configure;

import org.springframework.http.HttpMethod;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * The Spring Security support configuration class.
 *
 * @author resxnvnce
 */
@Configuration
@EnableWebSecurity
public class RestSecurityConfiguration {

    /**
     * {@code SecurityFilterChain} bean configuration.
     * <p>
     * For now, most of the API endpoints rely on the HTTP Basic authentication,
     * that's why no CSRF protection nor session management are being used.
     * <p>
     * <b>NOTE:</b> The <i>MyCheque REST API</i> security might be altered in a
     * future release (for instance, the JWT tokens support might come in handy),
     * so this bean configuration might not be in its final state.
     *
     * @param http the Spring Security support configurer.
     * @return the chain of {@linkplain jakarta.servlet.Filter filters} being matched against a
     *         {@link jakarta.servlet.http.HttpServletRequest HttpServletRequest}s.
     * @throws Exception should not be thrown if configured properly.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessions -> sessions
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authrz -> authrz
                        .requestMatchers("/mycheque.com/v1/receipts/**")
                        .authenticated()

                        .requestMatchers(HttpMethod.PATCH, "/mycheque.com/v1/customers")
                        .authenticated()

                        .anyRequest()
                        .permitAll()
                )
                .httpBasic(
                        Customizer.withDefaults()
                )
                .build();
    }

    /**
     * Returns the default {@code PasswordEncoder}.
     * <p>
     * <i>Not a single password should be stored in the raw form.<i>
     *
     * @return the password encoder to be used.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
