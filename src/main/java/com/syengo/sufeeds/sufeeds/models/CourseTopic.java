// Topic.java
package com.syengo.sufeeds.sufeeds.models;

import java.sql.Timestamp;

public class CourseTopic {
    private int id;
    private int classId;
    private String topicName;
    private int weekNumber;
    private String feedback;
    private String studentId;
    private Timestamp createdAt;
    private String learningOutcomes;
    private String resources;
    private int difficulty;

    // Constructor for new topics (without ID and timestamp)
    public CourseTopic(int classId, String topicName, int weekNumber, String feedback,
                       String studentId, String learningOutcomes, String resources, int difficulty) {
        this.classId = classId;
        this.topicName = topicName;
        this.weekNumber = weekNumber;
        this.feedback = feedback;
        this.studentId = studentId;
        this.learningOutcomes = learningOutcomes;
        this.resources = resources;
        this.difficulty = difficulty;
    }

    // Constructor for existing topics (with ID and timestamp)
    public CourseTopic(int id, int classId, String topicName, int weekNumber, String feedback,
                       String studentId, Timestamp createdAt, String learningOutcomes,
                       String resources, int difficulty) {
        this.id = id;
        this.classId = classId;
        this.topicName = topicName;
        this.weekNumber = weekNumber;
        this.feedback = feedback;
        this.studentId = studentId;
        this.createdAt = createdAt;
        this.learningOutcomes = learningOutcomes;
        this.resources = resources;
        this.difficulty = difficulty;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getClassId() {
        return classId;
    }

    public String getTopicName() {
        return topicName;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getStudentId() {
        return studentId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getLearningOutcomes() {
        return learningOutcomes;
    }

    public String getResources() {
        return resources;
    }

    public int getDifficulty() {
        return difficulty;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setLearningOutcomes(String learningOutcomes) {
        this.learningOutcomes = learningOutcomes;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "Week " + weekNumber + ": " + topicName;
    }
}
