package org.example.carrental;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a single car reservation.
 * A reservation is defined by:
 *  - car type
 *  - start date & time
 *  - end date & time
 * Reservations do NOT track the exact physical car; they only ensure that
 * capacity constraints per car type are respected.
 */
public class Reservation {

    private final String id;
    private final CarType carType;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    /**
     * Creates a reservation with a generated UUID.
     */
    public Reservation(CarType carType,
                       LocalDateTime startDateTime,
                       LocalDateTime endDateTime) {
        this(UUID.randomUUID().toString(), carType, startDateTime, endDateTime);
    }

    /**
     * Creates a reservation with a specific ID (mainly for testing).
     */
    public Reservation(String id,
                       CarType carType,
                       LocalDateTime startDateTime,
                       LocalDateTime endDateTime) {

        if (carType == null)
            throw new IllegalArgumentException("Car type must not be null");

        if (startDateTime == null || endDateTime == null)
            throw new IllegalArgumentException("Start and end date must not be null");

        if (!endDateTime.isAfter(startDateTime))
            throw new IllegalArgumentException("End date must be after start date");

        this.id = id;
        this.carType = carType;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * Checks whether this reservation overlaps with the given time range.
     *
     * Time ranges follow the rule:
     *     [start, end)
     * Meaning:
     *  - Reservations overlap if they share ANY time in common.
     *  - Back-to-back reservations are allowed.
     */
    public boolean overlaps(LocalDateTime start, LocalDateTime end) {
        return start.isBefore(this.endDateTime) && end.isAfter(this.startDateTime);
    }

    // Getters

    public String getId() {
        return id;
    }

    public CarType getCarType() {
        return carType;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    // Equality based on reservation ID only

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
