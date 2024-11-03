package com.syengo.sufeeds.sufeeds.models;

public class CourseClass {
    private Integer classId;
    private Integer studentId;
    private String className;
    private String semester;

    // Empty Constructor
    public CourseClass() {}

    // Constructor with fields
    public CourseClass(Integer classId, Integer studentId, String className, String semester) {
        this.classId = classId;
        this.studentId = studentId;
        this.className = className;
        this.semester = semester;
    }

    // Getters and Setters for each field in the Class Table on the Database
    public Integer getClassId() { return classId; }
    public void setClassId(Integer classId) { this.classId = classId; }

    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
}
