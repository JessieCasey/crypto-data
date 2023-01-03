package com.crypto.data.exception;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.MethodNotAllowedException;

/**
 * GlobalHandlerException handles exceptions
 */

@Slf4j
@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        log.error("[handleEntityNotFoundException]': " + request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(new MessageResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), request));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        log.error("[handlePaymentDeclinedException]: " + request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(new MessageResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleMethodNotAllowedException(MethodNotAllowedException ex, WebRequest request) {
        log.error("[handleMethodNotAllowedException]: " + request.getDescription(false));
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .body(new MessageResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), ex.getMessage(), request));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleDataNotEnoughException(DataNotFoundException ex, WebRequest request) {
        log.error("[handleMethodNotAllowedException]: " + request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value())
                .body(new MessageResponse(HttpStatus.NO_CONTENT.value(), ex.getMessage(), request));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleValidationException(ValidationException ex, WebRequest request) {
        log.error("[handleMethodNotAllowedException]: " + request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(new MessageResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(Exception ex, WebRequest request) {
        log.error("[handleException]: " + request.getDescription(false));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(new MessageResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), request));
    }
}
