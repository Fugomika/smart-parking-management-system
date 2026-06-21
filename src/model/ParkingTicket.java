package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ParkingTicket {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private String ticketId;
    private Vehicle vehicle;
    private long entryTime;
    private int slotNumber;

    public ParkingTicket(Vehicle vehicle, int slotNumber) {
        this.ticketId = "TKT-" + System.currentTimeMillis();
        this.vehicle = vehicle;
        this.entryTime = System.currentTimeMillis();
        this.slotNumber = slotNumber;
    }

    public void printTicketDetails() {
        System.out.println("============================");
        System.out.println("      TIKET PARKIR SPMS     ");
        System.out.println("============================");
        System.out.println("ID Tiket   : " + ticketId);
        System.out.println("Kendaraan  : " + vehicle);
        System.out.println("Slot       : " + slotNumber);
        System.out.println("Waktu Masuk: " + getFormattedEntryTime());
        System.out.println("============================");
    }

    public String getFormattedEntryTime() {
        return SDF.format(new Date(entryTime));
    }

    public String getTicketId() { return ticketId; }
    public Vehicle getVehicle() { return vehicle; }
    public long getEntryTime() { return entryTime; }
    public int getSlotNumber() { return slotNumber; }
}
