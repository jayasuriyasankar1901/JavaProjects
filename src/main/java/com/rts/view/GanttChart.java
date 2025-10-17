package com.rts.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import com.rts.model.ScheduleResult;

import java.util.List;

public class GanttChart extends Canvas {
    private static final int TASK_HEIGHT = 30;
    private static final int TIME_UNIT_WIDTH = 20;

    public GanttChart(double width, double height) {
        super(width, height);
        clearChart();
    }
    
    public void clearChart() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());
    }

    public void drawChart(ScheduleResult result) {
        if (result == null) {
            clearChart();
            return;
        }
        
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        
        List<String> timeline = result.getExecutionTimeline();
        if (timeline == null || timeline.isEmpty()) {
            return;
        }
        
        // Draw timeline
        for (int i = 0; i < timeline.size(); i++) {
            String taskId = timeline.get(i);
            int xPos = i * TIME_UNIT_WIDTH;
            
            // Choose color based on task
            if ("IDLE".equals(taskId)) {
                gc.setFill(Color.LIGHTGRAY);
            } else {
                gc.setFill(Color.LIGHTBLUE);
            }
            
            gc.fillRect(xPos, 50, TIME_UNIT_WIDTH - 1, TASK_HEIGHT);
            gc.strokeRect(xPos, 50, TIME_UNIT_WIDTH - 1, TASK_HEIGHT);
            
            // Draw task label
            if (!"IDLE".equals(taskId)) {
                gc.setFill(Color.BLACK);
                gc.setFont(new Font("Arial", 10));
                gc.fillText(taskId, xPos + 2, 50 + TASK_HEIGHT / 2 + 3);
            }
        }
    }
}