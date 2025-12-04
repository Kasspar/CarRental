package org.example.carrental;

import java.math.BigDecimal;

public class PricedReservation {

    private final Reservation reservation;
    private final BigDecimal price;

    public PricedReservation(Reservation reservation, BigDecimal price) {
        this.reservation = reservation;
        this.price = price;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public BigDecimal getPrice() {
        return price;
    }

    // Convenience getters, so callers don't have to unwrap Reservation all the time

    public String getId() {
        return reservation.getId();
    }

    public CarType getCarType() {
        return reservation.getCarType();
    }

    @Override
    public String toString() {
        return "PricedReservation{" +
                "id='" + reservation.getId() + '\'' +
                ", type=" + reservation.getCarType() +
                ", start=" + reservation.getStartDateTime() +
                ", end=" + reservation.getEndDateTime() +
                ", price=" + price +
                '}';
    }
}
