package org.example.carrental;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Defines the contract for a car rental reservation service.
 *
 * This abstraction makes it possible to:
 *  - test business logic independently of storage
 *  - replace the in-memory implementation with a DB-backed one later
 */
public interface CarRentalService {

    /**
     * Attempts to reserve a car of the given type starting at the given date/time
     * for the specified number of days.
     *
     * @throws IllegalArgumentException if input is invalid
     * @throws NoAvailabilityException  if no cars are available
     */
    Reservation reserve(CarType type, LocalDateTime startDateTime, int days);

    /**
     * Cancels an existing reservation by its ID.
     *
     * @param reservationId the ID of the reservation to cancel
     * @throws IllegalArgumentException if the ID is null/blank or not found
     */
    void cancelReservation(String reservationId);

    /**
     * Returns all reservations (useful for debugging and testing).
     */
    List<Reservation> getAllReservations();
}
