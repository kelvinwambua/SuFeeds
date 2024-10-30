package com.syengo.sufeeds.sufeeds.ui.controllers;

import com.syengo.sufeeds.sufeeds.models.CourseClass;
import com.syengo.sufeeds.sufeeds.models.CourseTopic;
import com.syengo.sufeeds.sufeeds.models.Student;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class ClassManagementController {
    @FXML private Text studentInfoText;
    @FXML private ListView<CourseClass> classListView;
    @FXML private Text classNameText;
    @FXML private Text courseCodeText;
    @FXML private Text semesterText;
    @FXML private Text academicYearText;
    @FXML private ListView<CourseTopic> topicsListView;

    private Student student;

    public void initData(Student student) {
        this.student = student;
        updateUI();
        setupListeners();
    }

    private void updateUI() {
        studentInfoText.setText(student.getName() + " - " + student.getAdmissionNo());
        refreshClassList();
    }

    private void setupListeners() {
        classListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    updateClassDetails(newValue);
                }
            }
        );
    }

    private void refreshClassList() {
        classListView.getItems().clear();
        // TODO: Load classes from database
        // classListView.getItems().addAll(classes);
    }

    private void updateClassDetails(CourseClass courseClass) {
//        classNameText.setText(courseClass.getClassName());
//        courseCodeText.setText(courseClass.getCourseCode());
//        semesterText.setText(courseClass.getSemester());
//        academicYearText.setText(courseClass.getAcademicYear());
//
//
//        topicsListView.getItems().clear();
//        topicsListView.getItems().addAll(courseClass.getTopics());
    }

    @FXML
    private void showAddClassDialog() {
        Dialog<CourseClass> dialog = new Dialog<>();
        dialog.setTitle("Add New Class");
        dialog.setHeaderText("Enter class details");

        // Create the custom dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField className = new TextField();
        TextField courseCode = new TextField();
        TextField semester = new TextField();
        TextField academicYear = new TextField();

        grid.addRow(0, new Label("Class Name:"), className);
        grid.addRow(1, new Label("Course Code:"), courseCode);
        grid.addRow(2, new Label("Semester:"), semester);
        grid.addRow(3, new Label("Academic Year:"), academicYear);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                CourseClass newClass = new CourseClass();
                newClass.setClassName(className.getText());
                newClass.setSemester(courseCode.getText());
                newClass.setSemester(semester.getText());
                newClass.setDescription(academicYear.getText());
                return newClass;
            }
            return null;
        });

        Optional<CourseClass> result = dialog.showAndWait();
        result.ifPresent(courseClass -> {
            // TODO: Save class to database
            refreshClassList();
        });
    }

    @FXML
    private void showEditClassDialog() {
        CourseClass selectedClass = classListView.getSelectionModel().getSelectedItem();
        if (selectedClass == null) {
            showAlert(Alert.AlertType.WARNING, "Warning",
                "Please select a class to edit!");
            return;
        }

        Dialog<CourseClass> dialog = new Dialog<>();
        dialog.setTitle("Edit Class");
        dialog.setHeaderText("Edit class details");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField className = new TextField(selectedClass.getClassName());
        TextField courseCode = new TextField(selectedClass.getClassName());
        TextField semester = new TextField(selectedClass.getSemester());
        TextField academicYear = new TextField(selectedClass.getSemester());

        grid.addRow(0, new Label("Class Name:"), className);
        grid.addRow(1, new Label("Course Code:"), courseCode);
        grid.addRow(2, new Label("Semester:"), semester);
        grid.addRow(3, new Label("Academic Year:"), academicYear);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                selectedClass.setClassName(className.getText());
                selectedClass.setDescription(courseCode.getText());
                selectedClass.setSemester(semester.getText());
                selectedClass.setTimeSlot(academicYear.getText());
                return selectedClass;
            }
            return null;
        });

        Optional<CourseClass> result = dialog.showAndWait();
        result.ifPresent(courseClass -> {
            // TODO: Update class in database
            refreshClassList();
        });
    }

    @FXML
    private void handleDeleteClass() {
        CourseClass selectedClass = classListView.getSelectionModel().getSelectedItem();
        if (selectedClass == null) {
            showAlert(Alert.AlertType.WARNING, "Warning",
                "Please select a class to delete!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Class");
        alert.setHeaderText("Delete " + selectedClass.getClassName());
        alert.setContentText("Are you sure you want to delete this class? " +
            "This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // TODO: Delete class from database
            refreshClassList();
        }
    }

    @FXML
    private void returnToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.initData(student);

            Stage stage = (Stage) studentInfoText.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        } catch (IOException e) {
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
