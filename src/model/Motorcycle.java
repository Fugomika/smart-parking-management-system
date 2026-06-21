package model;

public class Motorcycle extends Vehicle {
    public static final double RATE_PER_HOUR = 2000;

    public Motorcycle(String licensePlate) {
        super(licensePlate, "Motor");
    }

    @Override
    public double calculateParkingFee(int hours) {
        return hours * RATE_PER_HOUR;
    }
}
