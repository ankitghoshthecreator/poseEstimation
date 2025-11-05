package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainScreen extends JFrame {

    public MainScreen() {
        setTitle("Volenii – Physiotherapy Management System");
        setSize(600, 400); // Increased width and height
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Custom gradient panel
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(63, 94, 251);
                Color color2 = new Color(252, 70, 107);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Title section
        JLabel title = new JLabel("Volenii", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Physiotherapy Management System", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitle.setForeground(Color.WHITE);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);

        // Buttons section
        JButton doctorBtn = new JButton("Doctor Login");
        JButton patientBtn = new JButton("Patient Login");

        Font btnFont = new Font("Arial", Font.BOLD, 16);
        doctorBtn.setFont(btnFont);
        patientBtn.setFont(btnFont);

        Color btnColor = new Color(255, 255, 255, 220);
        doctorBtn.setBackground(btnColor);
        patientBtn.setBackground(btnColor);

        doctorBtn.setFocusPainted(false);
        patientBtn.setFocusPainted(false);

        doctorBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        patientBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(doctorBtn);
        buttonPanel.add(patientBtn);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Button actions
        doctorBtn.addActionListener((ActionEvent e) -> {
            new DoctorLogin();
            dispose();
        });

        patientBtn.addActionListener((ActionEvent e) -> {
            new PatientLogin();
            dispose();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainScreen::new);
    }
}
