package com.syengo.sufeeds.sufeeds.models;


public class Student {
    private String name;
    private String admissionNo;
    private String course;
    private String password;

    public Student(String name, String admissionNo, String course, String password) {
        this.name = name;
        this.admissionNo = admissionNo;
        this.course = course;
        this.password = password;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
