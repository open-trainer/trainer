package org.opentrainer.garmin.exception;

/**
 * Base exception for Garmin client errors
 */
public class GarminClientException extends RuntimeException {
    
    public GarminClientException(String message) {
        super(message);
    }

    public GarminClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
