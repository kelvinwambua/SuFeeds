package com.syengo.sufeeds.sufeeds.dao;

import com.syengo.sufeeds.sufeeds.config.DatabaseConnection;

import com.syengo.sufeeds.sufeeds.models.Student;
import java.sql.*;

public class StudentDAO {

    public boolean register(Student student) {
        String sql = "INSERT INTO tbl_students (name, \"AdmissionNo\", \"Course\", \"Password\") VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getAdmissionNo());
            pstmt.setString(3, student.getCourse());
            pstmt.setString(4, student.getPassword());

            int affected = pstmt.executeUpdate();
            return affected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Student login(String admissionNo, String password) {
        String sql = "SELECT * FROM tbl_students WHERE \"AdmissionNo\" = ? AND \"Password\" = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, admissionNo);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Student(
                    rs.getString("name"),
                    rs.getString("AdmissionNo"),
                    rs.getString("Course"),
                    rs.getString("Password")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Additional method to check if admission number already exists
    public boolean admissionNumberExists(String admissionNo) {
        String sql = "SELECT COUNT(*) FROM student WHERE \"AdmissionNo\" = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, admissionNo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to update student information
    public boolean updateStudent(Student student) {
        String sql = "UPDATE student SET name = ?, \"Course\" = ?, \"Password\" = ? WHERE \"AdmissionNo\" = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getCourse());
            pstmt.setString(3, student.getPassword());
            pstmt.setString(4, student.getAdmissionNo());

            int affected = pstmt.executeUpdate();
            return affected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to delete student
    public boolean deleteStudent(String admissionNo) {
        String sql = "DELETE FROM student WHERE \"AdmissionNo\" = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, admissionNo);
            int affected = pstmt.executeUpdate();
            return affected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to get all students
    public java.util.List<Student> getAllStudents() {
        String sql = "SELECT * FROM student";
        java.util.List<Student> students = new java.util.ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(new Student(
                    rs.getString("name"),
                    rs.getString("AdmissionNo"),
                    rs.getString("Course"),
                    rs.getString("Password")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }
}
