package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import db.DBConnection;

public class DoctorDashboard extends JFrame {

    private int doctorId;
    private String doctorName;
    private JTextArea infoArea;

    public DoctorDashboard(int doctorId, String doctorName) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;

        setTitle("Doctor Dashboard - " + doctorName);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(infoArea), BorderLayout.CENTER);

        loadDoctorData();
        setVisible(true);
    }

    private void loadDoctorData() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT p.id, p.name, p.age, p.diagnosis " +
                    "FROM patients p WHERE p.doctor_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, doctorId);
            ResultSet rs = ps.executeQuery();

            StringBuilder sb = new StringBuilder();
            sb.append("Doctor: ").append(doctorName).append("\n\n");
            sb.append("Patients under care:\n");
            sb.append("------------------------------------------\n");

            boolean hasPatients = false;
            while (rs.next()) {
                hasPatients = true;
                sb.append("ID: ").append(rs.getInt("id")).append(" | ");
                sb.append("Name: ").append(rs.getString("name")).append(" | ");
                sb.append("Age: ").append(rs.getInt("age")).append(" | ");
                sb.append("Diagnosis: ").append(rs.getString("diagnosis")).append("\n");
            }

            if (!hasPatients)
                sb.append("No patients assigned yet.");

            infoArea.setText(sb.toString());

        } catch (SQLException e) {
            infoArea.setText("Error loading data: " + e.getMessage());
        }
    }
}
