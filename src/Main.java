

import gui.MainFrame;
import system.ParkingSystem;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        ParkingSystem system = new ParkingSystem(10);

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(system);
            frame.setVisible(true);
        });
    }
}
