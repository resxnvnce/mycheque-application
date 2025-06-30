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
import com.mycheque.service.wrapper.AuthorizedWrapper;
import com.mycheque.service.wrapper.PatchnotesOutcome;

import com.mycheque.controller.i18n.MessageResolver;
import com.mycheque.controller.i18n.PatchnotesOutcomeResultExtractor;

import com.mycheque.datatransfer.query.Patchnotes;
import com.mycheque.datatransfer.query.ReceiptQuery;
import com.mycheque.datatransfer.result.GenericResult;
import com.mycheque.datatransfer.result.RequestedReceipt;

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
    private final List<PatchnotesOutcomeResultExtractor> outcomeMappers;

    /**
     * Constructs a {@link ReceiptController}.
     *
     * @param receiptService   the business logic service.
     * @param messageResolver  the validation errors message resolver.
     * @param outcomeMappers stereotype-annotated mappers.
     */
    public ReceiptController(ReceiptService receiptService,
                             MessageResolver messageResolver,
                             List<PatchnotesOutcomeResultExtractor> outcomeMappers) {

        this.receiptService = receiptService;
        this.messageResolver = messageResolver;

        outcomeMappers.sort(PatchnotesOutcomeResultExtractor::compareTo);

        this.outcomeMappers = List.copyOf(outcomeMappers);
    }

    @PostMapping
    public ResponseEntity<GenericResult> saveAll(
            @AuthenticationPrincipal DelegatingCustomerDetails principal,
            @Valid @RequestBody Patchnotes patchnotes, BindingResult errors, Locale locale) {

        final var wrapper = new AuthorizedWrapper<>(principal.getDelegate(), patchnotes);

        boolean isBodyValid = !errors.hasErrors();
        if (!isBodyValid) {
            String message = this.messageResolver.onIllegalPatchnotes(errors, locale);
            return new ResponseEntity<>(GenericResult.failed(message), HttpStatus.BAD_REQUEST);
        }

        return asResponseEntity(this.receiptService.saveAll(wrapper), locale);
    }

    /**
     * Convert the given {@code PatchnotesOutcome} into a localized response entity.
     *
     * @param outcome to be converted & localized.
     * @param locale  the {@link Locale} to use for message resolving.
     * @return the patchnotes outcome as a {@link ResponseEntity}.
     */
    private ResponseEntity<GenericResult> asResponseEntity(PatchnotesOutcome outcome, Locale locale) {
        for (var mapper : this.outcomeMappers) {
            GenericResult result = mapper.extract(outcome, locale);

            if (result != null) {
                return new ResponseEntity<>(result, result.isFailure() ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // never happens
    }

    @PostMapping("search/")
    public ResponseEntity<GenericResult> findAll(
            @AuthenticationPrincipal DelegatingCustomerDetails principal,
            @Valid @RequestBody ReceiptQuery receiptQuery, BindingResult errors, Locale locale) {

        final var wrapper = new AuthorizedWrapper<>(principal.getDelegate(), receiptQuery);

        boolean isBodyValid = !errors.hasErrors();
        if (!isBodyValid) {
            String message = this.messageResolver.onIllegalReceiptQuery(errors, locale);
            return new ResponseEntity<>(GenericResult.failed(message), HttpStatus.BAD_REQUEST);
        }

        return asResponseEntity(this.receiptService.findAll(wrapper), locale);
    }

    /**
     * Convert the given {@code RequestedReceipt}s into a localized response entity.
     *
     * @param receipts to be converted & localized.
     * @param locale   the {@link Locale} to use for message resolving.
     * @return the requested receipt records as a {@link ResponseEntity}.
     */
    private ResponseEntity<GenericResult> asResponseEntity(List<RequestedReceipt> receipts, Locale locale) {
        String message = receipts.isEmpty() ? this.messageResolver.onEmptyResult(locale) : null;
        return new ResponseEntity<>(GenericResult.succeeded(message, receipts), HttpStatus.OK);
    }
}
