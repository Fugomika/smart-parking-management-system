# Smart Parking Management System (SPMS)

Tugas Besar - Pemrograman Berorientasi Objek  
Semester Genap 2025/2026 | Program Studi S1 PJJ Informatika  
Universitas Telkom

---

## Anggota Kelompok 6

| Nama | NIM |
|------|-----|
| Arya Putra Nugroho | 103042310071 |
| Azzam Rafi Zafran | 103042400032 |
| Abdurrahman Farras Fadhila | 103042310070 |
| Chiqa Maharani | 103042400038 |
| Yuliana Mutia | 103042400041 |

---

## Deskripsi

SPMS adalah aplikasi berbasis Java yang mengelola parkir kendaraan secara digital, mencakup:
- Pencatatan kendaraan masuk dan keluar
- Manajemen slot parkir otomatis
- Perhitungan biaya parkir berdasarkan durasi
- Sistem pembayaran tunai
- Laporan riwayat dan total pendapatan

---

## Teknologi

- **Backend** : Java (OOP)
- **Frontend** : Java Swing
- **IDE** : NetBeans / IntelliJ IDEA

---

## Struktur Proyek

```
src/
└── com/spms/
    ├── Main.java                  # Entry point
    ├── model/
    │   ├── Vehicle.java           # Abstract class kendaraan
    │   ├── Car.java               # Subclass mobil (Rp 5.000/jam)
    │   ├── Motorcycle.java        # Subclass motor (Rp 2.000/jam)
    │   ├── ParkingSlot.java       # Manajemen slot parkir
    │   └── ParkingTicket.java     # Data tiket transaksi
    ├── payment/
    │   ├── PaymentMethod.java     # Interface pembayaran
    │   └── CashPayment.java       # Implementasi pembayaran tunai
    ├── system/
    │   └── ParkingSystem.java     # Controller utama (checkIn / checkOut)
    └── gui/
        ├── MainFrame.java         # Jendela utama
        ├── CheckInPanel.java      # Tab kendaraan masuk
        ├── CheckOutPanel.java     # Tab kendaraan keluar
        ├── SlotPanel.java         # Tab status slot
        └── ReportPanel.java       # Tab laporan
```

---

## Konsep OOP yang Digunakan

| Konsep | Implementasi |
|--------|-------------|
| **Abstraction** | `Vehicle` sebagai abstract class dengan method `calculateParkingFee()` |
| **Inheritance** | `Car` dan `Motorcycle` mewarisi `Vehicle` |
| **Polymorphism** | `calculateParkingFee()` di-override di `Car` dan `Motorcycle` |
| **Interface** | `PaymentMethod` diimplementasikan oleh `CashPayment` |
| **Encapsulation** | Atribut private/protected dengan getter/setter |

---

## Cara Menjalankan

### Prasyarat
- Java JDK 11 atau lebih baru
- NetBeans IDE atau Apache Ant

### Melalui NetBeans IDE
1. Buka **NetBeans IDE**.
2. Pilih menu **File** -> **Open Project**.
3. Cari dan pilih folder **SmartParking** ini.
4. Klik kanan pada nama project **SmartParking**, lalu pilih **Run** (atau tekan tombol **F6**).

### Melalui Command Line (Apache Ant)
1. Buka terminal atau Command Prompt di direktori utama proyek.
2. Jalankan perintah berikut untuk compile dan menjalankan program:
   ```bash
   ant run
   ```

---

## Fitur Aplikasi

### Kendaraan Masuk
- Input plat nomor
- Pilih jenis kendaraan (Mobil / Motor)
- Sistem menentukan slot parkir otomatis
- Cetak tiket parkir beserta ID tiket

### Kendaraan Keluar
- Cari kendaraan berdasarkan **Tiket ID** atau **Plat Nomor**
- Hitung durasi parkir (minimum 1 jam)
- Hitung biaya otomatis sesuai jenis kendaraan
- Proses pembayaran tunai dan tampilkan kembalian

### Status Slot
- Visualisasi grid semua slot parkir
- Warna **hijau** = kosong, **merah** = terisi
- Tampilkan plat kendaraan di slot yang terisi

### Laporan
- Riwayat seluruh transaksi parkir
- Total pendapatan kumulatif

---

## Tarif Parkir

| Jenis Kendaraan | Tarif |
|-----------------|-------|
| Mobil | Rp 5.000 / jam |
| Motor | Rp 2.000 / jam |

> Minimum parkir: **1 jam**
