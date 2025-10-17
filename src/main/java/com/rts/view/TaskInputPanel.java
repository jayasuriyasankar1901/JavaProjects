package com.rts.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class TaskInputPanel extends VBox {
    private TextField idField;
    private TextField executionTimeField;
    private TextField periodField;
    private TextField deadlineField;
    private Button addButton;
    private Button removeButton;

    public TaskInputPanel() {
        initializeUI();
    }

    private void initializeUI() {
        Label idLabel = new Label("Task ID:");
        idField = new TextField();

        Label executionTimeLabel = new Label("Execution Time:");
        executionTimeField = new TextField();

        Label periodLabel = new Label("Period:");
        periodField = new TextField();

        Label deadlineLabel = new Label("Deadline:");
        deadlineField = new TextField();

        addButton = new Button("Add Task");
        removeButton = new Button("Remove Task");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(executionTimeLabel, 0, 1);
        grid.add(executionTimeField, 1, 1);
        grid.add(periodLabel, 0, 2);
        grid.add(periodField, 1, 2);
        grid.add(deadlineLabel, 0, 3);
        grid.add(deadlineField, 1, 3);
        grid.add(addButton, 0, 4);
        grid.add(removeButton, 1, 4);

        getChildren().add(grid);
    }

    public String getTaskId() {
        return idField.getText();
    }

    public String getExecutionTime() {
        return executionTimeField.getText();
    }

    public String getPeriod() {
        return periodField.getText();
    }

    public String getDeadline() {
        return deadlineField.getText();
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getRemoveButton() {
        return removeButton;
    }
}