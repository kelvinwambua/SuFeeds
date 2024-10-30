package com.syengo.sufeeds.sufeeds.ui.controllers;

import com.syengo.sufeeds.sufeeds.models.CourseClass;
import com.syengo.sufeeds.sufeeds.models.CourseTopic;
import com.syengo.sufeeds.sufeeds.models.Student;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;

public class DashboardController {
    @FXML private Text welcomeText;
    @FXML private ListView<CourseClass> classList;
    @FXML private ComboBox<CourseClass> classSelector;
    @FXML private ListView<CourseTopic> topicsList;
    @FXML private Text nameText;
    @FXML private Text admissionText;
    @FXML private Text courseText;
    @FXML private StackPane contentArea;
    @FXML private ScrollPane homeFeed;
    @FXML private VBox classesContent;
    @FXML private VBox topicsContent;
    @FXML private VBox profileContent;
    @FXML private Text userNameText;
    @FXML private Text userAdmissionText;
    @FXML private VBox postsContainer;
    @FXML private VBox activityContent;

    @FXML private TextArea activityLog;

    private Student student;

    @FXML
    public void initialize() {
        // Initialize the default view state
        switchToHome();

        // Add a listener to add stylesheet once scene is available
        contentArea.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(getClass().getResource("/dashboard.css").toExternalForm());
            }
        });
    }

    public void initData(Student student) {
        this.student = student;
        updateUI();
    }

    @FXML
    private void switchToHome() {
        homeFeed.setVisible(true);
        classesContent.setVisible(false);
        topicsContent.setVisible(false);
        profileContent.setVisible(false);
    }
    @FXML
    private void switchToActivity() {
        homeFeed.setVisible(false);
        classesContent.setVisible(false);
        topicsContent.setVisible(false);
        profileContent.setVisible(false);
        activityContent.setVisible(true);
    }
    @FXML
    private void clearActivityLog() {
        activityLog.clear();
    }

    @FXML
    private void switchToClasses() {
        homeFeed.setVisible(false);
        classesContent.setVisible(true);
        topicsContent.setVisible(false);
        profileContent.setVisible(false);
    }

    @FXML
    private void switchToTopics() {
        homeFeed.setVisible(false);
        classesContent.setVisible(false);
        topicsContent.setVisible(true);
        profileContent.setVisible(false);
    }

    @FXML
    private void switchToProfile() {
        homeFeed.setVisible(false);
        classesContent.setVisible(false);
        topicsContent.setVisible(false);
        profileContent.setVisible(true);
    }

    private void updateUI() {

        // Update profile information
        nameText.setText(student.getName());
        admissionText.setText(student.getAdmissionNo());
        courseText.setText(student.getCourse());
        userNameText.setText(student.getName());
        userAdmissionText.setText(student.getAdmissionNo());

        refreshClasses();
    }

    @FXML
    private void showNewPostDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("New Post");
        dialog.setHeaderText("Create a new post");

        // Create the post content text area
        TextArea postContent = new TextArea();
        postContent.setPromptText("What's happening?");
        postContent.setPrefRowCount(5);

        dialog.getDialogPane().setContent(postContent);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return postContent.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(content -> {
            if (!content.trim().isEmpty()) {
                addNewPost(content);
            }
        });
    }

    private void addNewPost(String content) {
        // Create post container
        VBox post = new VBox();
        post.getStyleClass().add("post");
        post.setSpacing(10);

        // Create header with user info
        HBox header = new HBox();
        header.getStyleClass().add("post-header");
        header.setSpacing(10);

        Text userName = new Text(student.getName());
        userName.getStyleClass().add("post-author");

        Text meta = new Text(student.getCourse() + " • Just now");
        meta.getStyleClass().add("post-meta");

        header.getChildren().addAll(userName, meta);

        // Create post content
        Text postText = new Text(content);
        postText.getStyleClass().add("post-content");
        postText.setWrappingWidth(500);

        // Create action buttons
        HBox actions = new HBox();
        actions.getStyleClass().add("post-actions");
        actions.setSpacing(20);

        Button likeBtn = new Button("Like");
        Button commentBtn = new Button("Comment");
        Button shareBtn = new Button("Share");

        likeBtn.getStyleClass().add("action-button");
        commentBtn.getStyleClass().add("action-button");
        shareBtn.getStyleClass().add("action-button");

        actions.getChildren().addAll(likeBtn, commentBtn, shareBtn);

        // Add all elements to post
        post.getChildren().addAll(header, postText, actions);

        // Add post to the feed (at the top)
        postsContainer.getChildren().add(0, post);
    }

    @FXML
    private void showClassManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/class-management.fxml"));
            Parent root = loader.load();

            ClassManagementController controller = loader.getController();
            controller.initData(student);

            Stage stage = (Stage) welcomeText.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void refreshClasses() {
        classList.getItems().clear();
        // TODO: Load classes from database
    }

    @FXML
    private void showAddTopicDialog() {
        CourseClass selectedClass = classSelector.getValue();
        if (selectedClass == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a class first!");
            return;
        }

        Dialog<CourseTopic> dialog = new Dialog<>();
        dialog.setTitle("Add New Topic");
        dialog.setHeaderText("Add a new topic for " + selectedClass.getClassName());

        // Create the topic input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField topicName = new TextField();
        TextArea description = new TextArea();
        description.setPrefRowCount(3);

        grid.addRow(0, new Label("Topic Name:"), topicName);
        grid.addRow(1, new Label("Description:"), description);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // TODO: Implement topic creation logic
        dialog.showAndWait();
    }

    @FXML
    private void showEditProfileDialog() {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Edit Profile");
        dialog.setHeaderText("Update your profile information");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField(student.getName());
        TextField courseField = new TextField(student.getCourse());

        grid.addRow(0, new Label("Name:"), nameField);
        grid.addRow(1, new Label("Course:"), courseField);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                student.setName(nameField.getText());
                student.setCourse(courseField.getText());
                updateUI();
                return student;
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void showChangePasswordDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Enter your new password");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        PasswordField currentPassword = new PasswordField();
        PasswordField newPassword = new PasswordField();
        PasswordField confirmPassword = new PasswordField();

        grid.addRow(0, new Label("Current Password:"), currentPassword);
        grid.addRow(1, new Label("New Password:"), newPassword);
        grid.addRow(2, new Label("Confirm Password:"), confirmPassword);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                if (!newPassword.getText().equals(confirmPassword.getText())) {
                    showAlert(Alert.AlertType.ERROR, "Error", "New passwords do not match!");
                    return null;
                }
                // TODO: Implement password change logic
                return newPassword.getText();
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            stage.setScene(new Scene(root, 300, 200));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
