package com.syengo.sufeeds.sufeeds.ui.controllers;

import com.syengo.sufeeds.sufeeds.dao.ClassDAO;
import com.syengo.sufeeds.sufeeds.models.CourseClass;
import com.syengo.sufeeds.sufeeds.models.Student;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;

public class AddClassController {
    @FXML private TextField classNameField;
    @FXML private ComboBox<String> semesterComboBox;
    @FXML private Label errorLabel;


    private Student currentStudent;
    private final ClassDAO classDAO = new ClassDAO();
    private Runnable onSaveCallback;



    public void initData(Student student, Runnable onSaveCallback) {
        this.currentStudent = student;
        this.onSaveCallback = onSaveCallback;
        initializeSemesterComboBox();
    }

    private void initializeSemesterComboBox() {
        semesterComboBox.setItems(FXCollections.observableArrayList(
            "Semester 1",
            "Semester 2",
            "Semester 3"
        ));
    }

    @FXML
    private void handleSave() {
        String className = classNameField.getText().trim();
        String semester = semesterComboBox.getValue();

        if (className.isEmpty() || semester == null) {
            showError("Please fill in all fields");
            return;
        }

        CourseClass newClass = new CourseClass();
        newClass.setClassName(className);
        newClass.setSemester(semester);
        newClass.setStudentId(currentStudent.getStudentId());

        if (classDAO.addClass(newClass)) {
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            closeWindow();
        } else {
            showError("Error adding class");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void closeWindow() {
        Stage stage = (Stage) classNameField.getScene().getWindow();
        stage.close();
    }
}
