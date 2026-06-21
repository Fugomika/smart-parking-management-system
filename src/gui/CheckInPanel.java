package gui;

import model.*;
import system.ParkingSystem;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class CheckInPanel extends JPanel {
    private final ParkingSystem system;
    private JTextField txtPlat;
    private JRadioButton rbMobil, rbMotor;
    private JTextArea txtOutput;

    public CheckInPanel(ParkingSystem system) {
        this.system = system;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setBackground(Color.WHITE);

        // ---- Form ----
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(createSectionBorder("Form Kendaraan Masuk", new Color(21, 101, 192)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(makeLabel("Plat Nomor :"), gbc);
        gbc.gridx = 1;
        txtPlat = new JTextField(18);
        styleTextField(txtPlat);
        formPanel.add(txtPlat, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(makeLabel("Jenis Kendaraan :"), gbc);
        gbc.gridx = 1;
        JPanel jenisPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jenisPanel.setBackground(Color.WHITE);
        rbMobil = makeRadio("Mobil  (Rp 5.000/jam)");
        rbMotor = makeRadio("Motor  (Rp 2.000/jam)");
        rbMobil.setSelected(true);
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbMobil);
        bg.add(rbMotor);
        jenisPanel.add(rbMobil);
        jenisPanel.add(Box.createHorizontalStrut(20));
        jenisPanel.add(rbMotor);
        formPanel.add(jenisPanel, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        JButton btnMasuk = makeButton("  PROSES MASUK  ", new Color(21, 101, 192));
        formPanel.add(btnMasuk, gbc);

        // ---- Output ----
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBackground(Color.WHITE);
        outputPanel.setBorder(createSectionBorder("Tiket Parkir", new Color(46, 125, 50)));
        txtOutput = new JTextArea(11, 40);
        txtOutput.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtOutput.setEditable(false);
        txtOutput.setBackground(new Color(245, 250, 245));
        txtOutput.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        txtOutput.setText("Silakan isi form di atas dan klik PROSES MASUK.");
        outputPanel.add(new JScrollPane(txtOutput), BorderLayout.CENTER);

        add(formPanel, BorderLayout.NORTH);
        add(outputPanel, BorderLayout.CENTER);

        btnMasuk.addActionListener(e -> prosesmasuk());
        txtPlat.addActionListener(e -> prosesmasuk());
    }

    private void prosesmasuk() {
        String plat = txtPlat.getText().trim().toUpperCase();
        if (plat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Plat nomor tidak boleh kosong!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Vehicle vehicle = rbMobil.isSelected() ? new Car(plat) : new Motorcycle(plat);
        ParkingTicket ticket = system.checkIn(vehicle);

        if (ticket == null) {
            String msg = system.getAvailableSlotCount() == 0
                ? "Semua slot parkir penuh! Tidak ada slot tersedia."
                : "Kendaraan dengan plat [" + plat + "] sudah terdaftar di parkiran.";
            JOptionPane.showMessageDialog(this, msg, "Gagal Check-In", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ticket.printTicketDetails();

        String info = String.format(
            "============================\n" +
            "      TIKET PARKIR SPMS     \n" +
            "============================\n" +
            "ID Tiket    : %s\n" +
            "Kendaraan   : %s\n" +
            "Slot Parkir : %d\n" +
            "Waktu Masuk : %s\n" +
            "============================\n\n" +
            "* Simpan ID Tiket untuk checkout *\n\n" +
            "Slot tersedia : %d / %d",
            ticket.getTicketId(),
            ticket.getVehicle(),
            ticket.getSlotNumber(),
            ticket.getFormattedEntryTime(),
            system.getAvailableSlotCount(),
            system.getSlots().size()
        );
        txtOutput.setText(info);
        txtPlat.setText("");
        txtPlat.requestFocus();
    }

    // ---- Helpers ----
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
        tf.setPreferredSize(new Dimension(220, 30));
    }

    private JRadioButton makeRadio(String text) {
        JRadioButton rb = new JRadioButton(text);
        rb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rb.setBackground(Color.WHITE);
        return rb;
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(220, 36));
        return btn;
    }
}
