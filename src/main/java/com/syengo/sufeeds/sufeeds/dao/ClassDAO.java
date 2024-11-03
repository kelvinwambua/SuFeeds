package com.syengo.sufeeds.sufeeds.dao;

import com.syengo.sufeeds.sufeeds.config.DatabaseConnection;
import com.syengo.sufeeds.sufeeds.models.CourseClass;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassDAO {

    //Insert Class Details into DB-DONE
    public boolean addClass(CourseClass classObj) {
        String sql = "INSERT INTO tbl_class (student_id, class_name, semester) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, classObj.getStudentId());
            pstmt.setString(2, classObj.getClassName());
            pstmt.setString(3, classObj.getSemester());

            int affected = pstmt.executeUpdate();
            return affected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

   //Read Records filtered by Student ID for specific Post View-DONE
    public List<CourseClass> getClassesByStudent(Integer studentId) {
        String sql = "SELECT * FROM tbl_class WHERE student_id = ?";
        List<CourseClass> classes = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                CourseClass classObj = new CourseClass(
                    rs.getInt("class_id"),
                    rs.getInt("student_id"),
                    rs.getString("class_name"),
                    rs.getString("semester")
                );
                classes.add(classObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }
    //Simple Update on Database -DONE
    public boolean updateClass(CourseClass classObj) {
        String sql = "UPDATE tbl_class SET class_name = ?, semester = ? WHERE class_id = ? AND student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, classObj.getClassName());
            pstmt.setString(2, classObj.getSemester());
            pstmt.setInt(3, classObj.getClassId());
            pstmt.setInt(4, classObj.getStudentId());

            int affected = pstmt.executeUpdate();
            return affected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //Simple Native Delete to the Database -DONE
    public boolean deleteClass(Integer classId, Integer studentId) {
        String deletePostsSql = "DELETE FROM tbl_post WHERE class_id = ?";
        String deleteClassSql = "DELETE FROM tbl_class WHERE class_id = ? AND student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // First delete all posts in this class cause of Relationships
            try (PreparedStatement pstmt = conn.prepareStatement(deletePostsSql)) {
                pstmt.setInt(1, classId);
                pstmt.executeUpdate();
            }

            // Then delete the class
            try (PreparedStatement pstmt = conn.prepareStatement(deleteClassSql)) {
                pstmt.setInt(1, classId);
                pstmt.setInt(2, studentId);
                int affected = pstmt.executeUpdate();
                return affected > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
  //Allows us to find a specific class
    public CourseClass getClassById(Integer classId) {
        String sql = "SELECT * FROM tbl_class WHERE class_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, classId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new CourseClass(
                    rs.getInt("class_id"),
                    rs.getInt("student_id"),
                    rs.getString("class_name"),
                    rs.getString("semester")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
