package org.example.carrental;

import java.math.BigDecimal;

public interface PricingStrategy {

    /**
     * Calculates the total price for the given reservation.
     *
     * @param reservation the reservation to price
     * @return total price as BigDecimal
     */
    BigDecimal calculatePrice(Reservation reservation);
}
