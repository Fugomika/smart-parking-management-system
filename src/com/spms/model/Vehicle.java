package com.spms.model;

public abstract class Vehicle {
    protected String licensePlate;
    protected String vehicleType;

    public Vehicle(String licensePlate, String vehicleType) {
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
    }

    public abstract double calculateParkingFee(int hours);

    public String getLicensePlate() { return licensePlate; }
    public String getVehicleType() { return vehicleType; }

    @Override
    public String toString() {
        return vehicleType + " [" + licensePlate + "]";
    }
}
