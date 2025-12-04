package org.example.carrental;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * In-memory implementation of the CarRentalService.
 *
 * Responsible for:
 *  - validating input
 *  - calculating reservation time windows
 *  - checking overlap with existing reservations
 *  - enforcing capacity limits
 *  - storing reservations in memory
 *
 * This implementation is perfect for:
 *  - unit testing
 *  - simulations
 *  - coding challenges
 *  - systems without a database
 */
public class InMemoryCarRentalService implements CarRentalService {

    private final CarInventory inventory;

    // Stores reservations grouped by car type
    private final Map<CarType, List<Reservation>> reservationsByType =
            new EnumMap<>(CarType.class);

    public InMemoryCarRentalService(CarInventory inventory) {
        this.inventory = inventory;
        for (CarType type : CarType.values()) {
            reservationsByType.put(type, new ArrayList<>());
        }
    }

    @Override
    public Reservation reserve(CarType type, LocalDateTime startDateTime, int days) {
        validateInput(type, startDateTime, days);

        LocalDateTime endDateTime = startDateTime
                .plusDays(days)
                .truncatedTo(ChronoUnit.MINUTES);

        // Count overlapping reservations
        long overlapping =
                reservationsByType.get(type).stream()
                        .filter(r -> r.overlaps(startDateTime, endDateTime))
                        .count();

        int capacity = inventory.getCapacity(type);

        if (overlapping >= capacity) {
            throw new NoAvailabilityException(
                    "No available " + type + " cars for the requested period.");
        }

        Reservation reservation = new Reservation(type, startDateTime, endDateTime);
        reservationsByType.get(type).add(reservation);

        return reservation;
    }

    @Override
    public void cancelReservation(String reservationId) {
        if (reservationId == null || reservationId.isBlank()) {
            throw new IllegalArgumentException("Reservation ID must not be null or blank");
        }

        boolean removed = false;

        // Search for the reservation across all car types and remove it
        for (CarType type : CarType.values()) {
            List<Reservation> list = reservationsByType.get(type);
            Iterator<Reservation> it = list.iterator();

            while (it.hasNext()) {
                Reservation r = it.next();
                if (reservationId.equals(r.getId())) {
                    it.remove();
                    removed = true;
                    break;
                }
            }

            if (removed) {
                break;
            }
        }

        if (!removed) {
            throw new IllegalArgumentException("Reservation not found: " + reservationId);
        }
    }

    @Override
    public List<Reservation> getAllReservations() {
        List<Reservation> all = new ArrayList<>();
        reservationsByType.values().forEach(all::addAll);
        return Collections.unmodifiableList(all);
    }

    /**
     * Validates input arguments for reservation creation.
     */
    private void validateInput(CarType type, LocalDateTime startDateTime, int days) {
        if (type == null)
            throw new IllegalArgumentException("Car type must not be null");

        if (startDateTime == null)
            throw new IllegalArgumentException("Start date must not be null");

        if (days <= 0)
            throw new IllegalArgumentException("Days must be greater than zero");
    }
}
