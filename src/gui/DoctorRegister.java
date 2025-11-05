package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import db.DBConnection;

public class DoctorRegister extends JFrame {

    private JTextField nameField, emailField, phoneField, specializationField;
    private JPasswordField passwordField;

    public DoctorRegister() {
        setTitle("Volenii - Doctor Registration");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ---- HEADER ----
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 153));
        JLabel title = new JLabel("Doctor Registration - Volenii");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        headerPanel.add(title);

        // ---- MAIN FORM PANEL ----
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 250, 255));
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel[] labels = {
                new JLabel("Full Name:"),
                new JLabel("Email Address:"),
                new JLabel("Password:"),
                new JLabel("Phone Number:"),
                new JLabel("Specialization:")
        };

        nameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        phoneField = new JTextField(20);
        specializationField = new JTextField(20);

        JTextField[] fields = { nameField, emailField, phoneField, specializationField };

        // Label font
        for (JLabel lbl : labels) {
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lbl.setForeground(new Color(0, 51, 102));
        }

        // Field style
        for (JTextField tf : fields) {
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            tf.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 200, 230), 1),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
        }
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 230), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        // ---- Add components ----
        gbc.gridx = 0; gbc.gridy = 0;
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            formPanel.add(labels[i], gbc);
            gbc.gridx = 1;
            switch (i) {
                case 0 -> formPanel.add(nameField, gbc);
                case 1 -> formPanel.add(emailField, gbc);
                case 2 -> formPanel.add(passwordField, gbc);
                case 3 -> formPanel.add(phoneField, gbc);
                case 4 -> formPanel.add(specializationField, gbc);
            }
            gbc.gridy++;
        }

        // ---- Buttons ----
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 250, 255));

        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back to Login");

        styleButton(registerBtn, new Color(0, 153, 102));
        styleButton(backBtn, new Color(204, 51, 51));

        buttonPanel.add(registerBtn);
        buttonPanel.add(backBtn);

        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        registerBtn.addActionListener(e -> registerDoctor());
        backBtn.addActionListener(e -> {
            dispose();
            new DoctorLogin();
        });

        setVisible(true);
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });
    }

    private void registerDoctor() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String phone = phoneField.getText().trim();
        String specialization = specializationField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name, Email, and Password are required.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO therapist (name, email, password, phone, specialization) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, phone);
            ps.setString(5, specialization);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Doctor registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new DoctorLogin();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DoctorRegister::new);
    }
}
