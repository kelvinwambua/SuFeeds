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

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private StudentDAO studentDAO = new StudentDAO();

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        Student student = studentDAO.login(email, password);
        if (student != null) {
            try {
                // Load the dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
                Parent dashboard = loader.load();

                // Pass the logged-in student to the dashboard controller
                DashboardController dashboardController = loader.getController();
                dashboardController.initData(student);


                Scene dashboardScene = new Scene(dashboard);
                dashboardScene.getStylesheets().add(getClass().getResource("/dashboard.css").toExternalForm());

                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(dashboardScene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                showError("Error loading dashboard");
            }
        } else {
            showError("Invalid email or password");
        }
    }

    @FXML
    private void handleRegisterNavigation(ActionEvent event) {
        try {
            Parent register = FXMLLoader.load(getClass().getResource("/registration.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            register.getStylesheets().add(getClass().getResource("/login.css").toExternalForm());
            stage.setScene(new Scene(register));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading registration page");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
