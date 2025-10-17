package com.rts.controller;

import com.rts.model.Task;
import com.rts.util.ValidationUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class TaskInputController {

    @FXML
    private TextField taskIdField;

    @FXML
    private TextField executionTimeField;

    @FXML
    private TextField periodField;

    @FXML
    private TextField deadlineField;

    @FXML
    private Button addTaskButton;

    @FXML
    private Button removeTaskButton;

    @FXML
    public void initialize() {
        addTaskButton.setOnAction(event -> addTask());
        removeTaskButton.setOnAction(event -> removeTask());
    }

    private void addTask() {
        String taskId = taskIdField.getText();
        String executionTime = executionTimeField.getText();
        String period = periodField.getText();
        String deadline = deadlineField.getText();

        if (ValidationUtils.validateTaskInput(taskId, executionTime, period, deadline)) {
            int deadlineValue = (deadline == null || deadline.isEmpty()) ? 
                               Integer.parseInt(period) : Integer.parseInt(deadline);
            Task newTask = new Task(taskId, Integer.parseInt(executionTime), 
                                   Integer.parseInt(period), deadlineValue);
            // Code to add the task to the task set
            System.out.println("Task added: " + newTask);
            clearFields();
        } else {
            showAlert("Invalid Input", "Please enter valid task parameters.");
        }
    }

    private void removeTask() {
        String taskId = taskIdField.getText();
        // Code to remove the task from the task set
        if (taskId != null && !taskId.isEmpty()) {
            System.out.println("Removing task: " + taskId);
        }
        clearFields();
    }

    private void clearFields() {
        taskIdField.clear();
        executionTimeField.clear();
        periodField.clear();
        deadlineField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}