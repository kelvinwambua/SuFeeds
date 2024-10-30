package com.syengo.sufeeds.sufeeds.ui;

import com.syengo.sufeeds.sufeeds.models.Student;
import com.syengo.sufeeds.sufeeds.models.CourseClass;
import com.syengo.sufeeds.sufeeds.models.CourseTopic;
import com.syengo.sufeeds.sufeeds.dao.ClassDAO;
import com.syengo.sufeeds.sufeeds.dao.TopicDAO;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.Optional;
public class ClassManagementForm {
    private VBox root;
    private Student student;
    private TableView<CourseClass> classTable;
    private ClassDAO classDAO;
    private TopicDAO topicDAO;
    private ComboBox<String> dayCombo;

    public ClassManagementForm(Student student) {
        this.student = student;
        this.classDAO = new ClassDAO();
        this.topicDAO = new TopicDAO();
        createUI();
    }

    private void createUI() {
        root = new VBox(10);
        root.setPadding(new Insets(20));

        // Add Class section
        TextField classNameField = new TextField();
        classNameField.setPromptText("Class Name");
        dayCombo = new ComboBox<>();
        dayCombo.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
        TextField timeSlotField = new TextField();
        timeSlotField.setPromptText("Time Slot");
        TextArea descField = new TextArea();
        descField.setPromptText("Description");

        Button addButton = new Button("Add Class");
        addButton.setOnAction(e -> {
            CourseClass newClass = new CourseClass(
                classNameField.getText(),
                "Current", // You might want to make this selectable
                student.getAdmissionNo(),
                descField.getText(),
                dayCombo.getValue(),
                timeSlotField.getText()
            );
            if (classDAO.addClass(newClass)) {
                refreshClassTable();
                clearFields(classNameField, timeSlotField, descField);
            }
        });

        // Class Table
        classTable = new TableView<>();
        TableColumn<CourseClass, String> nameCol = new TableColumn<>("Class Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("className"));
        // Add other columns...

        classTable.getColumns().add(nameCol);
        refreshClassTable();

        // Add Topic Button
        Button addTopicButton = new Button("Add Topic");
        addTopicButton.setOnAction(e -> {
            CourseClass selectedClass = classTable.getSelectionModel().getSelectedItem();
            if (selectedClass != null) {
                showAddTopicDialog(selectedClass);
            }
        });

        root.getChildren().addAll(
            new Label("Add New Class"),
            classNameField,
            dayCombo,
            timeSlotField,
            descField,
            addButton,
            new Separator(),
            classTable,
            addTopicButton
        );
    }

    private void showAddTopicDialog(CourseClass selectedClass) {
        Dialog<CourseTopic> dialog = new Dialog<>();
        dialog.setTitle("Add Topic");

        // Create the form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        TextField topicNameField = new TextField();
        Spinner<Integer> weekSpinner = new Spinner<>(1, 14, 1);
        TextArea feedbackArea = new TextArea();
        TextArea outcomesArea = new TextArea();
        TextArea resourcesArea = new TextArea();
        Spinner<Integer> difficultySpinner = new Spinner<>(1, 5, 3);

        // Set preferred sizes for TextAreas
        feedbackArea.setPrefRowCount(2);
        outcomesArea.setPrefRowCount(2);
        resourcesArea.setPrefRowCount(2);

        // Set preferred widths
        topicNameField.setPrefWidth(200);
        feedbackArea.setPrefWidth(200);
        outcomesArea.setPrefWidth(200);
        resourcesArea.setPrefWidth(200);

        // First column
        grid.add(new Label("Topic Name:"), 0, 0);
        grid.add(topicNameField, 1, 0);

        grid.add(new Label("Week:"), 0, 1);
        grid.add(weekSpinner, 1, 1);

        grid.add(new Label("Difficulty (1-5):"), 0, 2);
        grid.add(difficultySpinner, 1, 2);

        // Second column
        grid.add(new Label("Feedback:"), 2, 0);
        grid.add(feedbackArea, 3, 0);

        grid.add(new Label("Learning Outcomes:"), 2, 1);
        grid.add(outcomesArea, 3, 1);

        grid.add(new Label("Resources:"), 2, 2);
        grid.add(resourcesArea, 3, 2);


        grid.setHgap(15);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);


        dialog.getDialogPane().setPrefWidth(600);
        dialog.getDialogPane().setPrefHeight(300);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return new CourseTopic(
                    selectedClass.getId(),
                    topicNameField.getText(),
                    weekSpinner.getValue(),
                    feedbackArea.getText(),
                    student.getAdmissionNo(),
                    outcomesArea.getText(),
                    resourcesArea.getText(),
                    difficultySpinner.getValue()
                );
            }
            return null;
        });

        Optional<CourseTopic> result = dialog.showAndWait();
        result.ifPresent(topic -> {
            if (topicDAO.addTopic(topic)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Topic added successfully!");
            }
        });
    }

    private void refreshClassTable() {
        classTable.getItems().clear();
        classTable.getItems().addAll(classDAO.getClassesForStudent(student.getAdmissionNo()));
    }

    public VBox getRoot() {
        return root;
    }
    private void clearFields(TextField classNameField, TextField timeSlotField, TextArea descField) {
        classNameField.clear();
        timeSlotField.clear();
        descField.clear();
        dayCombo.setValue(null);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
