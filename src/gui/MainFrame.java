package com.spms.gui;

import com.spms.system.ParkingSystem;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final ParkingSystem system;
    private SlotPanel slotPanel;
    private ReportPanel reportPanel;

    public MainFrame(ParkingSystem system) {
        this.system = system;
        initComponents();
    }

    private void initComponents() {
        setTitle("Smart Parking Management System (SPMS)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 660);
        setLocationRelativeTo(null);
        setResizable(true);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(21, 101, 192));
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JPanel textBlock = new JPanel(new GridLayout(2, 1, 0, 2));
        textBlock.setOpaque(false);
        JLabel titleLbl = new JLabel("Smart Parking Management System");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 21));
        titleLbl.setForeground(Color.WHITE);
        JLabel subLbl = new JLabel("Kelompok 6  |  PBO Telkom University  |  Semester Genap 2025/2026");
        subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subLbl.setForeground(new Color(180, 210, 255));
        textBlock.add(titleLbl);
        textBlock.add(subLbl);

        JLabel slotLbl = new JLabel("10 Slot Parkir");
        slotLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        slotLbl.setForeground(Color.WHITE);

        header.add(textBlock, BorderLayout.WEST);
        header.add(slotLbl, BorderLayout.EAST);

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

        CheckInPanel checkInPanel = new CheckInPanel(system);
        CheckOutPanel checkOutPanel = new CheckOutPanel(system);
        slotPanel = new SlotPanel(system);
        reportPanel = new ReportPanel(system);

        tabs.addTab("  Kendaraan Masuk  ", checkInPanel);
        tabs.addTab("  Kendaraan Keluar  ", checkOutPanel);
        tabs.addTab("  Status Slot  ", slotPanel);
        tabs.addTab("  Laporan  ", reportPanel);

        tabs.addChangeListener(e -> {
            int idx = tabs.getSelectedIndex();
            if (idx == 2) slotPanel.refresh();
            if (idx == 3) reportPanel.refresh();
        });

        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }
}
