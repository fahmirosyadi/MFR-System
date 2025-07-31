package com.mfr.system.exception;

/**
 * This class represents a custom exception for business logic errors.
 * The exception message is intended to be returned to the user.
 */
public class BusinessException extends RuntimeException{

    public BusinessException(String message) {
        super(message);
    }
    
}
