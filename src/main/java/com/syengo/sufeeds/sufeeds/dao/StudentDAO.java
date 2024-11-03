package com.syengo.sufeeds.sufeeds.dao;

import com.syengo.sufeeds.sufeeds.config.DatabaseConnection;
import com.syengo.sufeeds.sufeeds.models.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
     //Simple Insert  -DONE
    public boolean register(Student student) {
        String sql = "INSERT INTO tbl_student (full_name, email, password) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, student.getFullName());
            pstmt.setString(2, student.getEmail());
            pstmt.setString(3, student.getPassword());

            int affected = pstmt.executeUpdate();

            if (affected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    student.setStudentId(rs.getInt(1));
                }
                return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //Simple query if email entered =  email found && password entered and password found return true
    //-DONE

    public Student login(String email, String password) {
        String sql = "SELECT * FROM tbl_student WHERE email = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Student(
                    rs.getInt("student_id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getTimestamp("registration_date").toLocalDateTime()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // In case the email is already registered
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM tbl_student WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStudent(Student student) {
        String sql = "UPDATE tbl_student SET full_name = ?, email = ?, password = ? WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getFullName());
            pstmt.setString(2, student.getEmail());
            pstmt.setString(3, student.getPassword());
            pstmt.setInt(4, student.getStudentId());

            int affected = pstmt.executeUpdate();
            return affected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
  //Was meant to be an admin feature but again I am too lazy to do an admin dashboard
    public boolean deleteStudent(Integer studentId) {
        String sql = "DELETE FROM tbl_student WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            int affected = pstmt.executeUpdate();
            return affected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //Admin Feature
    public Student getStudentById(Integer studentId) {
        String sql = "SELECT * FROM tbl_student WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Student(
                    rs.getInt("student_id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getTimestamp("registration_date").toLocalDateTime()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
   //Admin Feature but I don't want to ever code in OOP again
    public List<Student> getAllStudents() {
        String sql = "SELECT * FROM tbl_student";
        List<Student> students = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(new Student(
                    rs.getInt("student_id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getTimestamp("registration_date").toLocalDateTime()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }
}
