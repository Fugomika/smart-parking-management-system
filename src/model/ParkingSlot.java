package model;

public class ParkingSlot {
    private int slotNumber;
    private boolean isOccupied;
    private Vehicle currentVehicle;

    public ParkingSlot(int slotNumber) {
        this.slotNumber = slotNumber;
        this.isOccupied = false;
        this.currentVehicle = null;
    }

    public void assignVehicle(Vehicle vehicle) {
        this.currentVehicle = vehicle;
        this.isOccupied = true;
    }

    public void releaseSlot() {
        this.currentVehicle = null;
        this.isOccupied = false;
    }

    public boolean isAvailable() { return !isOccupied; }
    public boolean isOccupied() { return isOccupied; }
    public int getSlotNumber() { return slotNumber; }
    public Vehicle getCurrentVehicle() { return currentVehicle; }
}
