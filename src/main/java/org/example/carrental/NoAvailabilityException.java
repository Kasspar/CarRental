package org.example.carrental;



/**
 * Thrown when a reservation request cannot be fulfilled due to
 * insufficient available cars of the requested type.
 */
public class NoAvailabilityException extends RuntimeException {

    public NoAvailabilityException(String message) {
        super(message);
    }
}
