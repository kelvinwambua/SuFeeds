package com.syengo.sufeeds.sufeeds.ui;

import com.syengo.sufeeds.sufeeds.models.CourseClass;
import com.syengo.sufeeds.sufeeds.models.Student;

import com.syengo.sufeeds.sufeeds.models.CourseTopic;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class DashboardForm {
    private Student student;
    private VBox root;
    private TabPane tabPane;

    public DashboardForm(Student student) {
        this.student = student;
        createUI();
    }

    private void createUI() {
        root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        // Header
        VBox headerBox = createHeader();

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        // Tabs
        Tab homeTab = createHomeTab();
        Tab classesTab = createClassesTab();
        Tab topicsTab = createTopicsTab();
        Tab profileTab = createProfileTab();

        tabPane.getTabs().addAll(homeTab, classesTab, topicsTab, profileTab);

        // Footer with logout button
        HBox footer = createFooter();

        // copyright text
        Text copyrightText = new Text("© 2024 SU Feeds. All rights reserved.");
        copyrightText.setStyle("-fx-font-size: 10px;");

        root.getChildren().addAll(headerBox, tabPane, footer, copyrightText);
    }

    private VBox createHeader() {
        VBox headerBox = new VBox(5);
        headerBox.setAlignment(Pos.CENTER);

        Text welcomeText = new Text("Welcome, " + student.getName());
        welcomeText.setFont(Font.font("System", FontWeight.BOLD, 20));

        Text infoText = new Text("Admission No: " + student.getAdmissionNo() +
            " | Course: " + student.getCourse());
        infoText.setFont(Font.font("System", 14));

        headerBox.getChildren().addAll(welcomeText, infoText, new Separator());
        return headerBox;
    }

    private Tab createHomeTab() {
        Tab tab = new Tab("Home");
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        // Quick stats
        VBox statsBox = new VBox(10);
        statsBox.getChildren().addAll(
            new Label("Your Quick Stats"),
            new Text("Total Classes: 5"),  // TODO:Replace with actual data
            new Text("Topics This Week: 3"),  //TODO: Replace with actual data
            new Text("Latest Activity: Added new topic in Programming")  //TODO: Replace with actual data
        );

        // Recent activity
        VBox activityBox = new VBox(10);
        activityBox.getChildren().addAll(
            new Label("Recent Activity"),
            createActivityItem("Added new topic in Java Programming", "2 hours ago"),
            createActivityItem("Updated feedback for Database Systems", "Yesterday"),
            createActivityItem("Added new class: Web Development", "2 days ago")
        );

        content.getChildren().addAll(statsBox, new Separator(), activityBox);
        tab.setContent(content);
        return tab;
    }

    private Tab createClassesTab() {
        Tab tab = new Tab("Classes");
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));


        Button addClassButton = new Button("Add New Class");
        addClassButton.setOnAction(e -> showClassManagement());


        ListView<CourseClass> classList = new ListView<>();
        VBox.setVgrow(classList, Priority.ALWAYS);

        Button refreshButton = new Button("Refresh Classes");
        refreshButton.setOnAction(e -> refreshClasses(classList));

        content.getChildren().addAll(
            addClassButton,
            new Label("Your Classes"),
            classList,
            refreshButton
        );

        tab.setContent(content);
        return tab;
    }

    private Tab createTopicsTab() {
        Tab tab = new Tab("Topics");
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        ComboBox<CourseClass> classSelector = new ComboBox<>();
        classSelector.setPromptText("Select a class");

        ListView<CourseTopic> topicsList = new ListView<>();
        VBox.setVgrow(topicsList, Priority.ALWAYS);

        // Add Topic Button
        Button addTopicButton = new Button("Add New Topic");
        addTopicButton.setOnAction(e -> {
            CourseClass selectedClass = classSelector.getValue();
            if (selectedClass != null) {
                showAddTopicDialog(selectedClass);
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning",
                    "Please select a class first!");
            }
        });

        content.getChildren().addAll(
            classSelector,
            addTopicButton,
            new Label("Topics"),
            topicsList
        );

        tab.setContent(content);
        return tab;
    }

    private Tab createProfileTab() {
        Tab tab = new Tab("Profile");
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        // Profile Information
        GridPane profileGrid = new GridPane();
        profileGrid.setHgap(10);
        profileGrid.setVgap(10);
        profileGrid.addRow(0, new Label("Name:"), new Text(student.getName()));
        profileGrid.addRow(1, new Label("Admission No:"),
            new Text(student.getAdmissionNo()));
        profileGrid.addRow(2, new Label("Course:"), new Text(student.getCourse()));

        // Edit Profile Button
        Button editProfileButton = new Button("Edit Profile");
        editProfileButton.setOnAction(e -> showEditProfileDialog());


        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setOnAction(e -> showChangePasswordDialog());

        content.getChildren().addAll(
            new Label("Profile Information"),
            profileGrid,
            editProfileButton,
            changePasswordButton
        );

        tab.setContent(content);
        return tab;
    }

    private HBox createFooter() {
        HBox footer = new HBox(10);
        footer.setAlignment(Pos.CENTER_RIGHT);

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> logout());

        footer.getChildren().add(logoutButton);
        return footer;
    }

    private HBox createActivityItem(String activity, String time) {
        HBox item = new HBox(10);
        item.setAlignment(Pos.CENTER_LEFT);

        Text activityText = new Text(activity);
        Text timeText = new Text(time);
        timeText.setStyle("-fx-fill: gray;");

        item.getChildren().addAll(activityText, timeText);
        return item;
    }

    private void showClassManagement() {
        Stage currentStage = (Stage) root.getScene().getWindow();
        ClassManagementForm classManagementForm = new ClassManagementForm(student);
        Scene scene = new Scene(classManagementForm.getRoot(), 800, 600);
        currentStage.setScene(scene);
    }

    private void refreshClasses(ListView<CourseClass> classList) {
        // TODO:Implement refresh logic using ClassDAO
        classList.getItems().clear();
    }

    private void showAddTopicDialog(CourseClass selectedClass) {
        Dialog<CourseTopic> dialog = new Dialog<>();
        dialog.setTitle("Add New Topic");
        dialog.setHeaderText("Add a new topic for " + selectedClass.getClassName());

    }

    private void showEditProfileDialog() {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Edit Profile");
        dialog.setHeaderText("Update your profile information");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

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

                return student;
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showChangePasswordDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Enter your new password");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

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
                    showAlert(Alert.AlertType.ERROR, "Error",
                        "New passwords do not match!");
                    return null;
                }
                return newPassword.getText();
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void logout() {
        Stage currentStage = (Stage) root.getScene().getWindow();
        LoginForm loginForm = new LoginForm();
        Scene scene = new Scene(loginForm.getRoot(), 300, 200);
        currentStage.setScene(scene);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public VBox getRoot() {
        return root;
    }
}
