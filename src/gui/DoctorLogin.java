package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import db.DBConnection;

public class DoctorLogin extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    public DoctorLogin() {
        setTitle("Volenii – Doctor Login");
        setSize(500, 350); // Increased window size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Gradient background panel
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color c1 = new Color(63, 94, 251);
                Color c2 = new Color(252, 70, 107);
                GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Title
        JLabel title = new JLabel("Volenii Doctor Portal", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Login to manage your sessions and patients", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(Color.WHITE);

        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        headerPanel.add(title);
        headerPanel.add(subtitle);

        // Input fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        inputPanel.setOpaque(false);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));

        emailField = new JTextField();
        styleTextField(emailField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));

        passwordField = new JPasswordField();
        styleTextField(passwordField);

        inputPanel.add(emailLabel);
        inputPanel.add(emailField);
        inputPanel.add(passLabel);
        inputPanel.add(passwordField);

        // Buttons
        JButton loginBtn = createStyledButton("Login");
        JButton registerBtn = createStyledButton("Register");

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(loginBtn);
        btnPanel.add(registerBtn);

        // Combine all
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setOpaque(false);
        centerPanel.add(inputPanel, BorderLayout.CENTER);
        centerPanel.add(btnPanel, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Action Listeners
        loginBtn.addActionListener(e -> authenticateDoctor());
        registerBtn.addActionListener(e -> openRegisterPage());

        setVisible(true);
    }

    /** Style helper for text fields */
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setBackground(new Color(255, 255, 255, 230));
    }

    /** Creates a styled button */
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setBackground(new Color(255, 255, 255, 240));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setForeground(Color.DARK_GRAY);

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(230, 230, 230));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(255, 255, 255, 240));
            }
        });
        return btn;
    }

    private void openRegisterPage() {
        dispose();
        new DoctorRegister();
    }

    private void authenticateDoctor() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email and password are required.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT therapist_id, name FROM therapist WHERE email=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int doctorId = rs.getInt("therapist_id");
                String name = rs.getString("name");
                JOptionPane.showMessageDialog(this, "Login successful!");
                dispose();
                new DoctorDashboard(doctorId, name);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DoctorLogin::new);
    }
}
