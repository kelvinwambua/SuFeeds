package com.syengo.sufeeds.sufeeds.ui;


import com.syengo.sufeeds.sufeeds.dao.StudentDAO;
import com.syengo.sufeeds.sufeeds.models.Student;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class LoginForm {
    private TextField admissionField;
    private PasswordField passwordField;
    private GridPane root;

    public LoginForm() {
        createUI();
    }

    private void createUI() {
        root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(20));

        // Add components
        Label admissionLabel = new Label("Admission No:");
        admissionField = new TextField();

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        // Add to grid
        root.add(admissionLabel, 0, 0);
        root.add(admissionField, 1, 0);
        root.add(passwordLabel, 0, 1);
        root.add(passwordField, 1, 1);
        root.add(loginButton, 0, 2);
        root.add(registerButton, 1, 2);

        // Add action handlers
        loginButton.setOnAction(e -> handleLogin());
        registerButton.setOnAction(e -> showRegisterForm());
    }

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

    private void showRegisterForm() {
        // Get the current stage
        Stage currentStage = (Stage) root.getScene().getWindow();

        // Create and show registration form
        RegistrationForm registrationForm = new RegistrationForm();
        Scene scene = new Scene(registrationForm.getRoot(), 400, 300);
        currentStage.setScene(scene);
    }

    private void showDashboard(Student student) {
        Stage currentStage = (Stage) root.getScene().getWindow();
        DashboardForm dashboardForm = new DashboardForm(student);
        Scene scene = new Scene(dashboardForm.getRoot(), 400, 300);
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
