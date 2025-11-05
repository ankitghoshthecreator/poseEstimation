package model;

public class Patient {
    private int id;
    private String name;
    private String email;
    private String password;
    private int age;
    private String diagnosis;
    private Integer doctorId;

    // --- Constructor for registration ---
    public Patient(String name, String email, String password, int age, String diagnosis, Integer doctorId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.diagnosis = diagnosis;
        this.doctorId = doctorId;
    }

    // --- Constructor for basic use (name, age, diagnosis) ---
    public Patient(String name, int age, String diagnosis) {
        this.name = name;
        this.age = age;
        this.diagnosis = diagnosis;
    }

    // --- Constructor for update operations ---
    public Patient(int id, String name, int age, String diagnosis) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.diagnosis = diagnosis;
    }

    // --- Getters and Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }
}
