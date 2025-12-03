package org.example.carrental;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Holds information about how many cars of each type exist in the fleet.
 *
 * Example:
 *   SEDAN -> 10
 *   SUV   -> 5
 *   VAN   -> 3
 *
 * This class performs only basic validation and storage.
 * Capacity enforcement is performed by the service layer.
 */
public class CarInventory {

    private final Map<CarType, Integer> capacities = new EnumMap<>(CarType.class);

    public CarInventory(Map<CarType, Integer> capacities) {
        if (capacities == null || capacities.isEmpty())
            throw new IllegalArgumentException("Capacities map must not be null or empty");

        for (CarType type : CarType.values()) {
            Integer value = capacities.get(type);
            if (value == null || value < 0) {
                throw new IllegalArgumentException("Capacity for " + type + " must be non-negative");
            }
            this.capacities.put(type, value);
        }
    }

    /**
     * Returns the maximum number of cars for the given type.
     */
    public int getCapacity(CarType type) {
        return capacities.getOrDefault(type, 0);
    }

    /**
     * Returns an immutable view of all capacities.
     */
    public Map<CarType, Integer> getCapacities() {
        return Collections.unmodifiableMap(capacities);
    }
}
