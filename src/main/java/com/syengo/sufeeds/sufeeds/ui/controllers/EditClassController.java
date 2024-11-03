package com.syengo.sufeeds.sufeeds.ui.controllers;

import com.syengo.sufeeds.sufeeds.dao.ClassDAO;
import com.syengo.sufeeds.sufeeds.models.CourseClass;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditClassController {
    @FXML private TextField classNameField;
    @FXML private TextField semesterField;

    private CourseClass courseClass;
    private ClassDAO classDAO = new ClassDAO();
    private Runnable onSaveCallback;

    public void initData(CourseClass courseClass, Runnable onSaveCallback) {
        this.courseClass = courseClass;
        this.onSaveCallback = onSaveCallback;


        classNameField.setText(courseClass.getClassName());
        semesterField.setText(courseClass.getSemester());
    }

    @FXML
    private void handleSave() {
        if (classNameField.getText().isEmpty() || semesterField.getText().isEmpty()) {
            showError("All fields are required");
            return;
        }

        courseClass.setClassName(classNameField.getText());
        courseClass.setSemester(semesterField.getText());

        if (classDAO.updateClass(courseClass)) {
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            closeWindow();
        } else {
            showError("Failed to update class");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) classNameField.getScene().getWindow()).close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
