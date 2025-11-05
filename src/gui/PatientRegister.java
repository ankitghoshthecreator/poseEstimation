package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import db.DBConnection;

public class PatientRegister extends JFrame {

    private JTextField nameField, emailField, ageField, diagnosisField;
    private JPasswordField passwordField;

    public PatientRegister() {
        setTitle("Volenii - Patient Registration");
        setSize(500, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ===== Header =====
        JPanel header = new JPanel();
        header.setBackground(new Color(0, 102, 204));
        JLabel title = new JLabel("Volenii Patient Registration", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // ===== Form Panel =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(nameField, gbc);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(emailLabel, gbc);

        emailField = new JTextField(20);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(emailField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(passwordField, gbc);

        // Age
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(ageLabel, gbc);

        ageField = new JTextField(20);
        ageField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(ageField, gbc);

        // Diagnosis
        JLabel diagnosisLabel = new JLabel("Diagnosis:");
        diagnosisLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(diagnosisLabel, gbc);

        diagnosisField = new JTextField(20);
        diagnosisField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        gbc.gridx = 1; gbc.gridy = 4;
        formPanel.add(diagnosisField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // ===== Button Panel =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back to Login");

        registerBtn.setBackground(new Color(0, 153, 76));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        registerBtn.setFocusPainted(false);
        registerBtn.setPreferredSize(new Dimension(150, 38));

        backBtn.setBackground(new Color(255, 140, 0));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        backBtn.setFocusPainted(false);
        backBtn.setPreferredSize(new Dimension(150, 38));

        buttonPanel.add(registerBtn);
        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // ===== Event Handling =====
        registerBtn.addActionListener(e -> registerPatient());
        backBtn.addActionListener(e -> {
            dispose();
            new PatientLogin();
        });

        setVisible(true);
    }

    private void registerPatient() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String diagnosis = diagnosisField.getText().trim();
        String ageText = ageField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || ageText.isEmpty() || diagnosis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int age = Integer.parseInt(ageText);
            if (age <= 0 || age > 120) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO patients (name, email, password, age, diagnosis, doctor_id) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, password);
                ps.setInt(4, age);
                ps.setString(5, diagnosis);
                ps.setNull(6, Types.INTEGER); // not yet assigned to a doctor

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new PatientLogin();
                } else {
                    JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
