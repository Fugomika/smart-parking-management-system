-- Smart Parking Management System (SPMS)
-- Schema Database MySQL

CREATE DATABASE IF NOT EXISTS spms_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE spms_db;

-- Kendaraan yang sedang aktif parkir
CREATE TABLE IF NOT EXISTS active_tickets (
    ticket_id     VARCHAR(50)  PRIMARY KEY,
    license_plate VARCHAR(20)  NOT NULL,
    vehicle_type  VARCHAR(10)  NOT NULL,     -- 'Mobil' atau 'Motor'
    slot_number   INT          NOT NULL,
    entry_time    BIGINT       NOT NULL      -- System.currentTimeMillis()
);

-- Riwayat kendaraan yang sudah keluar
CREATE TABLE IF NOT EXISTS parking_history (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id     VARCHAR(50)  NOT NULL,
    vehicle       VARCHAR(50)  NOT NULL,     -- contoh: "Mobil [B1234XY]"
    slot_number   INT          NOT NULL,
    entry_time    VARCHAR(30)  NOT NULL,     -- format dd/MM/yyyy HH:mm:ss
    exit_time     VARCHAR(30)  NOT NULL,
    hours         INT          NOT NULL,
    fee           DOUBLE       NOT NULL
);
