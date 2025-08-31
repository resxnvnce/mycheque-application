package com.mycheque.controller.advice;

import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mycheque.controller.CustomerController;
import com.mycheque.controller.i18n.MessageResolver;

import com.mycheque.datatransfer.result.GenericResult;

import com.mycheque.service.exception.TokenAlreadyInUseException;

/**
 * A global {@code Exception} handler class for the {@link CustomerController}.
 *
 * @author resxnvnce
 * @see RestControllerAdvice
 */
@RestControllerAdvice(assignableTypes = CustomerController.class)
public class CustomerControllerAdvice {

    private final MessageResolver messageResolver;

    /**
     * Constructs a {@code CustomerControllerAdvice}.
     *
     * @param messageResolver a supplier of properly localized & interpolated response messages.
     */
    public CustomerControllerAdvice(MessageResolver messageResolver) {
        this.messageResolver = messageResolver;
    }

    /**
     * Handles a {@code TokenAlreadyInUseException} and
     * returns a {@link ResponseEntity} with a suitable message.
     *
     * @param exception an {@code Exception} handled by this method.
     * @param locale    the {@link Locale} in which to do the message lookup.
     * @return a response entity with the message explaining what went wrong.
     */
    @ExceptionHandler
    public ResponseEntity<GenericResult> handleException(TokenAlreadyInUseException exception, Locale locale) {
        String message = this.messageResolver.onException(exception, locale);
        return new ResponseEntity<>(GenericResult.failed(message), HttpStatus.BAD_REQUEST);
    }
}
