package com.syengo.sufeeds.sufeeds.dao;

import com.syengo.sufeeds.sufeeds.config.DatabaseConnection;
import com.syengo.sufeeds.sufeeds.models.Post;
import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {
    //Simple Insert - DONE
    public boolean createPost(Post post) {
        String sql = "INSERT INTO tbl_post (student_id, class_id, week_number, topic, content) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, post.getStudentId());
            pstmt.setInt(2, post.getClassId());
            pstmt.setInt(3, post.getWeekNumber());
            pstmt.setString(4, post.getTopic());
            pstmt.setString(5, post.getContent());

            int affected = pstmt.executeUpdate();

            if (affected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    post.setPostId(rs.getInt(1));
                }
                return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //Did not Use it, App will become too much- NOT DONE  :(

    public Post getPostById(Integer postId) {
        String sql = "SELECT * FROM tbl_post WHERE post_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Post(
                    rs.getInt("post_id"),
                    rs.getInt("student_id"),
                    rs.getInt("class_id"),
                    rs.getInt("week_number"),
                    rs.getString("topic"),
                    rs.getString("content"),
                    rs.getTimestamp("post_date").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
     // Wanted to have a view where we can list specific posts based on class or filter them but Nah too much work - NOT DONE Gladly
    public List<Post> getPostsByClass(Integer classId) {
        String sql = "SELECT * FROM tbl_post WHERE class_id = ? ORDER BY week_number, post_date";
        List<Post> posts = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, classId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                posts.add(new Post(
                    rs.getInt("post_id"),
                    rs.getInt("student_id"),
                    rs.getInt("class_id"),
                    rs.getInt("week_number"),
                    rs.getString("topic"),
                    rs.getString("content"),
                    rs.getTimestamp("post_date").toLocalDateTime()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }
    //This allows us to have all the information when we query for posts like who posted it and which class
    //DONE
    //JOINs I think it is an equijoin but I have not read DBMS so I am not too sure
    public List<Post> getPostsByClass() {
        List<Post> posts = new ArrayList<>();
        String query = """
        SELECT p.*, c.class_name, c.semester, s.full_name, s.email
        FROM tbl_post p
        JOIN tbl_class c ON p.class_id = c.class_id
        JOIN tbl_student s ON p.student_id = s.student_id
        ORDER BY p.post_date DESC
    """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Post post = new Post();
                post.setPostId(rs.getInt("post_id"));
                post.setClassId(rs.getInt("class_id"));
                post.setStudentId(rs.getInt("student_id"));
                post.setWeekNumber(rs.getInt("week_number"));
                post.setTopic(rs.getString("topic"));
                post.setContent(rs.getString("content"));
                post.setPostDate(rs.getTimestamp("post_date").toLocalDateTime());  // Changed from created_at to post_date
                post.setClassName(rs.getString("class_name"));
                post.setSemsester(rs.getString("semester"));
                post.setAuthorName(rs.getString("full_name"));
                post.setAuthorEmail(rs.getString("email"));
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return posts;
    }
    //So we can delete them -DONE
    public List<Post> getPostsByStudent(Integer studentId) {
        String sql = "SELECT * FROM tbl_post WHERE student_id = ? ORDER BY post_date DESC";
        List<Post> posts = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                posts.add(new Post(
                    rs.getInt("post_id"),
                    rs.getInt("student_id"),
                    rs.getInt("class_id"),
                    rs.getInt("week_number"),
                    rs.getString("topic"),
                    rs.getString("content"),
                    rs.getTimestamp("post_date").toLocalDateTime()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }
    //Was meant to be there for filtering but I am too lazy-NOT DONE
    public List<Post> getPostsByWeek(Integer classId, Integer weekNumber) {
        String sql = "SELECT * FROM tbl_post WHERE class_id = ? AND week_number = ? ORDER BY post_date";
        List<Post> posts = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, classId);
            pstmt.setInt(2, weekNumber);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                posts.add(new Post(
                    rs.getInt("post_id"),
                    rs.getInt("student_id"),
                    rs.getInt("class_id"),
                    rs.getInt("week_number"),
                    rs.getString("topic"),
                    rs.getString("content"),
                    rs.getTimestamp("post_date").toLocalDateTime()
                ));
            }

            System.out.println("Retrieved " + posts.size() + " posts for class " + classId + " in week " + weekNumber);
        } catch (Exception e) {
            System.err.println("Error retrieving posts:");
            e.printStackTrace();
        }
        return posts;
    }
    // Simple Update -DONE
    public boolean updatePost(Post post) {
        String sql = "UPDATE tbl_post SET topic = ?, content = ?, week_number = ? WHERE post_id = ? AND student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, post.getTopic());
            pstmt.setString(2, post.getContent());
            pstmt.setInt(3, post.getWeekNumber());
            pstmt.setInt(4, post.getPostId());
            pstmt.setInt(5, post.getStudentId());

            int affected = pstmt.executeUpdate();
            return affected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //Simple Delete -DONE
    public boolean deletePost(Integer postId, Integer studentId) {
        String sql = "DELETE FROM tbl_post WHERE post_id = ? AND student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, postId);
            pstmt.setInt(2, studentId);

            int affected = pstmt.executeUpdate();
            return affected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
