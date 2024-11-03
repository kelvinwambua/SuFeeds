// Do not touch this it is likely to break
package com.syengo.sufeeds.sufeeds.ui.controllers;

import com.syengo.sufeeds.sufeeds.dao.ClassDAO;
import com.syengo.sufeeds.sufeeds.dao.PostDAO;
import com.syengo.sufeeds.sufeeds.models.CourseClass;
import com.syengo.sufeeds.sufeeds.models.Post;
import com.syengo.sufeeds.sufeeds.models.Student;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

public class DashboardController {
    @FXML private Label welcomeLabel;
    @FXML private ListView<CourseClass> classListView;
    @FXML private VBox classDetailsSection;
    @FXML private VBox welcomeSection;
    @FXML private Label selectedClassLabel;
    @FXML private ComboBox<Integer> weekComboBox;
    @FXML private VBox postsContainer;
    @FXML private VBox globalFeedContainer;
    @FXML private  Label errorLabel;
    @FXML private VBox homeView;
    @FXML private VBox classesView;
    @FXML private VBox newPostView;
    @FXML private VBox profileView;
    @FXML private Label userNameLabel;
    @FXML private Label userEmailLabel;
    @FXML private TextField postTopic;
    @FXML private TextArea postContent;
    @FXML private ComboBox<CourseClass> classSelector;
    @FXML private Label profileNameLabel ;
    @FXML private Label profileEmailLabel;
    @FXML private Label profileStudentIdLabel;
    @FXML private VBox profilePostsContainer;

    private Student currentStudent;
    private ClassDAO classDAO = new ClassDAO();
    private PostDAO postDAO = new PostDAO();
    private CourseClass selectedClass;
    @FXML
    public void initialize() {

        initializeWeekComboBox();

    }
    @FXML
    private void showHome(ActionEvent event) {
        updateActiveTab((Button) event.getSource());
        homeView.setVisible(true);
        classesView.setVisible(false);
        newPostView.setVisible(false);
        profileView.setVisible(false);
        loadGlobalFeedPosts();
    }

    @FXML
    private void showClasses(ActionEvent event) {
        updateActiveTab((Button) event.getSource());
        homeView.setVisible(false);
        classesView.setVisible(true);
        newPostView.setVisible(false);
        profileView.setVisible(false);
        loadClasses();
    }





    @FXML
    private void showProfile(ActionEvent event) {
        updateActiveTab((Button) event.getSource());
        homeView.setVisible(false);
        classesView.setVisible(false);
        newPostView.setVisible(false);
        profileView.setVisible(true);
        profileNameLabel.setText(currentStudent.getFullName());
        profileEmailLabel.setText(currentStudent.getEmail());
        profileStudentIdLabel.setText(String.valueOf(currentStudent.getStudentId()));

        loadUserPosts();
    }
    private void loadUserPosts() {
        profilePostsContainer.getChildren().clear();
        List<Post> userPosts = postDAO.getPostsByStudent(currentStudent.getStudentId());

        if (userPosts.isEmpty()) {
            Label emptyLabel = new Label("You haven't posted anything yet");
            emptyLabel.getStyleClass().add("empty-placeholder");
            profilePostsContainer.getChildren().add(emptyLabel);
        } else {
            for (Post post : userPosts) {
                createProfilePostView(post);
            }
        }
    }
    private void createProfilePostView(Post post) {
        VBox postBox = new VBox(10);
        postBox.getStyleClass().add("post-box");
        postBox.setPadding(new Insets(15));
        Label classInfo = new Label("Week " + post.getWeekNumber());
        classInfo.getStyleClass().add("post-class-info");
        Label topicLabel = new Label(post.getTopic());
        topicLabel.getStyleClass().add("post-topic");
        topicLabel.setWrapText(true);
        Label contentLabel = new Label(post.getContent());
        contentLabel.getStyleClass().add("post-content");
        contentLabel.setWrapText(true);
        Label timestamp = new Label(formatTimestamp(Timestamp.valueOf(post.getPostDate())));
        timestamp.getStyleClass().add("post-timestamp");
        HBox actionBox = new HBox(10);
        actionBox.setAlignment(Pos.CENTER_LEFT);
        Button editButton = new Button("Edit");
        editButton.getStyleClass().addAll("secondary-button", "small-button");
        editButton.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.EDIT));
        Button deleteButton = new Button("Delete");
        deleteButton.getStyleClass().addAll("danger-button", "small-button");
        deleteButton.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.TRASH));
        editButton.setOnAction(e -> handleEditPost(post));
        deleteButton.setOnAction(e -> handleDeleteProfilePost(post));
        actionBox.getChildren().addAll(editButton, deleteButton);
        postBox.getChildren().addAll(classInfo, topicLabel, contentLabel, timestamp, actionBox);
        profilePostsContainer.getChildren().add(postBox);
    }
    private void handleDeleteProfilePost(Post post) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Post");
        alert.setHeaderText("Are you sure you want to delete this post?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (postDAO.deletePost(post.getPostId(), currentStudent.getStudentId())) {
                    loadUserPosts(); // Refresh the posts list
                } else {
                    showError("Error deleting post");
                }
            }
        });
    }
    public void initData(Student student) {
        this.currentStudent = student;
        userNameLabel.setText(student.getFullName());
        userEmailLabel.setText(student.getEmail());

        loadClasses();
        setupClassListViewListener();
        loadGlobalFeedPosts();
    }
    private void initializeWeekComboBox() {
        List<Integer> weeks = IntStream.rangeClosed(1, 14).boxed().toList();
        weekComboBox.setItems(FXCollections.observableArrayList(weeks));

        weekComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldWeek, newWeek) -> {
            if (newWeek != null && selectedClass != null) {
                loadPosts(selectedClass, newWeek);
            }
        });
    }

    private void createGlobalFeedPostView(Post post, CourseClass courseClass) {
        VBox postBox = new VBox(10);
        postBox.getStyleClass().add("post-box");
        postBox.setPadding(new Insets(15));
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Circle avatar = new Circle(20);
        avatar.setFill(Color.BEIGE);
        ImagePattern pattern = new ImagePattern(new Image(getClass().getResource("/assets/loginimage.png").toExternalForm()));

        avatar.setFill(pattern);
        VBox authorInfo = new VBox(2);
        Label authorName = new Label(post.getAuthorName());
        authorName.getStyleClass().add("post-author-name");
        Label authorEmail = new Label("@" + post.getAuthorEmail().split("@")[0]);
        authorEmail.getStyleClass().add("post-author-handle");
        authorInfo.getChildren().addAll(authorName, authorEmail);

        Label classInfo = new Label(post.getClassName() + " • Week " + post.getWeekNumber());
        classInfo.getStyleClass().add("post-class-info");

        headerBox.getChildren().addAll(avatar, authorInfo, new Region(), classInfo);


        Label topicLabel = new Label(post.getTopic());
        topicLabel.getStyleClass().add("post-topic");
        topicLabel.setWrapText(true);


        Label contentLabel = new Label(post.getContent());
        contentLabel.getStyleClass().add("post-content");
        contentLabel.setWrapText(true);

        Label timestamp = new Label(formatTimestamp(Timestamp.valueOf(post.getPostDate())));
        timestamp.getStyleClass().add("post-timestamp");

        // Action buttons
        HBox actionBox = new HBox(20);
        actionBox.setAlignment(Pos.CENTER_LEFT);
        // I was excited for the project and wanted to add these but I don't want to code anymore

        Button replyButton = createActionButton("💬", "Reply");
        Button shareButton = createActionButton("🔄", "Share");
        Button likeButton = createActionButton("❤️", "Like");

        actionBox.getChildren().addAll(replyButton, shareButton, likeButton);

        postBox.getChildren().addAll(headerBox, topicLabel, contentLabel, timestamp, actionBox);

        postBox.setOnMouseEntered(e -> postBox.setStyle("-fx-background-color: #f8f9fa;"));
        postBox.setOnMouseExited(e -> postBox.setStyle("-fx-background-color: white;"));

        globalFeedContainer.getChildren().add(postBox);
    }

    private Button createActionButton(String icon, String tooltip) {
        Button button = new Button(icon);
        button.getStyleClass().add("post-action-button");
        Tooltip.install(button, new Tooltip(tooltip));
        return button;
    }

    private String formatTimestamp(Timestamp timestamp) {
        LocalDateTime postTime = timestamp.toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(postTime, now);

        if (duration.toMinutes() < 60) {
            return duration.toMinutes() + "m";
        } else if (duration.toHours() < 24) {
            return duration.toHours() + "h";
        } else if (duration.toDays() < 7) {
            return duration.toDays() + "d";
        } else {
            return postTime.format(DateTimeFormatter.ofPattern("MMM d"));
        }
    }
    private void loadClasses() {
        List<CourseClass> classes = classDAO.getClassesByStudent(currentStudent.getStudentId());
        classListView.setItems(FXCollections.observableArrayList(classes));

        Label emptyPlaceholder = new Label("No classes added yet");
        emptyPlaceholder.getStyleClass().add("empty-placeholder");
        classListView.setPlaceholder(emptyPlaceholder);

        classListView.setFixedCellSize(95);

        classListView.setCellFactory(param -> new ListCell<CourseClass>() {
            @Override
            protected void updateItem(CourseClass item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox content = new VBox(5);
                    content.getStyleClass().add("class-cell-content");

                    Label className = new Label(item.getClassName());
                    className.getStyleClass().add("class-name");

                    Label semesterInfo = new Label(item.getSemester());
                    semesterInfo.getStyleClass().add("semester-info");

                    content.getChildren().addAll(className, semesterInfo);
                    setGraphic(content);
                }
            }
        });
    }


    private void setupClassListViewListener() {
        classListView.getSelectionModel().selectedItemProperty().addListener((obs, oldClass, newClass) -> {
            if (newClass != null) {
                selectedClass = newClass;
                showClassDetails(newClass);
            }
        });
    }

    private void showClassDetails(CourseClass courseClass) {
        selectedClassLabel.setText(courseClass.getClassName() + " (" + courseClass.getSemester() + ")");
        classDetailsSection.setVisible(true);
        weekComboBox.getSelectionModel().selectFirst();
    }
    private void loadPosts(CourseClass courseClass, int weekNumber) {
        postsContainer.getChildren().clear();
        List<Post> posts = postDAO.getPostsByWeek(courseClass.getClassId(), weekNumber);

        for (Post post : posts) {
            createPostView(post);
        }
    }
    private void loadGlobalFeedPosts() {
        globalFeedContainer.getChildren().clear();
        List<Post> allPosts = postDAO.getPostsByClass(); // You'll need to add this method to PostDAO

        allPosts.forEach(post -> {
            CourseClass courseClass = classDAO.getClassById(post.getClassId());
            createGlobalFeedPostView(post, courseClass);
        });
    }

    private void createPostView(Post post) {
        VBox postBox = new VBox(10);
        postBox.getStyleClass().add("post-box");
        postBox.setPadding(new Insets(15));

        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        Circle avatar = new Circle(20);

        VBox authorInfo = new VBox(2);
        Label authorName = new Label(post.getAuthorName());
        authorName.getStyleClass().add("post-author-name");

        Label timestamp = new Label(formatTimestamp(Timestamp.valueOf(post.getPostDate())));
        timestamp.getStyleClass().add("post-timestamp");

        authorInfo.getChildren().addAll(authorName, timestamp);

        headerBox.getChildren().addAll(avatar, authorInfo);
        Label topicLabel = new Label(post.getTopic());
        topicLabel.getStyleClass().add("post-topic");
        topicLabel.setWrapText(true);

        Label contentLabel = new Label(post.getContent());
        contentLabel.getStyleClass().add("post-content");
        contentLabel.setWrapText(true);

        HBox actionBox = new HBox(10);
        actionBox.setAlignment(Pos.CENTER_LEFT);

        if (post.getStudentId().equals(currentStudent.getStudentId())) {
            Button editButton = new Button("Edit");
            Button deleteButton = new Button("Delete");
            editButton.getStyleClass().add("post-action-button");
            deleteButton.getStyleClass().add("post-action-button");

            editButton.setOnAction(e -> handleEditPost(post));
            deleteButton.setOnAction(e -> handleDeletePost(post));

            actionBox.getChildren().addAll(editButton, deleteButton);
        }

        postBox.getChildren().addAll(headerBox, topicLabel, contentLabel, actionBox);
        postsContainer.getChildren().add(postBox);
    }

    @FXML
    private void handleAddClass() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_class.fxml"));
            Parent addClassView = loader.load();
            AddClassController controller = loader.getController();
            controller.initData(currentStudent, () -> loadClasses());

            Scene addClassScene = new Scene(addClassView);
            addClassScene.getStylesheets().add(getClass().getResource("/add_class.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Add New Class");
            stage.setScene(addClassScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error opening add class window");
        }
    }

    @FXML
    private void handleAddPost() {
        CourseClass selectedClass = classSelector.getValue();
        Integer selectedWeek = weekComboBox.getValue();
        String topic = postTopic.getText();
        String content = postContent.getText();

        // Debug prints cause it was not working
        System.out.println("Selected Class: " + selectedClass);
        System.out.println("Selected Week: " + selectedWeek);
        System.out.println("Topic: " + topic);
        System.out.println("Content: " + content);

        if (selectedClass == null) {
            showError("Please select a class");
            return;
        }

        // Validate week selection
        if (selectedWeek == null) {
            showError("Please select a week");
            return;
        }

        // Validate topic
        if (topic == null || topic.trim().isEmpty()) {
            showError("Please enter a topic");
            return;
        }

        // Validate content
        if (content == null || content.trim().isEmpty()) {
            showError("Please enter content");
            return;
        }

        try {
            Post newPost = new Post();
            newPost.setClassId(selectedClass.getClassId());
            newPost.setStudentId(currentStudent.getStudentId());
            newPost.setWeekNumber(selectedWeek);
            newPost.setTopic(topic);
            newPost.setContent(content);

            System.out.println("Attempting to create post with:");
            System.out.println("Class ID: " + newPost.getClassId());
            System.out.println("Student ID: " + newPost.getStudentId());
            System.out.println("Week: " + newPost.getWeekNumber());
            System.out.println("Topic: " + newPost.getTopic());
            System.out.println("Content: " + newPost.getContent());

            if (postDAO.createPost(newPost)) {
                // Clear the form
                postTopic.clear();
                postContent.clear();
                classSelector.setValue(null);
                weekComboBox.setValue(null);

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Post created successfully!");
                alert.showAndWait();



            } else {
                showError("Failed to create post");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error creating post: " + e.getMessage());
        }
    }
    @FXML
    private void updateActiveTab(Button selectedButton) {
        // Remove active class from all buttons
        for (Node node : ((VBox) selectedButton.getParent()).getChildren()) {
            if (node instanceof Button) {
                node.getStyleClass().remove("active-nav-item");
            }
        }

        selectedButton.getStyleClass().add("active-nav-item");
    }
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent loginView = loader.load();
            Scene loginScene = new Scene(loginView);

            loginScene.getStylesheets().add(getClass().getResource("/login.css").toExternalForm());

            Stage stage = (Stage) homeView.getScene().getWindow();
            stage.setScene(loginScene);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading login screen");
        }
    }

    private void handleEditPost(Post post) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edit_post.fxml"));
            Parent editPostView = loader.load();

            EditPostController controller = loader.getController();

            controller.initData(post, () -> {
                // Check if we're in the class view
                if (classesView.isVisible() && selectedClass != null) {
                    if (weekComboBox.getValue() != null) {
                        loadPosts(selectedClass, weekComboBox.getValue());
                    } else {
                        weekComboBox.setValue(post.getWeekNumber());
                        loadPosts(selectedClass, post.getWeekNumber());
                    }
                }
                // If we're in the profile view
                else if (profileView.isVisible()) {
                    loadUserPosts();
                }
                // If we're in the home view
                else if (homeView.isVisible()) {
                    loadGlobalFeedPosts();
                }
            });

            Scene editPostScene = new Scene(editPostView);
            editPostScene.getStylesheets().add(getClass().getResource("/edit_post.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Edit Post");
            stage.setScene(editPostScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error opening edit post window");
        }
    }
    private void handleDeletePost(Post post) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Post");
        alert.setHeaderText("Are you sure you want to delete this post?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (postDAO.deletePost(post.getPostId(), currentStudent.getStudentId())) {
                    loadPosts(selectedClass, weekComboBox.getValue());
                } else {
                    showError("Error deleting post");
                }
            }
        });
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleEditClass() {
        CourseClass selectedClass = classListView.getSelectionModel().getSelectedItem();
        if (selectedClass == null) {
            showError("Please select a class to edit");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edit_class.fxml"));
            Parent editClassView = loader.load();

            EditClassController controller = loader.getController();
            controller.initData(selectedClass, () -> loadClasses());

            Scene editClassScene = new Scene(editClassView);
            editClassScene.getStylesheets().add(getClass().getResource("/edit_class.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Edit Class");
            stage.setScene(editClassScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error opening edit class window");
        }
    }
    @FXML
    private void handleDeleteClass() {
        CourseClass selectedClass = classListView.getSelectionModel().getSelectedItem();
        if (selectedClass == null) {
            showError("Please select a class to delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Class");
        alert.setHeaderText("Are you sure you want to delete this class?");
        alert.setContentText("This will delete all posts associated with this class.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (classDAO.deleteClass(selectedClass.getClassId(), currentStudent.getStudentId())) {
                    loadClasses();
                } else {
                    showError("Error deleting class");
                }
            }

        });
    }

    @FXML
    private void showNewPost(ActionEvent event) {
        updateActiveTab((Button) event.getSource());
        homeView.setVisible(false);
        classesView.setVisible(false);
        newPostView.setVisible(true);
        profileView.setVisible(false);

        classSelector.setCellFactory(param -> new ListCell<CourseClass>() {
            @Override
            protected void updateItem(CourseClass item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getClassName() + " (" + item.getSemester() + ")");
                }
                if (!newPostView.getStylesheets().contains("/dashboard.css")) {
                    newPostView.getStylesheets().add(getClass().getResource("/dashboard.css").toExternalForm());
                }
            }
        });


        classSelector.setButtonCell(new ListCell<CourseClass>() {
            @Override
            protected void updateItem(CourseClass item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getClassName() + " (" + item.getSemester() + ")");
                }
            }
        });


        classSelector.setItems(FXCollections.observableArrayList(
            classDAO.getClassesByStudent(currentStudent.getStudentId())
        ));

        if (weekComboBox.getItems().isEmpty()) {
            List<Integer> weeks = IntStream.rangeClosed(1, 14).boxed().toList();
            weekComboBox.setItems(FXCollections.observableArrayList(weeks));
        }

        postTopic.clear();
        postContent.clear();
        classSelector.setValue(null);
        weekComboBox.setValue(null);
    }


    @FXML
    private void handleEditProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edit_profile.fxml"));
            Parent editProfileView = loader.load();

            EditProfileController controller = loader.getController();
            controller.initData(currentStudent, () -> {
                welcomeLabel.setText("Welcome, " + currentStudent.getFullName());
            });

            Scene editProfileScene = new Scene(editProfileView);
            editProfileScene.getStylesheets().add(getClass().getResource("/edit_profile.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Edit Profile");
            stage.setScene(editProfileScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error opening edit profile window");
        }
    }
}
