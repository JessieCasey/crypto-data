package com.crypto.data.exception;

/**
 * The EntityNotFoundException exception extends RuntimeException.
 * GlobalHandlerException handles this exception. {@link GlobalHandlerException}
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
