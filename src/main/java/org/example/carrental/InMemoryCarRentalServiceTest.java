package org.example.carrental;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for InMemoryCarRentalService.
 *
 * These tests validate core business rules such as:
 *  - capacity limits
 *  - time-range overlap logic
 *  - back-to-back reservation handling
 *  - input validation
 *  - independence of capacities per car type
 */
class InMemoryCarRentalServiceTest {

    private CarRentalService service;

    @BeforeEach
    void setUp() {
        // Initialize capacities for each car type
        Map<CarType, Integer> capacities = new EnumMap<>(CarType.class);
        capacities.put(CarType.SEDAN, 2);
        capacities.put(CarType.SUV, 1);
        capacities.put(CarType.VAN, 1);

        // Create inventory and service for testing
        CarInventory inventory = new CarInventory(capacities);
        service = new InMemoryCarRentalService(inventory);
    }

    @Test
    void shouldCreateReservationWhenCarsAreAvailable() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 10, 10, 0);

        // Attempt a valid reservation
        Reservation reservation = service.reserve(CarType.SEDAN, start, 3);

        // Validate returned reservation properties
        assertNotNull(reservation.getId());
        assertEquals(CarType.SEDAN, reservation.getCarType());
        assertEquals(start, reservation.getStartDateTime());
        assertEquals(start.plusDays(3), reservation.getEndDateTime());
    }

    @Test
    void shouldNotAllowMoreReservationsThanCapacityForOverlappingPeriod() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 10, 10, 0);

        // Sedan capacity = 2 → create 2 overlapping reservations
        service.reserve(CarType.SEDAN, start, 3);
        service.reserve(CarType.SEDAN, start.plusHours(1), 2);

        // Third reservation overlaps and should exceed capacity → expect exception
        assertThrows(NoAvailabilityException.class,
                () -> service.reserve(CarType.SEDAN, start.plusDays(1), 1));
    }

    @Test
    void shouldAllowBackToBackReservations() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 10, 10, 0);

        // First reservation: from Jan 10, 10:00 to Jan 12, 10:00
        service.reserve(CarType.SUV, start, 2);

        // Second reservation starts exactly when the previous one ends → no overlap
        Reservation second = service.reserve(CarType.SUV, start.plusDays(2), 1);

        assertNotNull(second);
    }

    @Test
    void shouldValidateInput() {
        LocalDateTime now = LocalDateTime.now();

        // Null car type should trigger validation
        assertThrows(IllegalArgumentException.class,
                () -> service.reserve(null, now, 1));

        // Null start date should trigger validation
        assertThrows(IllegalArgumentException.class,
                () -> service.reserve(CarType.SEDAN, null, 1));

        // Number of days must be > 0
        assertThrows(IllegalArgumentException.class,
                () -> service.reserve(CarType.SEDAN, now, 0));
    }

    @Test
    void differentCarTypesDoNotShareCapacity() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 10, 10, 0);

        // SUV has capacity = 1, but it does not affect Sedan capacity
        service.reserve(CarType.SUV, start, 2);
        service.reserve(CarType.SEDAN, start, 2); // should succeed

        // Two independent reservations should exist
        assertEquals(2, service.getAllReservations().size());
    }
}