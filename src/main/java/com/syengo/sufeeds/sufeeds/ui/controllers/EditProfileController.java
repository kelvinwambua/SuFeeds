package com.syengo.sufeeds.sufeeds.ui.controllers;

import com.syengo.sufeeds.sufeeds.dao.StudentDAO;
import com.syengo.sufeeds.sufeeds.models.Student;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditProfileController {
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;

    private Student student;
    private StudentDAO studentDAO = new StudentDAO();
    private Runnable onSaveCallback;

    public void initData(Student student, Runnable onSaveCallback) {
        this.student = student;
        this.onSaveCallback = onSaveCallback;

        // Populate fields with existing data
        fullNameField.setText(student.getFullName());
        emailField.setText(student.getEmail());
    }

    @FXML
    private void handleSave() {
        if (fullNameField.getText().isEmpty() || emailField.getText().isEmpty() ||
            currentPasswordField.getText().isEmpty()) {
            showError("Required fields cannot be empty");
            return;
        }

        // Verify current password
        if (!currentPasswordField.getText().equals(student.getPassword())) {
            showError("Current password is incorrect");
            return;
        }

        // Check if email already exists (excluding current user)
        String newEmail = emailField.getText();
        if (!newEmail.equals(student.getEmail()) && studentDAO.emailExists(newEmail)) {
            showError("Email already exists");
            return;
        }

        // Validate new password if provided
        if (!newPasswordField.getText().isEmpty()) {
            if (!newPasswordField.getText().equals(confirmPasswordField.getText())) {
                showError("New passwords do not match");
                return;
            }
            student.setPassword(newPasswordField.getText());
        }

        student.setFullName(fullNameField.getText());
        student.setEmail(emailField.getText());

        if (studentDAO.updateStudent(student)) {
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            closeWindow();
        } else {
            showError("Failed to update profile");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) fullNameField.getScene().getWindow()).close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
