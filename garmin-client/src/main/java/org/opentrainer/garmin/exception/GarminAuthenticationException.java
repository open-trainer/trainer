package org.opentrainer.garmin.exception;

/**
 * Exception for authentication-related errors
 */
public class GarminAuthenticationException extends GarminClientException {
    
    public GarminAuthenticationException(String message) {
        super(message);
    }

    public GarminAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
