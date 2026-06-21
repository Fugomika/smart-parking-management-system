package gui;

import model.ParkingTicket;
import payment.CashPayment;
import system.ParkingSystem;
import system.ParkingSystem.CheckOutResult;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class CheckOutPanel extends JPanel {
    private final ParkingSystem system;
    private JTextField txtCari, txtCash;
    private JTextArea txtInfo, txtResult;
    private JButton btnCari, btnProses;
    private ParkingTicket currentTicket;
    private double currentFee;

    private static final NumberFormat IDR = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"));

    public CheckOutPanel(ParkingSystem system) {
        this.system = system;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);

        // ---- Search ----
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(createSectionBorder("Cari Kendaraan", new Color(21, 101, 192)));
        searchPanel.add(makeLabel("Tiket ID / Plat Nomor :"));
        txtCari = new JTextField(20);
        styleTextField(txtCari);
        searchPanel.add(txtCari);
        btnCari = makeButton("  CARI  ", new Color(21, 101, 192));
        searchPanel.add(btnCari);

        // ---- Ticket info ----
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(createSectionBorder("Info Kendaraan", new Color(255, 152, 0)));
        txtInfo = new JTextArea(6, 40);
        txtInfo.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtInfo.setEditable(false);
        txtInfo.setBackground(new Color(255, 253, 240));
        txtInfo.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        txtInfo.setText("Masukkan Tiket ID atau Plat Nomor, lalu klik CARI.");
        infoPanel.add(new JScrollPane(txtInfo), BorderLayout.CENTER);

        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(infoPanel, BorderLayout.CENTER);

        // ---- Payment ----
        JPanel payPanel = new JPanel(new GridBagLayout());
        payPanel.setBackground(Color.WHITE);
        payPanel.setBorder(createSectionBorder("Pembayaran Tunai", new Color(46, 125, 50)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        payPanel.add(makeLabel("Masukkan Uang (Rp) :"), gbc);
        gbc.gridx = 1;
        txtCash = new JTextField(15);
        styleTextField(txtCash);
        payPanel.add(txtCash, gbc);

        gbc.gridx = 2;
        btnProses = makeButton("  PROSES KELUAR  ", new Color(46, 125, 50));
        btnProses.setEnabled(false);
        payPanel.add(btnProses, gbc);

        // ---- Result ----
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(Color.WHITE);
        resultPanel.setBorder(createSectionBorder("Hasil Pembayaran", new Color(136, 14, 79)));
        txtResult = new JTextArea(5, 40);
        txtResult.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtResult.setEditable(false);
        txtResult.setBackground(new Color(252, 245, 252));
        txtResult.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        resultPanel.add(new JScrollPane(txtResult), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(payPanel);
        bottomPanel.add(resultPanel);

        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);

        btnCari.addActionListener(e -> cariKendaraan());
        txtCari.addActionListener(e -> cariKendaraan());
        btnProses.addActionListener(e -> prosesKeluar());
    }

    private void cariKendaraan() {
        String input = txtCari.getText().trim().toUpperCase();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan Tiket ID atau Plat Nomor!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ParkingTicket ticket;
        if (input.startsWith("TKT-")) {
            ticket = system.getActiveTickets().get(input);
        } else {
            ticket = system.findTicketByLicensePlate(input);
        }

        if (ticket == null) {
            txtInfo.setText("Kendaraan / Tiket tidak ditemukan di parkiran aktif.");
            currentTicket = null;
            btnProses.setEnabled(false);
            return;
        }

        currentTicket = ticket;
        int estHours = system.estimateHours(ticket);
        currentFee = ticket.getVehicle().calculateParkingFee(estHours);

        txtInfo.setText(String.format(
            "ID Tiket    : %s\n" +
            "Kendaraan   : %s\n" +
            "Slot Parkir : %d\n" +
            "Waktu Masuk : %s\n" +
            "Durasi      : %d jam (minimum 1 jam)\n" +
            "Biaya Parkir: Rp %s",
            ticket.getTicketId(),
            ticket.getVehicle(),
            ticket.getSlotNumber(),
            ticket.getFormattedEntryTime(),
            estHours,
            IDR.format((long) currentFee)
        ));

        txtCash.setText("");
        txtResult.setText("");
        btnProses.setEnabled(true);
        txtCash.requestFocus();
    }

    private void prosesKeluar() {
        if (currentTicket == null) return;

        String cashStr = txtCash.getText().trim().replace(".", "").replace(",", "");
        double cashGiven;
        try {
            cashGiven = Double.parseDouble(cashStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Masukkan jumlah uang yang valid!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int finalHours = system.estimateHours(currentTicket);
        double finalFee = currentTicket.getVehicle().calculateParkingFee(finalHours);

        if (cashGiven < finalFee) {
            JOptionPane.showMessageDialog(this,
                String.format("Uang kurang! Biaya parkir: Rp %s\nUang diberikan: Rp %s",
                    IDR.format((long) finalFee), IDR.format((long) cashGiven)),
                "Pembayaran Kurang", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CashPayment payment = new CashPayment(cashGiven);
        CheckOutResult result = system.checkOut(currentTicket.getTicketId(), payment);

        if (result == null) {
            JOptionPane.showMessageDialog(this, "Gagal proses checkout!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double change = payment.getChange();
        txtResult.setText(String.format(
            "============================\n" +
            "     STRUK PEMBAYARAN SPMS  \n" +
            "============================\n" +
            "Kendaraan   : %s\n" +
            "Durasi      : %d jam\n" +
            "Biaya Parkir: Rp %s\n" +
            "Dibayar     : Rp %s\n" +
            "Kembalian   : Rp %s\n" +
            "============================\n" +
            "Terima kasih! Hati-hati di jalan.",
            result.ticket.getVehicle(),
            result.hours,
            IDR.format((long) result.fee),
            IDR.format((long) cashGiven),
            IDR.format((long) change)
        ));

        currentTicket = null;
        currentFee = 0;
        btnProses.setEnabled(false);
        txtCari.setText("");
        txtCash.setText("");
    }

    private TitledBorder createSectionBorder(String title, Color color) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(color, 1), title,
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 13), color);
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }

    private void styleTextField(JTextField tf) {
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setPreferredSize(new Dimension(200, 30));
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
