package gui;

import system.ParkingSystem;
import system.ParkingSystem.ParkingRecord;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ReportPanel extends JPanel {
    private final ParkingSystem system;
    private DefaultTableModel tableModel;
    private JLabel lblRevenue, lblTotal;

    private static final NumberFormat IDR = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"));

    public ReportPanel(ParkingSystem system) {
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
        JLabel title = new JLabel("Laporan Riwayat Parkir");
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

        // Revenue summary panel
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        summaryPanel.setBackground(new Color(227, 242, 253));
        summaryPanel.setBorder(BorderFactory.createLineBorder(new Color(21, 101, 192), 1));

        lblTotal = makeSummaryLabel("Total Transaksi: 0");
        lblRevenue = makeSummaryLabel("Total Pendapatan: Rp 0");
        summaryPanel.add(lblTotal);
        summaryPanel.add(new JSeparator(SwingConstants.VERTICAL));
        summaryPanel.add(lblRevenue);

        // Table
        String[] columns = {"Tiket ID", "Kendaraan", "Slot", "Waktu Masuk", "Waktu Keluar", "Durasi (jam)", "Biaya"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(21, 101, 192));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(200, 220, 255));
        table.setGridColor(new Color(220, 220, 220));
        table.setShowHorizontalLines(true);

        // Right-align Biaya column
        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(6).setCellRenderer(rightAlign);
        // Center-align Slot and Durasi
        DefaultTableCellRenderer centerAlign = new DefaultTableCellRenderer();
        centerAlign.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(2).setCellRenderer(centerAlign);
        table.getColumnModel().getColumn(5).setCellRenderer(centerAlign);

        // Column widths
        int[] widths = {160, 160, 50, 140, 140, 90, 110};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        add(headerBar, BorderLayout.NORTH);
        add(summaryPanel, BorderLayout.CENTER);
        add(new JScrollPane(table), BorderLayout.SOUTH);

        // Adjust layout to give table more space
        setLayout(new BorderLayout(10, 8));
        add(headerBar, BorderLayout.NORTH);

        JPanel centerContent = new JPanel(new BorderLayout(0, 8));
        centerContent.setBackground(Color.WHITE);
        centerContent.add(summaryPanel, BorderLayout.NORTH);
        centerContent.add(new JScrollPane(table), BorderLayout.CENTER);
        add(centerContent, BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> refresh());
        refresh();
    }

    public void refresh() {
        tableModel.setRowCount(0);
        List<ParkingRecord> history = system.getHistory();

        for (ParkingRecord r : history) {
            tableModel.addRow(new Object[]{
                r.ticketId,
                r.vehicle,
                r.slotNumber,
                r.entryTime,
                r.exitTime,
                r.hours,
                "Rp " + IDR.format((long) r.fee)
            });
        }

        lblTotal.setText("Total Transaksi: " + history.size());
        lblRevenue.setText("Total Pendapatan: Rp " + IDR.format((long) system.getTotalRevenue()));
    }

    private JLabel makeSummaryLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(new Color(21, 101, 192));
        return l;
    }
}
