package com.syengo.sufeeds.sufeeds.ui;

import com.syengo.sufeeds.sufeeds.dao.StudentDAO;
import com.syengo.sufeeds.sufeeds.models.Student;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class RegistrationForm {
    private TextField nameField;
    private TextField admissionField;
    private TextField courseField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private GridPane root;

    public RegistrationForm() {
        createUI();
    }

    private void createUI() {
        root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(20));

        // Create fields
        nameField = new TextField();
        admissionField = new TextField();
        courseField = new TextField();
        passwordField = new PasswordField();
        confirmPasswordField = new PasswordField();

        // Add components to grid
        root.add(new Label("Name:"), 0, 0);
        root.add(nameField, 1, 0);

        root.add(new Label("Admission No:"), 0, 1);
        root.add(admissionField, 1, 1);

        root.add(new Label("Course:"), 0, 2);
        root.add(courseField, 1, 2);

        root.add(new Label("Password:"), 0, 3);
        root.add(passwordField, 1, 3);

        root.add(new Label("Confirm Password:"), 0, 4);
        root.add(confirmPasswordField, 1, 4);

        Button registerButton = new Button("Register");
        Button backButton = new Button("Back to Login");

        root.add(registerButton, 0, 5);
        root.add(backButton, 1, 5);

        // Add action handlers
        registerButton.setOnAction(e -> handleRegistration());
        backButton.setOnAction(e -> showLoginForm());
    }

    private void handleRegistration() {
        String name = nameField.getText();
        String admissionNo = admissionField.getText();
        String course = courseField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validation
        if (name.isEmpty() || admissionNo.isEmpty() || course.isEmpty() ||
            password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields are required!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match!");
            return;
        }

        // Create student object
        Student student = new Student(name, admissionNo, course, password);

        // Register student
        StudentDAO dao = new StudentDAO();
        if (dao.register(student)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful!");
            showLoginForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Registration failed!");
        }
    }

    private void showLoginForm() {
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

    public GridPane getRoot() {
        return root;
    }
}
