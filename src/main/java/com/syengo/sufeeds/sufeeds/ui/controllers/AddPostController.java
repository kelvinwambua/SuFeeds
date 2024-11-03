package com.syengo.sufeeds.sufeeds.ui.controllers;

import com.syengo.sufeeds.sufeeds.dao.PostDAO;
import com.syengo.sufeeds.sufeeds.models.CourseClass;
import com.syengo.sufeeds.sufeeds.models.Post;
import com.syengo.sufeeds.sufeeds.models.Student;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddPostController {
    @FXML private Label classInfoLabel;
    @FXML private Label weekLabel;
    @FXML private TextField topicField;
    @FXML private TextArea contentArea;
    @FXML private Label errorLabel;

    private Student currentStudent;
    private CourseClass currentClass;
    private Integer weekNumber;
    private PostDAO postDAO = new PostDAO();
    private Runnable onPostCallback;


    public void initData(Student student, CourseClass courseClass, Integer week, Runnable onPostCallback) {
        this.currentStudent = student;
        this.currentClass = courseClass;
        this.weekNumber = week;
        this.onPostCallback = onPostCallback;

        classInfoLabel.setText(courseClass.getClassName() + " (" + courseClass.getSemester() + ")");
        weekLabel.setText("Week " + week);
    }

    @FXML
    private void handlePost() {
        String topic = topicField.getText().trim();
        String content = contentArea.getText().trim();

        if (topic.isEmpty() || content.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        Post newPost = new Post();
        newPost.setStudentId(currentStudent.getStudentId());
        newPost.setClassId(currentClass.getClassId());
        newPost.setWeekNumber(weekNumber);
        newPost.setTopic(topic);
        newPost.setContent(content);

        if (postDAO.createPost(newPost)) {
            if (onPostCallback != null) {
                onPostCallback.run();
            }
            closeWindow();
        } else {
            showError("Error creating post");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void closeWindow() {
        Stage stage = (Stage) topicField.getScene().getWindow();
        stage.close();
    }
}
