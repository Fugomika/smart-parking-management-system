package com.spms.system;

import com.spms.db.DatabaseManager;
import com.spms.model.*;
import com.spms.payment.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class ParkingSystem {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private List<ParkingSlot> slots;
    private Map<String, ParkingTicket> activeTickets;
    private List<ParkingRecord> history;
    private double totalRevenue;

    public ParkingSystem(int totalSlots) {
        slots = new ArrayList<>();
        activeTickets = new LinkedHashMap<>();
        history = new ArrayList<>();
        totalRevenue = 0;

        for (int i = 1; i <= totalSlots; i++) {
            slots.add(new ParkingSlot(i));
        }

        DatabaseManager.initDatabase();
        loadFromDatabase();
    }

    private void loadFromDatabase() {
        // Muat tiket aktif → tandai slot yang terisi
        activeTickets = DatabaseManager.loadActiveTickets();
        for (ParkingTicket ticket : activeTickets.values()) {
            for (ParkingSlot slot : slots) {
                if (slot.getSlotNumber() == ticket.getSlotNumber()) {
                    slot.assignVehicle(ticket.getVehicle());
                    break;
                }
            }
        }

        // Muat riwayat dan total pendapatan
        history = DatabaseManager.loadHistory();
        totalRevenue = DatabaseManager.loadTotalRevenue();
    }

    public ParkingTicket checkIn(Vehicle vehicle) {
        for (ParkingTicket t : activeTickets.values()) {
            if (t.getVehicle().getLicensePlate().equalsIgnoreCase(vehicle.getLicensePlate())) {
                return null;
            }
        }
        ParkingSlot slot = findAvailableSlot();
        if (slot == null) return null;

        slot.assignVehicle(vehicle);
        ParkingTicket ticket = new ParkingTicket(vehicle, slot.getSlotNumber());
        activeTickets.put(ticket.getTicketId(), ticket);

        DatabaseManager.saveActiveTicket(ticket);

        return ticket;
    }

    public CheckOutResult checkOut(String ticketId, PaymentMethod method) {
        ParkingTicket ticket = activeTickets.get(ticketId);
        if (ticket == null) return null;

        long now = System.currentTimeMillis();
        long durationMs = now - ticket.getEntryTime();
        int hours = (int) Math.ceil(durationMs / (1000.0 * 60 * 60));
        if (hours < 1) hours = 1;

        double fee = ticket.getVehicle().calculateParkingFee(hours);
        method.processPayment(fee);

        for (ParkingSlot s : slots) {
            if (s.getSlotNumber() == ticket.getSlotNumber()) {
                s.releaseSlot();
                break;
            }
        }

        totalRevenue += fee;
        ParkingRecord record = new ParkingRecord(
            ticket.getTicketId(),
            ticket.getVehicle().toString(),
            ticket.getSlotNumber(),
            ticket.getFormattedEntryTime(),
            SDF.format(new Date(now)),
            hours,
            fee
        );
        history.add(record);
        activeTickets.remove(ticketId);

        DatabaseManager.saveParkingRecord(
            record.ticketId, record.vehicle, record.slotNumber,
            record.entryTime, record.exitTime, record.hours, record.fee
        );
        DatabaseManager.removeActiveTicket(ticketId);

        return new CheckOutResult(ticket, hours, fee, method);
    }

    public ParkingTicket findTicketByLicensePlate(String plate) {
        for (ParkingTicket t : activeTickets.values()) {
            if (t.getVehicle().getLicensePlate().equalsIgnoreCase(plate)) return t;
        }
        return null;
    }

    public int estimateHours(ParkingTicket ticket) {
        long durationMs = System.currentTimeMillis() - ticket.getEntryTime();
        int hours = (int) Math.ceil(durationMs / (1000.0 * 60 * 60));
        return Math.max(hours, 1);
    }

    private ParkingSlot findAvailableSlot() {
        for (ParkingSlot s : slots) {
            if (s.isAvailable()) return s;
        }
        return null;
    }

    public List<ParkingSlot> getSlots()                          { return slots; }
    public Map<String, ParkingTicket> getActiveTickets()         { return activeTickets; }
    public List<ParkingRecord> getHistory()                      { return history; }
    public double getTotalRevenue()                              { return totalRevenue; }

    public int getAvailableSlotCount() {
        int c = 0;
        for (ParkingSlot s : slots) if (s.isAvailable()) c++;
        return c;
    }

    public int getOccupiedSlotCount() {
        return slots.size() - getAvailableSlotCount();
    }

    // ---- Inner result/record classes ----

    public static class CheckOutResult {
        public final ParkingTicket ticket;
        public final int hours;
        public final double fee;
        public final PaymentMethod paymentMethod;

        public CheckOutResult(ParkingTicket ticket, int hours, double fee, PaymentMethod paymentMethod) {
            this.ticket = ticket;
            this.hours = hours;
            this.fee = fee;
            this.paymentMethod = paymentMethod;
        }
    }

    public static class ParkingRecord {
        public final String ticketId;
        public final String vehicle;
        public final int slotNumber;
        public final String entryTime;
        public final String exitTime;
        public final int hours;
        public final double fee;

        public ParkingRecord(String ticketId, String vehicle, int slotNumber,
                             String entryTime, String exitTime, int hours, double fee) {
            this.ticketId = ticketId;
            this.vehicle = vehicle;
            this.slotNumber = slotNumber;
            this.entryTime = entryTime;
            this.exitTime = exitTime;
            this.hours = hours;
            this.fee = fee;
        }
    }
}
