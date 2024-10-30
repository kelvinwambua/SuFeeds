package com.syengo.sufeeds.sufeeds.ui.controllers;

import com.syengo.sufeeds.sufeeds.dao.StudentDAO;
import com.syengo.sufeeds.sufeeds.models.Student;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField admissionField;
    @FXML private PasswordField passwordField;

    @FXML
    public void initialize() {
        admissionField.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.getStylesheets().add(getClass().getResource("/login.css").toExternalForm());
            }
        });
    }
    @FXML
    private void handleLogin() {
        String admissionNo = admissionField.getText();
        String password = passwordField.getText();

        StudentDAO dao = new StudentDAO();
        Student student = dao.login(admissionNo, password);

        if (student != null) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Login successful!");
            showDashboard(student);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid credentials!");
        }
    }

    @FXML
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(content);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/alert.css").toExternalForm());
        dialogPane.getStyleClass().add("modern-alert");

        if (alertType == Alert.AlertType.ERROR) {
            dialogPane.getStyleClass().add("error");
        } else if (alertType == Alert.AlertType.INFORMATION) {
            dialogPane.getStyleClass().add("information");
        }

        dialogPane.lookupButton(ButtonType.OK).getStyleClass().add("alert-button");

        alert.showAndWait();
    }

    // LoginController.java
    @FXML
    private void showRegisterForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/registration.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) admissionField.getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDashboard(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.initData(student);

            Stage stage = (Stage) admissionField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setFullScreen(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
