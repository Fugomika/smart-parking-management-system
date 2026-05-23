package com.spms.model;

public class Car extends Vehicle {
    public static final double RATE_PER_HOUR = 5000;

    public Car(String licensePlate) {
        super(licensePlate, "Mobil");
    }

    @Override
    public double calculateParkingFee(int hours) {
        return hours * RATE_PER_HOUR;
    }
}
