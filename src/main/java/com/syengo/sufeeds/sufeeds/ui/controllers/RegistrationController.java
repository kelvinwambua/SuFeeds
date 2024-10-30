package com.syengo.sufeeds.sufeeds.ui.controllers;

import com.syengo.sufeeds.sufeeds.dao.StudentDAO;
import com.syengo.sufeeds.sufeeds.models.Student;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistrationController {
    @FXML private TextField nameField;
    @FXML private TextField admissionField;
    @FXML private TextField courseField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML
    public void initialize() {
        admissionField.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.getStylesheets().add(getClass().getResource("/registration.css").toExternalForm());
            }
        });
    }

    @FXML
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


    // RegistrationController.java
    @FXML
    private void showLoginForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) admissionField.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 1000);
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } catch (Exception e) {
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
