package com.spms.gui;

import com.spms.model.ParkingSlot;
import com.spms.system.ParkingSystem;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class SlotPanel extends JPanel {
    private final ParkingSystem system;
    private JPanel gridPanel;
    private JLabel lblSummary;

    private static final Color COLOR_AVAILABLE = new Color(200, 230, 201);
    private static final Color COLOR_OCCUPIED   = new Color(255, 205, 210);
    private static final Color COLOR_AVAIL_TEXT = new Color(27, 94, 32);
    private static final Color COLOR_OCC_TEXT   = new Color(183, 28, 28);

    public SlotPanel(ParkingSystem system) {
        this.system = system;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setBackground(Color.WHITE);

        // Header bar
        JPanel headerBar = new JPanel(new BorderLayout());
        headerBar.setBackground(Color.WHITE);
        JLabel title = new JLabel("Status Slot Parkir");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(21, 101, 192));
        JButton btnRefresh = new JButton("  Refresh  ");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefresh.setBackground(new Color(21, 101, 192));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        headerBar.add(title, BorderLayout.WEST);
        headerBar.add(btnRefresh, BorderLayout.EAST);

        // Grid
        gridPanel = new JPanel(new GridLayout(0, 5, 12, 12));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Summary
        lblSummary = new JLabel();
        lblSummary.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Legend
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        legendPanel.setBackground(Color.WHITE);
        legendPanel.add(makeLegend(COLOR_AVAILABLE, COLOR_AVAIL_TEXT, "Kosong"));
        legendPanel.add(makeLegend(COLOR_OCCUPIED, COLOR_OCC_TEXT, "Terisi"));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(lblSummary, BorderLayout.WEST);
        bottomPanel.add(legendPanel, BorderLayout.EAST);

        add(headerBar, BorderLayout.NORTH);
        add(new JScrollPane(gridPanel), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> refresh());
        refresh();
    }

    public void refresh() {
        gridPanel.removeAll();
        List<ParkingSlot> slots = system.getSlots();

        for (ParkingSlot slot : slots) {
            JPanel card = new JPanel(new BorderLayout());
            card.setPreferredSize(new Dimension(130, 90));
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(slot.isAvailable() ? COLOR_AVAIL_TEXT : COLOR_OCC_TEXT, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
            ));
            card.setBackground(slot.isAvailable() ? COLOR_AVAILABLE : COLOR_OCCUPIED);

            JLabel lblSlot = new JLabel("SLOT " + slot.getSlotNumber(), SwingConstants.CENTER);
            lblSlot.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblSlot.setForeground(slot.isAvailable() ? COLOR_AVAIL_TEXT : COLOR_OCC_TEXT);

            JLabel lblStatus = new JLabel(slot.isAvailable() ? "KOSONG" : "TERISI", SwingConstants.CENTER);
            lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblStatus.setForeground(slot.isAvailable() ? COLOR_AVAIL_TEXT : COLOR_OCC_TEXT);

            JLabel lblVehicle = new JLabel(
                slot.isAvailable() ? " " : slot.getCurrentVehicle().getLicensePlate(),
                SwingConstants.CENTER);
            lblVehicle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lblVehicle.setForeground(Color.DARK_GRAY);

            card.add(lblSlot, BorderLayout.NORTH);
            card.add(lblStatus, BorderLayout.CENTER);
            card.add(lblVehicle, BorderLayout.SOUTH);

            gridPanel.add(card);
        }

        int available = system.getAvailableSlotCount();
        int occupied = system.getOccupiedSlotCount();
        lblSummary.setText(String.format(
            "Tersedia: %d  |  Terisi: %d  |  Total: %d",
            available, occupied, slots.size()));

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel makeLegend(Color bg, Color fg, String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setBackground(Color.WHITE);
        JLabel box = new JLabel("  ");
        box.setOpaque(true);
        box.setBackground(bg);
        box.setBorder(BorderFactory.createLineBorder(fg));
        p.add(box);
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        p.add(lbl);
        return p;
    }
}
