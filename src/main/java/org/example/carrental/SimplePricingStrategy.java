package org.example.carrental;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class SimplePricingStrategy implements PricingStrategy {

    private final Map<CarType, BigDecimal> dailyRates;
    private final int discountThresholdDays;
    private final BigDecimal discountMultiplier;

    /**
     * @param dailyRates           base daily rate per car type
     * @param discountThresholdDays number of days after which discount applies
     * @param discountMultiplier   multiplier applied to the total price
     *                              e.g. 0.9 = 10% discount
     */
    public SimplePricingStrategy(Map<CarType, BigDecimal> dailyRates,
                                 int discountThresholdDays,
                                 BigDecimal discountMultiplier) {
        this.dailyRates = dailyRates;
        this.discountThresholdDays = discountThresholdDays;
        this.discountMultiplier = discountMultiplier;
    }

    @Override
    public BigDecimal calculatePrice(Reservation reservation) {
        BigDecimal rate = dailyRates.get(reservation.getCarType());
        if (rate == null) {
            throw new IllegalStateException("No daily rate defined for type " + reservation.getCarType());
        }

        long days = ChronoUnit.DAYS.between(
                reservation.getStartDateTime(),
                reservation.getEndDateTime()
        );

        if (days <= 0) {
            throw new IllegalArgumentException("Reservation duration must be at least 1 day");
        }

        BigDecimal basePrice = rate.multiply(BigDecimal.valueOf(days));

        if (days >= discountThresholdDays) {
            return basePrice.multiply(discountMultiplier);
        }

        return basePrice;
    }
}
