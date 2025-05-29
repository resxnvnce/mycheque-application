package com.mycheque.controller;

import java.util.List;
import java.util.Locale;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.mycheque.domain.Receipt;

import com.mycheque.service.ReceiptService;
import com.mycheque.service.wrapper.PatchnotesOutcome;
import com.mycheque.service.wrapper.PatchnotesWrapper;

import com.mycheque.controller.i18n.MessageResolver;
import com.mycheque.controller.i18n.PatchnotesOutcomeResultExtractor;

import com.mycheque.datatransfer.accept.Patchnotes;
import com.mycheque.datatransfer.expose.GenericResult;

import com.mycheque.security.DelegatingCustomerDetails;

/**
 * The {@link RestController} eligible to work with {@link Receipt}-related endpoints.
 *
 * @author resxnvnce
 */
@RestController
@RequestMapping(path = "/mycheque.com/v1/receipts/")
public class ReceiptController {

    /**
     * The {@link Receipt} service logic implementor.
     */
    private final ReceiptService receiptService;

    /**
     * Localized, interpolated messages supplier.
     */
    private final MessageResolver messageResolver;

    /**
     * The sorted chain of {@link PatchnotesOutcomeResultExtractor}s applied against outcomes received.
     */
    private final List<PatchnotesOutcomeResultExtractor> resultExtractors;

    /**
     * Constructs a {@link ReceiptController}.
     *
     * @param receiptService   the business logic service.
     * @param messageResolver  the validation errors message resolver.
     * @param resultExtractors stereotype-annotated mappers.
     */
    public ReceiptController(ReceiptService receiptService,
                             MessageResolver messageResolver,
                             List<PatchnotesOutcomeResultExtractor> resultExtractors) {

        this.receiptService = receiptService;
        this.messageResolver = messageResolver;

        resultExtractors.sort(PatchnotesOutcomeResultExtractor::compareTo);

        this.resultExtractors = List.copyOf(resultExtractors);
    }

    @PostMapping
    public ResponseEntity<GenericResult> saveAllReceipts(
            @AuthenticationPrincipal DelegatingCustomerDetails principal,
            @Valid @RequestBody Patchnotes patchnotes, BindingResult errors, Locale locale) {

        final var wrapper = new PatchnotesWrapper(principal.getDelegate(), patchnotes);

        boolean isBodyValid = !errors.hasErrors();
        if (!isBodyValid) {
            String message = this.messageResolver.onIllegalPatchnotes(errors, locale);
            return new ResponseEntity<>(GenericResult.failed(message), HttpStatus.BAD_REQUEST);
        }

        return asResponseEntity(this.receiptService.saveAllReceipts(wrapper), locale);
    }

    /**
     * Convert the given patchnotes outcome into a localized response entity.
     *
     * @param outcome to be converted & localized.
     * @param locale  the {@link Locale} to use for message resolving.
     * @return the patchnotes outcome as a {@link ResponseEntity}.
     */
    private ResponseEntity<GenericResult> asResponseEntity(PatchnotesOutcome outcome, Locale locale) {
        for (var extractor : this.resultExtractors) {
            GenericResult result = extractor.extract(outcome, locale);

            if (result != null) {
                return new ResponseEntity<>(result, result.isFailure() ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // never happens
    }
}
