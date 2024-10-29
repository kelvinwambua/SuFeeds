package com.syengo.sufeeds.sufeeds.ui;

import com.syengo.sufeeds.sufeeds.models.Student;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import javafx.scene.Scene;

public class DashboardForm {
    private Student student;
    private VBox root;

    public DashboardForm(Student student) {
        this.student = student;
        createUI();
    }

    private void createUI() {
        root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // Welcome section
        VBox welcomeBox = new VBox(5);
        welcomeBox.getChildren().addAll(
            new Text("Welcome, " + student.getName()),
            new Text("Admission No: " + student.getAdmissionNo()),
            new Text("Course: " + student.getCourse())
        );

        // Buttons
        Button profileButton = new Button("View Profile");
        Button coursesButton = new Button("View Courses");
        Button gradesButton = new Button("View Grades");
        Button logoutButton = new Button("Logout");

        profileButton.setMaxWidth(Double.MAX_VALUE);
        coursesButton.setMaxWidth(Double.MAX_VALUE);
        gradesButton.setMaxWidth(Double.MAX_VALUE);
        logoutButton.setMaxWidth(Double.MAX_VALUE);

        // Add action handlers
        profileButton.setOnAction(e -> showProfile());
        coursesButton.setOnAction(e -> showCourses());
        gradesButton.setOnAction(e -> showGrades());
        logoutButton.setOnAction(e -> logout());

        root.getChildren().addAll(
            welcomeBox,
            profileButton,
            coursesButton,
            gradesButton,
            logoutButton
        );
    }

    private void showProfile() {
        showAlert(Alert.AlertType.INFORMATION, "Profile",
            "Student Profile\n\n" +
                "Name: " + student.getName() + "\n" +
                "Admission No: " + student.getAdmissionNo() + "\n" +
                "Course: " + student.getCourse());
    }

    private void showCourses() {
        showAlert(Alert.AlertType.INFORMATION, "Courses",
            "Enrolled Courses\n\n" +
                "1. " + student.getCourse() + " (Main Course)\n" +
                "2. Mathematics 101\n" +
                "3. Computer Programming\n" +
                "4. Communication Skills");
    }

    private void showGrades() {
        showAlert(Alert.AlertType.INFORMATION, "Grades",
            "Current Grades\n\n" +
                "Mathematics 101: A\n" +
                "Computer Programming: B+\n" +
                "Communication Skills: A-");
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
