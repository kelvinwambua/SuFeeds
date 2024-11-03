// Ignore this File it was the old Database Structure I was using


package com.syengo.sufeeds.sufeeds.dao;


import com.syengo.sufeeds.sufeeds.config.DatabaseConnection;
import com.syengo.sufeeds.sufeeds.models.CourseTopic;
import eu.hansolo.toolbox.evtbus.Topic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TopicDAO {
    public boolean addTopic(CourseTopic topic) {
        String sql = "INSERT INTO tbl_topics (class_id, topic_name, week_number, feedback, student_id, " +
            "learning_outcomes, resources, difficulty) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, topic.getClassId());
            stmt.setString(2, topic.getTopicName());
            stmt.setInt(3, topic.getWeekNumber());
            stmt.setString(4, topic.getFeedback());
            stmt.setString(5, topic.getStudentId());
            stmt.setString(6, topic.getLearningOutcomes());
            stmt.setString(7, topic.getResources());
            stmt.setInt(8, topic.getDifficulty());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CourseTopic> getTopicsForClass(int classId) {
        List<CourseTopic> topics = new ArrayList<>();
        String sql = "SELECT * FROM tbl_topics WHERE class_id = ? ORDER BY week_number";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                topics.add(new CourseTopic(
                    rs.getInt("id"),
                    rs.getInt("class_id"),
                    rs.getString("topic_name"),
                    rs.getInt("week_number"),
                    rs.getString("feedback"),
                    rs.getString("student_id"),
                    rs.getTimestamp("created_at"),
                    rs.getString("learning_outcomes"),
                    rs.getString("resources"),
                    rs.getInt("difficulty")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topics;
    }
}
