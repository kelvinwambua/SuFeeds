package com.syengo.sufeeds.sufeeds.ui.controllers;

import com.syengo.sufeeds.sufeeds.dao.PostDAO;
import com.syengo.sufeeds.sufeeds.models.Post;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;

public class EditPostController {
    @FXML private Label classInfoLabel;
    @FXML private ComboBox<Integer> weekComboBox;
    @FXML private TextField topicField;
    @FXML private TextArea contentArea;
    @FXML private Label errorLabel;

    private Post currentPost;
    private PostDAO postDAO = new PostDAO();
    private Runnable onSaveCallback;
    @FXML

    public void initData(Post post, Runnable onSaveCallback) {
        this.currentPost = post;
        this.onSaveCallback = onSaveCallback;

        initializeWeekComboBox();
        populateFields();
    }

    private void initializeWeekComboBox() {
        weekComboBox.setItems(FXCollections.observableArrayList(
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14
        ));
    }

    private void populateFields() {
        topicField.setText(currentPost.getTopic());
        contentArea.setText(currentPost.getContent());
        weekComboBox.setValue(currentPost.getWeekNumber());
    }

    @FXML
    private void handleSave() {
        String topic = topicField.getText().trim();
        String content = contentArea.getText().trim();
        Integer weekNumber = weekComboBox.getValue();

        if (topic.isEmpty() || content.isEmpty() || weekNumber == null) {
            showError("Please fill in all fields");
            return;
        }

        currentPost.setTopic(topic);
        currentPost.setContent(content);
        currentPost.setWeekNumber(weekNumber);

        if (postDAO.updatePost(currentPost)) {
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            closeWindow();
        } else {
            showError("Error updating post");
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
