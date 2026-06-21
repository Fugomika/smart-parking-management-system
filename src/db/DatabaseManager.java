package db;

import model.*;
import system.ParkingSystem;

import java.sql.*;
import java.util.*;

public class DatabaseManager {

    private static final String URL  = "jdbc:mysql://localhost:3306/spms_db?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true";
    private static final String USER = "root";
    private static final String PASS = "";   // sesuaikan password MySQL kamu

    // ---- Connection ----

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // ---- Init tabel ----

    public static void initDatabase() {
        String createActive =
            "CREATE TABLE IF NOT EXISTS active_tickets (" +
            "  ticket_id     VARCHAR(50) PRIMARY KEY," +
            "  license_plate VARCHAR(20) NOT NULL," +
            "  vehicle_type  VARCHAR(10) NOT NULL," +
            "  slot_number   INT         NOT NULL," +
            "  entry_time    BIGINT      NOT NULL" +
            ")";

        String createHistory =
            "CREATE TABLE IF NOT EXISTS parking_history (" +
            "  id          INT AUTO_INCREMENT PRIMARY KEY," +
            "  ticket_id   VARCHAR(50) NOT NULL," +
            "  vehicle     VARCHAR(50) NOT NULL," +
            "  slot_number INT         NOT NULL," +
            "  entry_time  VARCHAR(30) NOT NULL," +
            "  exit_time   VARCHAR(30) NOT NULL," +
            "  hours       INT         NOT NULL," +
            "  fee         DOUBLE      NOT NULL" +
            ")";

        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            st.execute(createActive);
            st.execute(createHistory);
        } catch (SQLException e) {
            throw new RuntimeException("Gagal inisialisasi database: " + e.getMessage(), e);
        }
    }

    // ---- Active Tickets ----

    public static void saveActiveTicket(ParkingTicket ticket) {
        String sql = "INSERT INTO active_tickets (ticket_id, license_plate, vehicle_type, slot_number, entry_time) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ticket.getTicketId());
            ps.setString(2, ticket.getVehicle().getLicensePlate());
            ps.setString(3, ticket.getVehicle().getVehicleType());
            ps.setInt   (4, ticket.getSlotNumber());
            ps.setLong  (5, ticket.getEntryTime());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menyimpan tiket aktif: " + e.getMessage(), e);
        }
    }

    public static void removeActiveTicket(String ticketId) {
        String sql = "DELETE FROM active_tickets WHERE ticket_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ticketId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menghapus tiket aktif: " + e.getMessage(), e);
        }
    }

    public static Map<String, ParkingTicket> loadActiveTickets() {
        Map<String, ParkingTicket> map = new LinkedHashMap<>();
        String sql = "SELECT ticket_id, license_plate, vehicle_type, slot_number, entry_time FROM active_tickets ORDER BY entry_time";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String  ticketId    = rs.getString("ticket_id");
                String  plate       = rs.getString("license_plate");
                String  type        = rs.getString("vehicle_type");
                int     slot        = rs.getInt   ("slot_number");
                long    entryTime   = rs.getLong  ("entry_time");

                Vehicle vehicle = type.equalsIgnoreCase("Mobil") ? new Car(plate) : new Motorcycle(plate);
                ParkingTicket ticket = new ParkingTicket(vehicle, slot, ticketId, entryTime);
                map.put(ticketId, ticket);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal memuat tiket aktif: " + e.getMessage(), e);
        }
        return map;
    }

    // ---- Parking History ----

    public static void saveParkingRecord(String ticketId, String vehicle, int slotNumber,
                                          String entryTime, String exitTime, int hours, double fee) {
        String sql = "INSERT INTO parking_history (ticket_id, vehicle, slot_number, entry_time, exit_time, hours, fee) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ticketId);
            ps.setString(2, vehicle);
            ps.setInt   (3, slotNumber);
            ps.setString(4, entryTime);
            ps.setString(5, exitTime);
            ps.setInt   (6, hours);
            ps.setDouble(7, fee);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menyimpan riwayat: " + e.getMessage(), e);
        }
    }

    public static List<ParkingSystem.ParkingRecord> loadHistory() {
        List<ParkingSystem.ParkingRecord> list = new ArrayList<>();
        String sql = "SELECT ticket_id, vehicle, slot_number, entry_time, exit_time, hours, fee FROM parking_history ORDER BY id";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ParkingSystem.ParkingRecord(
                    rs.getString("ticket_id"),
                    rs.getString("vehicle"),
                    rs.getInt   ("slot_number"),
                    rs.getString("entry_time"),
                    rs.getString("exit_time"),
                    rs.getInt   ("hours"),
                    rs.getDouble("fee")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal memuat riwayat: " + e.getMessage(), e);
        }
        return list;
    }

    public static double loadTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(fee), 0) FROM parking_history";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            throw new RuntimeException("Gagal memuat total pendapatan: " + e.getMessage(), e);
        }
        return 0;
    }
}
