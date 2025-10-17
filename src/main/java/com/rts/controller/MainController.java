package com.rts.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private TextField taskInputField;

    @FXML
    private Button addTaskButton;

    @FXML
    private Button startSimulationButton;

    @FXML
    private Label statusLabel;

    @SuppressWarnings("unused")
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize() {
        addTaskButton.setOnAction(event -> addTask());
        startSimulationButton.setOnAction(event -> startSimulation());
    }

    private void addTask() {
        String taskInput = taskInputField.getText();
        // Logic to add task
        statusLabel.setText("Task added: " + taskInput);
        taskInputField.clear();
    }

    private void startSimulation() {
        // Logic to start the simulation
        statusLabel.setText("Simulation started.");
    }
}