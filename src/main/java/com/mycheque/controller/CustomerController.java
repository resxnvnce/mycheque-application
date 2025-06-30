package com.mycheque.controller;

import lombok.AllArgsConstructor;

import java.util.Locale;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.mycheque.domain.Customer;

import com.mycheque.service.CustomerService;
import com.mycheque.service.wrapper.AuthorizedWrapper;

import com.mycheque.controller.i18n.MessageResolver;
import com.mycheque.controller.validation.CredentialsValidator;

import com.mycheque.datatransfer.result.GenericResult;
import com.mycheque.datatransfer.profile.Credentials;
import com.mycheque.datatransfer.profile.CredentialsUpdate;

import com.mycheque.security.DelegatingCustomerDetails;

/**
 * The {@link RestController} eligible to work with {@link Customer}-related endpoints.
 *
 * @author resxnvnce
 */
@RestController
@RequestMapping(path = "/mycheque.com/v1/customers/")
@AllArgsConstructor // the constructor might be too large
public class CustomerController {

    /**
     * The {@link Customer} service logic implementor.
     */
    private final CustomerService customerService;

    /**
     * The {@link Customer} passwords encoder.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Localized, interpolated messages supplier.
     */
    private final MessageResolver messageResolver;

    /**
     * The validator for both {@link Credentials} and {@link CredentialsUpdate} records.
     */
    private final CredentialsValidator credentialsValidator;

    @PostMapping
    public ResponseEntity<GenericResult> register(
            @Valid @RequestBody Credentials credentials, BindingResult errors, Locale locale) {

        boolean isBodyValid = !errors.hasErrors() && this.credentialsValidator.validate(credentials, errors);
        if (!isBodyValid) {
            String message = this.messageResolver.onFailedRegistration(errors, locale);
            return new ResponseEntity<>(GenericResult.failed(message), HttpStatus.BAD_REQUEST);
        }

        credentials.securePasswordUsing(this.passwordEncoder::encode);

        Object description = this.customerService.register(credentials).toMutationDescription();
        String message = this.messageResolver.onRegistration(locale);

        return new ResponseEntity<>(GenericResult.succeeded(message, description), HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<GenericResult> applyUpdates(
            @AuthenticationPrincipal DelegatingCustomerDetails principal,
            @Valid @RequestBody CredentialsUpdate credentialsUpdate, BindingResult errors, Locale locale) {

        final var wrapper = new AuthorizedWrapper<>(principal.getDelegate(), credentialsUpdate);

        boolean isBodyValid = !errors.hasErrors() && this.credentialsValidator.validate(wrapper, errors);
        if (!isBodyValid) {
            String message = this.messageResolver.onFailedUpdate(errors, locale);
            return new ResponseEntity<>(GenericResult.failed(message), HttpStatus.BAD_REQUEST);
        }

        credentialsUpdate.securePasswordUsing(this.passwordEncoder::encode);

        Object description = this.customerService.applyUpdates(wrapper).toMutationDescription();
        String message = this.messageResolver.onUpdate(locale);

        return new ResponseEntity<>(GenericResult.succeeded(message, description), HttpStatus.OK);
    }
}
