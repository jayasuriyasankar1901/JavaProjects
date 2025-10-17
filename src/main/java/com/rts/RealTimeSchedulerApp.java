package com.rts;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import com.rts.model.Task;
import com.rts.model.ScheduleResult;
import com.rts.algorithm.*;

import java.util.*;

public class RealTimeSchedulerApp extends Application {
    
    private List<Task> tasks = new ArrayList<>();
    private ListView<String> taskListView;
    private ComboBox<String> algorithmComboBox;
    private Canvas ganttCanvas;
    private TextArea logArea;
    private Label utilizationLabel;
    private Label statusLabel;
    private final int SIMULATION_TIME = 40;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Real-Time Scheduling Simulator");
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #f5f5f5;");
        
        // Top: Input Panel
        root.setTop(createInputPanel());
        
        // Center: Gantt Chart
        root.setCenter(createGanttChartPanel());
        
        // Bottom: Log and Info
        root.setBottom(createLogPanel());
        
        // Add sample tasks
        addSampleTasks();
        
        Scene scene = new Scene(root, 1300, 750);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private VBox createInputPanel() {
        VBox inputPanel = new VBox(10);
        inputPanel.setPadding(new Insets(10));
        inputPanel.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5;");
        
        Label titleLabel = new Label("📋 Task Configuration");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        // Task input fields
        HBox inputRow1 = new HBox(10);
        inputRow1.setAlignment(Pos.CENTER_LEFT);
        
        Label idLabel = new Label("Task ID:");
        TextField idField = new TextField();
        idField.setPromptText("e.g., T1");
        idField.setPrefWidth(80);
        
        Label execLabel = new Label("Execution Time (C):");
        TextField execField = new TextField();
        execField.setPromptText("e.g., 2");
        execField.setPrefWidth(80);
        
        Label periodLabel = new Label("Period (T):");
        TextField periodField = new TextField();
        periodField.setPromptText("e.g., 5");
        periodField.setPrefWidth(80);
        
        Label deadlineLabel = new Label("Deadline (D):");
        TextField deadlineField = new TextField();
        deadlineField.setPromptText("Optional");
        deadlineField.setPrefWidth(80);
        
        inputRow1.getChildren().addAll(idLabel, idField, execLabel, execField, 
                                        periodLabel, periodField, deadlineLabel, deadlineField);
        
        // Buttons row
        HBox buttonRow = new HBox(10);
        buttonRow.setAlignment(Pos.CENTER_LEFT);
        
        Button addButton = new Button("➕ Add Task");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        addButton.setOnAction(e -> {
            try {
                String id = idField.getText().trim();
                int exec = Integer.parseInt(execField.getText().trim());
                int period = Integer.parseInt(periodField.getText().trim());
                int deadline = deadlineField.getText().trim().isEmpty() ? 
                               period : Integer.parseInt(deadlineField.getText().trim());
                
                if (id.isEmpty() || exec <= 0 || period <= 0 || deadline <= 0) {
                    showAlert("Invalid Input", "Please enter valid positive values.");
                    return;
                }
                
                Task task = new Task(id, exec, period, deadline);
                tasks.add(task);
                updateTaskList();
                updateUtilization();
                
                // Clear fields
                idField.clear();
                execField.clear();
                periodField.clear();
                deadlineField.clear();
                
                logArea.appendText("✓ Added task: " + task + "\n");
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter numeric values for execution time, period, and deadline.");
            }
        });
        
        Button removeButton = new Button("➖ Remove Task");
        removeButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
        removeButton.setOnAction(e -> {
            String selected = taskListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String taskId = selected.split(":")[0].replace("Task ", "").trim();
                tasks.removeIf(t -> t.getId().equals(taskId));
                updateTaskList();
                updateUtilization();
                logArea.appendText("✓ Removed task: " + taskId + "\n");
            }
        });
        
        Button clearButton = new Button("🗑️ Clear All");
        clearButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;");
        clearButton.setOnAction(e -> {
            tasks.clear();
            updateTaskList();
            updateUtilization();
            clearGanttChart();
            logArea.clear();
            logArea.appendText("✓ All tasks cleared.\n");
        });
        
        Button sampleButton = new Button("📝 Load Sample");
        sampleButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        sampleButton.setOnAction(e -> {
            tasks.clear();
            addSampleTasks();
            logArea.appendText("✓ Sample tasks loaded.\n");
        });
        
        buttonRow.getChildren().addAll(addButton, removeButton, clearButton, sampleButton);
        
        // Task list
        taskListView = new ListView<>();
        taskListView.setPrefHeight(80);
        taskListView.setStyle("-fx-border-color: #cccccc;");
        
        // Control row
        HBox controlRow = new HBox(10);
        controlRow.setAlignment(Pos.CENTER_LEFT);
        
        Label algoLabel = new Label("Select Algorithm:");
        algorithmComboBox = new ComboBox<>();
        algorithmComboBox.getItems().addAll("Rate Monotonic Scheduling (RMS)", "Earliest Deadline First (EDF)");
        algorithmComboBox.getSelectionModel().selectFirst();
        algorithmComboBox.setPrefWidth(300);
        
        Button simulateButton = new Button("▶️ Simulate");
        simulateButton.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
        simulateButton.setOnAction(e -> runSimulation());
        
        utilizationLabel = new Label("CPU Utilization: 0.00");
        utilizationLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        utilizationLabel.setStyle("-fx-text-fill: #333;");
        
        controlRow.getChildren().addAll(algoLabel, algorithmComboBox, simulateButton, utilizationLabel);
        
        inputPanel.getChildren().addAll(titleLabel, new Separator(), inputRow1, buttonRow, 
                                         new Label("Current Tasks:"), taskListView, 
                                         new Separator(), controlRow);
        
        return inputPanel;
    }
    
    private VBox createGanttChartPanel() {
        VBox chartPanel = new VBox(10);
        chartPanel.setPadding(new Insets(10));
        chartPanel.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5;");
        
        Label chartTitle = new Label("📊 Gantt Chart / Timeline Visualization");
        chartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        ganttCanvas = new Canvas(1250, 350);
        clearGanttChart();
        
        ScrollPane scrollPane = new ScrollPane(ganttCanvas);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(370);
        
        chartPanel.getChildren().addAll(chartTitle, new Separator(), scrollPane);
        VBox.setVgrow(chartPanel, Priority.ALWAYS);
        
        return chartPanel;
    }
    
    private VBox createLogPanel() {
        VBox logPanel = new VBox(10);
        logPanel.setPadding(new Insets(10));
        logPanel.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5;");
        
        Label logTitle = new Label("📝 Simulation Log & Status");
        logTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        logArea = new TextArea();
        logArea.setPrefHeight(120);
        logArea.setEditable(false);
        logArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 11;");
        logArea.setText("Welcome to Real-Time Scheduling Simulator!\n");
        logArea.appendText("Add tasks and click 'Simulate' to begin.\n");
        
        statusLabel = new Label("Status: Ready");
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        statusLabel.setStyle("-fx-text-fill: green;");
        
        logPanel.getChildren().addAll(logTitle, new Separator(), logArea, statusLabel);
        
        return logPanel;
    }
    
    private void addSampleTasks() {
        tasks.clear();
        tasks.add(new Task("T1", 1, 4, 4));
        tasks.add(new Task("T2", 2, 5, 5));
        tasks.add(new Task("T3", 1, 10, 10));
        updateTaskList();
        updateUtilization();
    }
    
    private void updateTaskList() {
        taskListView.getItems().clear();
        for (Task task : tasks) {
            taskListView.getItems().add(String.format("Task %s: C=%d, T=%d, D=%d", 
                task.getId(), task.getExecutionTime(), task.getPeriod(), task.getDeadline()));
        }
    }
    
    private void updateUtilization() {
        double utilization = tasks.stream()
                                  .mapToDouble(t -> (double) t.getExecutionTime() / t.getPeriod())
                                  .sum();
        utilizationLabel.setText(String.format("CPU Utilization: %.2f", utilization));
        
        if (utilization > 1.0) {
            utilizationLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        } else {
            utilizationLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        }
    }
    
    private void runSimulation() {
        if (tasks.isEmpty()) {
            showAlert("No Tasks", "Please add tasks before running simulation.");
            return;
        }
        
        String selectedAlgo = algorithmComboBox.getSelectionModel().getSelectedItem();
        Scheduler scheduler;
        
        if (selectedAlgo.contains("Rate Monotonic")) {
            scheduler = new RateMonotonicScheduler();
        } else {
            scheduler = new EarliestDeadlineFirstScheduler();
        }
        
        logArea.clear();
        logArea.appendText("🚀 Starting simulation with " + selectedAlgo + "\n");
        logArea.appendText("Simulation time: " + SIMULATION_TIME + " units\n");
        logArea.appendText("=" .repeat(60) + "\n");
        
        // Reset tasks
        for (Task task : tasks) {
            task.reset();
        }
        
        ScheduleResult result = scheduler.schedule(new ArrayList<>(tasks), SIMULATION_TIME);
        
        drawGanttChart(result);
        
        // Log results
        logArea.appendText("\n📈 Simulation Results:\n");
        logArea.appendText("-".repeat(60) + "\n");
        
        if (result.getMissedDeadlines().isEmpty()) {
            logArea.appendText("✅ No missed deadlines!\n");
            statusLabel.setText("Status: ✅ Schedule is feasible - No deadline misses");
            statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        } else {
            logArea.appendText("❌ Deadline misses detected:\n");
            for (String miss : result.getMissedDeadlines()) {
                logArea.appendText("  • " + miss + "\n");
            }
            statusLabel.setText("Status: ❌ Deadline misses detected");
            statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        }
        
        double utilization = tasks.stream()
                                  .mapToDouble(t -> (double) t.getExecutionTime() / t.getPeriod())
                                  .sum();
        logArea.appendText(String.format("\n💡 CPU Utilization: %.2f (%.1f%%)\n", utilization, utilization * 100));
        
        // Calculate idle time
        long idleTime = result.getExecutionTimeline().stream().filter(t -> t.equals("IDLE")).count();
        logArea.appendText(String.format("⏸️  Idle Time: %d / %d units (%.1f%%)\n", 
            idleTime, SIMULATION_TIME, (idleTime * 100.0 / SIMULATION_TIME)));
        
        logArea.appendText("\n" + "=".repeat(60) + "\n");
        logArea.appendText("✓ Simulation completed successfully!\n");
    }
    
    private void drawGanttChart(ScheduleResult result) {
        GraphicsContext gc = ganttCanvas.getGraphicsContext2D();
        
        // Clear canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, ganttCanvas.getWidth(), ganttCanvas.getHeight());
        
        List<String> timeline = result.getExecutionTimeline();
        int timeUnits = timeline.size();
        
        double cellWidth = Math.min(30, (ganttCanvas.getWidth() - 100) / timeUnits);
        double chartHeight = 40;
        double yStart = 50;
        
        // Draw title
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gc.fillText("Task Execution Timeline", 10, 25);
        
        // Task color mapping
        Map<String, Color> taskColors = new HashMap<>();
        Color[] colors = {Color.rgb(52, 152, 219), Color.rgb(46, 204, 113), 
                         Color.rgb(241, 196, 15), Color.rgb(231, 76, 60),
                         Color.rgb(155, 89, 182), Color.rgb(26, 188, 156)};
        
        int colorIndex = 0;
        for (Task task : tasks) {
            taskColors.put(task.getId(), colors[colorIndex % colors.length]);
            colorIndex++;
        }
        taskColors.put("IDLE", Color.LIGHTGRAY);
        
        // Draw timeline
        for (int i = 0; i < timeUnits; i++) {
            String taskId = timeline.get(i);
            Color color = taskColors.getOrDefault(taskId, Color.GRAY);
            
            // Draw cell
            gc.setFill(color);
            gc.fillRect(100 + i * cellWidth, yStart, cellWidth - 1, chartHeight);
            
            // Draw border
            gc.setStroke(Color.DARKGRAY);
            gc.strokeRect(100 + i * cellWidth, yStart, cellWidth - 1, chartHeight);
            
            // Draw task label
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            if (!taskId.equals("IDLE")) {
                gc.fillText(taskId, 100 + i * cellWidth + 5, yStart + 25);
            }
        }
        
        // Draw time axis
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeLine(100, yStart + chartHeight, 100 + timeUnits * cellWidth, yStart + chartHeight);
        
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", 10));
        for (int i = 0; i <= timeUnits; i += 5) {
            double x = 100 + i * cellWidth;
            gc.strokeLine(x, yStart + chartHeight, x, yStart + chartHeight + 5);
            gc.fillText(String.valueOf(i), x - 5, yStart + chartHeight + 20);
        }
        
        // Draw Y-axis label
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        gc.fillText("Time →", 100 + timeUnits * cellWidth + 10, yStart + chartHeight);
        
        // Draw legend
        double legendY = yStart + chartHeight + 50;
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        gc.fillText("Legend:", 10, legendY);
        
        int legendIndex = 0;
        for (Task task : tasks) {
            Color color = taskColors.get(task.getId());
            double legendX = 10 + (legendIndex % 4) * 200;
            double legendYPos = legendY + 20 + (legendIndex / 4) * 25;
            
            gc.setFill(color);
            gc.fillRect(legendX, legendYPos, 30, 15);
            gc.setStroke(Color.DARKGRAY);
            gc.strokeRect(legendX, legendYPos, 30, 15);
            
            gc.setFill(Color.BLACK);
            gc.setFont(Font.font("Arial", 11));
            gc.fillText(String.format("%s (C=%d, T=%d, D=%d)", 
                task.getId(), task.getExecutionTime(), task.getPeriod(), task.getDeadline()), 
                legendX + 35, legendYPos + 12);
            
            legendIndex++;
        }
        
        // Draw IDLE legend
        double legendX = 10 + (legendIndex % 4) * 200;
        double legendYPos = legendY + 20 + (legendIndex / 4) * 25;
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(legendX, legendYPos, 30, 15);
        gc.setStroke(Color.DARKGRAY);
        gc.strokeRect(legendX, legendYPos, 30, 15);
        gc.setFill(Color.BLACK);
        gc.fillText("IDLE", legendX + 35, legendYPos + 12);
    }
    
    private void clearGanttChart() {
        GraphicsContext gc = ganttCanvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, ganttCanvas.getWidth(), ganttCanvas.getHeight());
        
        gc.setFill(Color.LIGHTGRAY);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        gc.fillText("Add tasks and click 'Simulate' to see the Gantt chart", 
                   ganttCanvas.getWidth() / 2 - 200, ganttCanvas.getHeight() / 2);
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}