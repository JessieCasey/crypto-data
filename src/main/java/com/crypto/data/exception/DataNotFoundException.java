package com.crypto.data.exception;

/**
 * The DataNotEnoughException exception extends RuntimeException.
 * GlobalHandlerException handles this exception. {@link GlobalHandlerException}
 */

public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException(String message) {
        super(message);
    }
}
