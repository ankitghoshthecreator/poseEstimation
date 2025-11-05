package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Patient;

public class DBUtils {

    /**
     * Inserts a new patient with random doctor assignment.
     * Uses transaction (TCL) for atomicity.
     */
    public static boolean insertPatient(Patient p) {
        String getDoctorSql = "SELECT therapist_id FROM therapist ORDER BY RAND() LIMIT 1";
        String insertSql = "INSERT INTO patients (name, email, password, age, diagnosis, doctor_id) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection("admin");
            if (conn == null) {
                System.err.println("❌ Database connection failed.");
                return false;
            }

            conn.setAutoCommit(false); // TCL begin

            int doctorId = 0;
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(getDoctorSql)) {
                if (rs.next()) {
                    doctorId = rs.getInt("therapist_id");
                } else {
                    System.err.println("❌ No doctors found in database.");
                    DBConnection.rollback(conn);
                    return false;
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setString(1, p.getName());
                ps.setString(2, p.getEmail());
                ps.setString(3, p.getPassword());
                ps.setInt(4, p.getAge());
                ps.setString(5, p.getDiagnosis());
                ps.setInt(6, doctorId);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    DBConnection.commit(conn);
                    System.out.println("✅ Patient inserted successfully with Doctor ID: " + doctorId);
                    return true;
                } else {
                    DBConnection.rollback(conn);
                    System.err.println("❌ Insert failed — no rows affected.");
                    return false;
                }
            }

        } catch (SQLException e) {
            DBConnection.rollback(conn);
            System.err.println("❌ Insert failed: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("⚠ Connection close failed: " + e.getMessage());
            }
        }
    }

    /**
     * Fetch all patients
     */
    public static List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients";

        try (Connection conn = DBConnection.getConnection("doctor");
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Patient p = new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("diagnosis")
                );
                patients.add(p);
            }
        } catch (SQLException e) {
            System.err.println("❌ Select failed: " + e.getMessage());
        }
        return patients;
    }

    /**
     * Update patient details (doctor privilege)
     */
    public static boolean updatePatient(Patient p) {
        String sql = "UPDATE patients SET name=?, age=?, diagnosis=? WHERE id=?";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection("doctor");
            if (conn == null) {
                System.err.println("❌ Connection failed.");
                return false;
            }

            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, p.getName());
                ps.setInt(2, p.getAge());
                ps.setString(3, p.getDiagnosis());
                ps.setInt(4, p.getId());

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    DBConnection.commit(conn);
                    System.out.println("✅ Patient updated successfully.");
                    return true;
                } else {
                    DBConnection.rollback(conn);
                    System.err.println("❌ Update failed — no rows affected.");
                    return false;
                }
            }

        } catch (SQLException e) {
            DBConnection.rollback(conn);
            System.err.println("❌ Update failed: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("⚠ Connection close failed: " + e.getMessage());
            }
        }
    }

    /**
     * Delete patient record (admin privilege)
     */
    public static boolean deletePatient(int id) {
        String sql = "DELETE FROM patients WHERE id=?";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection("admin");
            if (conn == null) {
                System.err.println("❌ Connection failed.");
                return false;
            }

            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int rows = ps.executeUpdate();

                if (rows > 0) {
                    DBConnection.commit(conn);
                    System.out.println("✅ Patient deleted successfully.");
                    return true;
                } else {
                    DBConnection.rollback(conn);
                    System.err.println("❌ Delete failed — no rows affected.");
                    return false;
                }
            }

        } catch (SQLException e) {
            DBConnection.rollback(conn);
            System.err.println("❌ Delete failed: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("⚠ Connection close failed: " + e.getMessage());
            }
        }
    }
}
