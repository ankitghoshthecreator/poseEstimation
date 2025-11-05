package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Database base URL
    private static final String URL = "jdbc:mysql://localhost:3306/physiotherapy_db";

    // Root admin (full access)
    private static final String ADMIN_USER = "root";
    private static final String ADMIN_PASS = "ag98228696AnPr!";

    // Role-specific MySQL users (created via GRANT)
    private static final String DOCTOR_USER = "doctor_user";
    private static final String DOCTOR_PASS = "docpass";

    private static final String PATIENT_USER = "patient_user";
    private static final String PATIENT_PASS = "patpass";

    /**
     * Default: Admin connection (for setup or debugging)
     */
    public static Connection getConnection() {
        return getConnection("admin");
    }

    /**
     * Role-based connection — supports DCL user separation
     *
     * @param role "doctor", "patient", or "admin"
     */
    public static Connection getConnection(String role) {
        String user;
        String password;

        switch (role.toLowerCase()) {
            case "doctor":
                user = DOCTOR_USER;
                password = DOCTOR_PASS;
                break;
            case "patient":
                user = PATIENT_USER;
                password = PATIENT_PASS;
                break;
            default:
                user = ADMIN_USER;
                password = ADMIN_PASS;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, user, password);
            conn.setAutoCommit(false); // Disable autocommit for TCL use (commit/rollback)
            System.out.println("✅ Connected as role: " + role);
            return conn;

        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found.");
        } catch (SQLException e) {
            System.err.println("❌ Connection failed for role [" + role + "]: " + e.getMessage());
        }
        return null;
    }

    /**
     * Explicitly commit a transaction (TCL)
     */
    public static void commit(Connection conn) {
        if (conn != null) {
            try {
                conn.commit();
                System.out.println("✅ Transaction committed.");
            } catch (SQLException e) {
                System.err.println("❌ Commit failed: " + e.getMessage());
            }
        }
    }

    /**
     * Explicitly rollback a transaction (TCL)
     */
    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
                System.err.println("⚠ Transaction rolled back.");
            } catch (SQLException e) {
                System.err.println("❌ Rollback failed: " + e.getMessage());
            }
        }
    }

    // For testing
    public static void main(String[] args) {
        Connection adminConn = getConnection("admin");
        Connection doctorConn = getConnection("doctor");
        Connection patientConn = getConnection("patient");
    }
}
