package com.syengo.sufeeds.sufeeds.ui.controllers;

import com.syengo.sufeeds.sufeeds.dao.StudentDAO;
import com.syengo.sufeeds.sufeeds.models.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.time.LocalDateTime;

public class RegistrationController {
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    private StudentDAO studentDAO = new StudentDAO();

    @FXML
    private void handleRegister(ActionEvent event) {
        // Clear previous error messages
        errorLabel.setVisible(false);

        // Get field values
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate input fields
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            showError("Please enter a valid email address");
            return;
        }

        // Validate password match
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        // Check if email already exists
        if (studentDAO.emailExists(email)) {
            showError("Email already registered");
            return;
        }

        // Create new student
        Student newStudent = new Student();
        newStudent.setFullName(fullName);
        newStudent.setEmail(email);
        newStudent.setPassword(password);

        // Attempt registration
        if (studentDAO.register(newStudent)) {
            try {
                // Registration successful, navigate to login
                Parent login = FXMLLoader.load(getClass().getResource("/login.fxml"));
                Stage stage = (Stage) fullNameField.getScene().getWindow();
                Scene scene = new Scene(login);
                scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                showError("Error navigating to login page");
            }
        } else {
            showError("Registration failed. Please try again.");
        }
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        try {
            Parent login = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Stage stage = (Stage) fullNameField.getScene().getWindow();
            Scene scene = new Scene(login);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error returning to login page");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private boolean isValidEmail(String email) {
        // Basic email validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
}
