package org.example.carrental;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PricedCarRentalService {

    private final CarRentalService carRentalService;
    private final PricingStrategy pricingStrategy;

    public PricedCarRentalService(CarRentalService carRentalService,
                                  PricingStrategy pricingStrategy) {
        this.carRentalService = carRentalService;
        this.pricingStrategy = pricingStrategy;
    }

    /**
     * Reserves a car and immediately calculates the price.
     */
    public PricedReservation reserveWithPrice(CarType type,
                                              LocalDateTime startDateTime,
                                              int days) {
        Reservation reservation = carRentalService.reserve(type, startDateTime, days);
        BigDecimal price = pricingStrategy.calculatePrice(reservation);
        return new PricedReservation(reservation, price);
    }
}
