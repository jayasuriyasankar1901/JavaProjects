package com.rts.controller;

import com.rts.model.ScheduleResult;
import com.rts.model.TaskSet;
import com.rts.algorithm.Scheduler;
import com.rts.algorithm.RateMonotonicScheduler;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;

import java.util.ArrayList;

public class SimulationController {
    private TaskSet taskSet;
    @SuppressWarnings("unused")
    private Scheduler scheduler;
    @SuppressWarnings("unused")
    private ScheduleResult scheduleResult;
    private Button startButton;
    private Button pauseButton;
    private Button resetButton;
    private ProgressBar progressBar;
    private TextArea logArea;

    private boolean isRunning;
    private Thread simulationThread;

    public SimulationController(Button startButton, Button pauseButton, Button resetButton, ProgressBar progressBar, TextArea logArea) {
        this.startButton = startButton;
        this.pauseButton = pauseButton;
        this.resetButton = resetButton;
        this.progressBar = progressBar;
        this.logArea = logArea;
        this.taskSet = new TaskSet();
        this.scheduler = new RateMonotonicScheduler(); // or new EarliestDeadlineFirstScheduler();
        this.scheduleResult = new ScheduleResult(new ArrayList<>(), new ArrayList<>());
        this.isRunning = false;

        initialize();
    }

    private void initialize() {
        startButton.setOnAction(event -> startSimulation());
        pauseButton.setOnAction(event -> pauseSimulation());
        resetButton.setOnAction(event -> resetSimulation());
    }

    private void startSimulation() {
        if (!isRunning) {
            isRunning = true;
            simulationThread = new Thread(createSimulationTask());
            simulationThread.setDaemon(true);
            simulationThread.start();
        }
    }

    private void pauseSimulation() {
        isRunning = false;
    }

    private void resetSimulation() {
        isRunning = false;
        progressBar.setProgress(0);
        logArea.clear();
        scheduleResult = new ScheduleResult(new ArrayList<>(), new ArrayList<>());
        taskSet.clear();
    }

    private Task<Void> createSimulationTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                int simulationTime = 40;
                for (int time = 0; time < simulationTime && isRunning; time++) {
                    final int currentTime = time;
                    final double progress = (double) time / simulationTime;
                    
                    // Simulation logic goes here
                    // Update progress bar and log area
                    Platform.runLater(() -> {
                        progressBar.setProgress(progress);
                        logArea.appendText("Time: " + currentTime + "\n");
                    });

                    try {
                        Thread.sleep(100); // Adjust based on simulation speed
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                return null;
            }
        };
    }

    public void setTaskSet(TaskSet taskSet) {
        this.taskSet = taskSet;
        // Scheduler interface doesn't have setTaskSet method
        // Use scheduler.schedule(taskSet.getTasks(), simulationTime) instead
    }
    
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}