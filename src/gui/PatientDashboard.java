package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import db.DBConnection;

public class PatientDashboard extends JFrame {

    private int patientId;
    private String patientName;
    private int doctorId;
    private JTextArea infoArea;

    public PatientDashboard(int patientId, String patientName, int doctorId) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;

        setTitle("Volenii - Patient Dashboard (" + patientName + ")");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top header panel
        JPanel header = new JPanel();
        header.setBackground(new Color(0, 102, 204));
        JLabel title = new JLabel("Patient Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Info area
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Consolas", Font.PLAIN, 15));
        infoArea.setBackground(new Color(245, 248, 255));
        infoArea.setBorder(BorderFactory.createTitledBorder("Patient Information"));
        add(new JScrollPane(infoArea), BorderLayout.CENTER);

        // Button panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        JButton refreshBtn = new JButton("Refresh Info");
        JButton closeBtn = new JButton("Close");

        refreshBtn.setBackground(new Color(0, 153, 76));
        refreshBtn.setForeground(Color.WHITE);
        closeBtn.setBackground(new Color(204, 0, 0));
        closeBtn.setForeground(Color.WHITE);

        bottomPanel.add(refreshBtn);
        bottomPanel.add(closeBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadPatientInfo());
        closeBtn.addActionListener(e -> dispose());

        loadPatientInfo();
        setVisible(true);
    }

    private void loadPatientInfo() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT p.patient_id, p.name, p.age, p.diagnosis, t.name AS doctor_name " +
                    "FROM patients p LEFT JOIN therapist t ON p.doctor_id = t.therapist_id " +
                    "WHERE p.patient_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Patient ID       : ").append(rs.getInt("patient_id")).append("\n");
                sb.append("Name             : ").append(rs.getString("name")).append("\n");
                sb.append("Age              : ").append(rs.getInt("age")).append("\n");
                sb.append("Diagnosis        : ").append(rs.getString("diagnosis")).append("\n");
                sb.append("Assigned Doctor  : ").append(rs.getString("doctor_name") == null ? "Not Assigned" : rs.getString("doctor_name")).append("\n");
                infoArea.setText(sb.toString());
            } else {
                infoArea.setText("No patient record found for ID: " + patientId);
            }

        } catch (SQLException e) {
            infoArea.setText("Error loading patient info:\n" + e.getMessage());
        }
    }
}
