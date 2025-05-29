package com.mycheque.controller.configure;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

/**
 * The controller infrastructure configuration class.
 *
 * @author resxnvnce
 */
@Configuration
public class RestControllerConfiguration {

    /**
     * Returns the {@link LocaleResolver} bean.
     *
     * @return the locale resolver.
     */
    @Bean
    LocaleResolver getLocaleResolver() {
        final var resolverBean = new AcceptHeaderLocaleResolver();

        resolverBean.setDefaultLocale(Locale.US);
        resolverBean.setSupportedLocales(
                forLanguageTags("en", "en-GB", "en-US", "ru", "ru-RU")
        );

        return resolverBean;
    }

    /**
     * Parse the given language tags into the matching {@link Locale}s.
     *
     * @param languageTags the language tags.
     * @return the {@code Locale}s that best represent the language tags.
     * @see Locale#forLanguageTag(String)
     */
    private List<Locale> forLanguageTags(String... languageTags) {
        return Stream.of(languageTags).distinct().map(Locale::forLanguageTag).toList();
    }
}
