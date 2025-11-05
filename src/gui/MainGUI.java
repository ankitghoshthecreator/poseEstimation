package gui;

import db.DBUtils;
import model.Patient;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainGUI extends JFrame {

    private JTextField nameField, ageField, diagnosisField, idField;
    private JTextArea outputArea;

    public MainGUI() {
        setTitle("Patient Management System");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("ID (for update/delete):"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        inputPanel.add(ageField);

        inputPanel.add(new JLabel("Diagnosis:"));
        diagnosisField = new JTextField();
        inputPanel.add(diagnosisField);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton insertBtn = new JButton("Add Patient");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton viewBtn = new JButton("View All");

        btnPanel.add(insertBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(viewBtn);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        add(inputPanel, BorderLayout.NORTH);
        add(btnPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        insertBtn.addActionListener(e -> addPatient());
        updateBtn.addActionListener(e -> updatePatient());
        deleteBtn.addActionListener(e -> deletePatient());
        viewBtn.addActionListener(e -> viewPatients());
    }

    private void addPatient() {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String diag = diagnosisField.getText();

        boolean ok = DBUtils.insertPatient(new Patient(name, age, diag));
        outputArea.setText(ok ? "Inserted successfully\n" : "Insert failed\n");
    }

    private void updatePatient() {
        String idText = idField.getText().trim();
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String diag = diagnosisField.getText().trim();

        if (idText.isEmpty() || ageText.isEmpty() || name.isEmpty() || diag.isEmpty()) {
            outputArea.setText("Error: All fields are required for update.\n");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            int age = Integer.parseInt(ageText);

            boolean ok = DBUtils.updatePatient(new Patient(id, name, age, diag));
            outputArea.setText(ok ? "Updated successfully\n" : "Update failed\n");

        } catch (NumberFormatException ex) {
            outputArea.setText("Error: ID and Age must be numeric.\n");
        }
    }



    private void deletePatient() {
        String idText = idField.getText().trim();

        if (idText.isEmpty()) {
            outputArea.setText("Error: ID is required for delete.\n");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            boolean ok = DBUtils.deletePatient(id);
            outputArea.setText(ok ? "Deleted successfully\n" : "Delete failed\n");
        } catch (NumberFormatException ex) {
            outputArea.setText("Error: ID must be numeric.\n");
        }
    }

    private void viewPatients() {
        List<Patient> list = DBUtils.getAllPatients();
        outputArea.setText("");
        for (Patient p : list)
            outputArea.append(p.getId() + " | " + p.getName() + " | " + p.getAge() + " | " + p.getDiagnosis() + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }
}
