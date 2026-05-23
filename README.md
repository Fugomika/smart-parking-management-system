# Smart Parking Management System (SPMS)

Sistem Manajemen Parkir Pintar berbasis desktop yang dibangun menggunakan **Java Swing (GUI)** dengan menerapkan prinsip Object-Oriented Programming (OOP). Proyek ini dirancang untuk mempermudah pemantauan slot parkir, proses check-in/check-out kendaraan, perhitungan tarif parkir otomatis berdasarkan jenis kendaraan dan durasi, serta pelaporan pendapatan.

---

## 🚀 Fitur Utama

- **Real-time Slot Visualizer**: Visualisasi slot parkir interaktif (Warna hijau menunjukkan slot tersedia, warna merah menunjukkan slot terisi).
- **Manajemen Kendaraan (Mobil & Motor)**: Pengelompokan jenis kendaraan dengan tarif parkir yang disesuaikan secara dinamis.
- **Proses Check-In & Check-Out Cepat**:
  - **Check-In**: Menghasilkan tiket unik dengan ID otomatis setelah mengalokasikan slot parkir yang kosong.
  - **Check-Out**: Mencari tiket berdasarkan ID Tiket atau Nomor Plat Kendaraan, menghitung durasi parkir (bulat ke atas per jam), menghitung tarif, dan memproses pembayaran.
- **Metode Pembayaran**: Struktur kode yang modular untuk metode pembayaran (saat ini mendukung pembayaran tunai/Cash).
- **Laporan & Riwayat**: Dashboard laporan yang menampilkan riwayat lengkap kendaraan masuk-keluar beserta total pendapatan (revenue) yang diperoleh secara real-time.

---

## 🛠️ Arsitektur Proyek (OOP & Desain)

Proyek ini dirancang menggunakan arsitektur MVC sederhana berbasis OOP:
- **`com.spms.model`**: Berisi objek data seperti `Vehicle` (dan sub-class-nya `Car` dan `Motorcycle`), `ParkingSlot`, dan `ParkingTicket`.
- **`com.spms.payment`**: Mengimplementasikan interface `PaymentMethod` (misalnya `CashPayment`) untuk menangani proses pembayaran secara fleksibel.
- **`com.spms.gui`**: Komponen visual utama menggunakan Java Swing (`MainFrame`, `CheckInPanel`, `CheckOutPanel`, `SlotPanel`, `ReportPanel`).
- **`com.spms.system`**: Engine utama (`ParkingSystem`) yang menangani alokasi memori, daftar slot aktif, penghitungan tarif, dan pencatatan riwayat transaksi.

---

## 💻 Cara Menjalankan Proyek

### 1. Menggunakan NetBeans IDE (Sangat Direkomendasikan)
1. Buka **NetBeans IDE**.
2. Pilih **File** -> **Open Project**.
3. Arahkan ke folder proyek ini (`SmartParking`).
4. Klik kanan pada proyek **SmartParking** di panel Projects, kemudian pilih **Run**.

### 2. Menggunakan Command Line (CLI) dengan Apache Ant
Pastikan Anda sudah menginstal JDK dan Apache Ant di sistem Anda.
1. Buka terminal/command prompt pada direktori utama proyek.
2. Jalankan perintah berikut untuk mengompilasi dan menjalankan program:
   ```bash
   ant run
   ```

---

## 📄 Struktur Folder Proyek
```text
SmartParking/
├── build.xml               # Konfigurasi build Apache Ant
├── manifest.mf             # Manifest file untuk JAR bundling
├── nbproject/              # Konfigurasi proyek NetBeans
├── src/                    # Source code Java
│   └── com/
│       └── spms/
│           ├── Main.java              # Entry point utama program
│           ├── gui/                   # Paket untuk komponen UI Swing
│           ├── model/                 # Kelas representasi entitas (OOP)
│           ├── payment/               # Manajemen metode pembayaran
│           └── system/                # Logika sistem parkir utama
└── .gitignore              # Mengabaikan folder build/ & file konfigurasi privat
```
