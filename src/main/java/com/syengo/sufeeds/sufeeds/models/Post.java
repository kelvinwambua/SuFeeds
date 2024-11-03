// This is actual hell
package com.syengo.sufeeds.sufeeds.models;

import java.time.LocalDateTime;

public class Post {
    private Integer postId;
    private Integer studentId;
    private Integer classId;
    private Integer weekNumber;
    private String topic;
    private String content;
    private LocalDateTime postDate;
    private  String className;
    private String authorName;
    private String authorEmail;
    public String Semsester;

    // Constructor
    public Post() {}

    // Constructor with fields
    public Post(Integer postId, Integer studentId, Integer classId, Integer weekNumber,
                String topic, String content, LocalDateTime postDate) {
        this.postId = postId;
        this.studentId = studentId;
        this.classId = classId;
        this.weekNumber = weekNumber;
        this.topic = topic;
        this.content = content;
        this.postDate = postDate;

    }
    // Setters and getters for all the fields in the database
     public String getSemsester() {

        return Semsester;
     }
     public void setSemsester(String semsester) {

        Semsester = semsester;
     }
     public String getAuthorName() {

        return authorName;
     }
     public void setAuthorName(String authorName) {

        this.authorName = authorName;
     }
     public String getAuthorEmail() {

        return authorEmail;
     }
     public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;

     }
     public String getClassName(){

        return className;
     }
     public void setClassName(String className){

        this.className = className;
     }
    // Getters and Setters
    public Integer getPostId() {
        return postId;
    }
    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getStudentId() {
        return studentId;
    }
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getClassId() {
        return classId;
    }
    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }
    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public String getTopic() {
        return topic;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPostDate() {
        return postDate;
    }
    public void setPostDate(LocalDateTime postDate) {
        this.postDate = postDate;
    }
}
