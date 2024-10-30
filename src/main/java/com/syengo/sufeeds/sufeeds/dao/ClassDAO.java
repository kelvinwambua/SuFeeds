package com.syengo.sufeeds.sufeeds.dao;

import com.syengo.sufeeds.sufeeds.config.DatabaseConnection;
import com.syengo.sufeeds.sufeeds.models.CourseClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClassDAO {
    public boolean addClass(CourseClass classObj) {
        String sql = "INSERT INTO tbl_classes (class_name, semester, student_id, description, day_of_week, time_slot) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, classObj.getClassName());
            stmt.setString(2, classObj.getSemester());
            stmt.setString(3, classObj.getStudentId());
            stmt.setString(4, classObj.getDescription());
            stmt.setString(5, classObj.getDayOfWeek());
            stmt.setString(6, classObj.getTimeSlot());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CourseClass> getClassesForStudent(String studentId) {
        List<CourseClass> classes = new ArrayList<>();
        String sql = "SELECT * FROM tbl_classes WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                classes.add(new CourseClass(
                    rs.getInt("id"),
                    rs.getString("class_name"),
                    rs.getString("semester"),
                    rs.getString("student_id"),
                    rs.getString("description"),
                    rs.getString("day_of_week"),
                    rs.getString("time_slot")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }
}
