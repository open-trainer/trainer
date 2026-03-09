package org.opentrainer.garmin.exception;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 26-12-2020
 */
public class ActivityNotFoundGarminConnectException extends GarminConnectException {
    public ActivityNotFoundGarminConnectException(String message) {
        super(message);
    }
}
