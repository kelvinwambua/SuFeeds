
package com.syengo.sufeeds.sufeeds.models;

import java.time.LocalDateTime;

public class Student {
    private Integer studentId;
    private String fullName;
    private String email;
    private String password;
    private LocalDateTime registrationDate;

    // Constructor
    public Student() {}

    // Constructor with fields
    public Student(Integer studentId, String fullName, String email, String password, LocalDateTime registrationDate) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
    }

    // Getters and Setters
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
}

