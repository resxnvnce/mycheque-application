package com.mycheque.validation.configure;

import com.mycheque.client.ClientTemplate;

import com.mycheque.service.CustomerService;

import com.mycheque.validation.CustomerTokenValidator;
import com.mycheque.validation.UsernameNotTakenValidator;
import com.mycheque.validation.DelegatingCustomerTokenValidator;
import com.mycheque.validation.DelegatingUsernameNotTakenValidator;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * The validation infrastructure custom configuration class.
 *
 * @author resxnvnce
 */
@Configuration
public class ValidationConfiguration {

    /**
     * Returns the {@link MessageSource} bean for resolving validation error messages.
     *
     * @return bean validation errors message source.
     */
    @Bean
    @BeanValidation
    MessageSource getMessageSource() {
        final var sourceBean = new ReloadableResourceBundleMessageSource();

        sourceBean.setBasename("classpath:validation/messages");
        sourceBean.setDefaultEncoding("UTF-8");

        return sourceBean;
    }

    /**
     * Returns the {@link LocalValidatorFactoryBean} using the custom validation message source.
     *
     * @return the validator instance to be used.
     */
    @Bean
    @BeanValidation
    LocalValidatorFactoryBean getValidator() {
        final var validatorBean = new LocalValidatorFactoryBean();

        validatorBean.setValidationMessageSource(
                getMessageSource()
        );

        return validatorBean;
    }

    /**
     * Returns the {@link CustomerTokenValidator} for
     * validating tokens expected to be valid.
     *
     * @param delegate the template performing the condition evaluation.
     * @return the validator ensuring incoming token is valid.
     */
    @Bean
    CustomerTokenValidator customerTokenValidator(ClientTemplate delegate) {
        return new DelegatingCustomerTokenValidator(delegate);
    }

    /**
     * Returns the {@link UsernameNotTakenValidator} for
     * validating usernames expected to be not taken by anyone.
     *
     * @param delegate the service performing the condition evaluation.
     * @return the validator ensuring incoming username is a new one.
     */
    @Bean
    UsernameNotTakenValidator usernameNotTakenValidator(CustomerService delegate) {
        return new DelegatingUsernameNotTakenValidator(delegate);
    }
}
