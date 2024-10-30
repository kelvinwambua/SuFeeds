// CourseClass.java
package com.syengo.sufeeds.sufeeds.models;

public class CourseClass {
    private int id;
    private String className;
    private String semester;
    private String studentId;
    private String description;
    private String dayOfWeek;
    private String timeSlot;
    public  CourseClass(){}

    // Constructor for new classes (without ID)
    public CourseClass(String className, String semester, String studentId, String description,
                 String dayOfWeek, String timeSlot) {
        this.className = className;
        this.semester = semester;
        this.studentId = studentId;
        this.description = description;
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
    }

    // Constructor for existing classes (with ID)
    public CourseClass(int id, String className, String semester, String studentId,
                 String description, String dayOfWeek, String timeSlot) {
        this.id = id;
        this.className = className;
        this.semester = semester;
        this.studentId = studentId;
        this.description = description;
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public String getSemester() {
        return semester;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getDescription() {
        return description;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Override
    public String toString() {
        return className + " (" + dayOfWeek + " " + timeSlot + ")";
    }
}
