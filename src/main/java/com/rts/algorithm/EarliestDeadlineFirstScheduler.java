package com.rts.algorithm;

import com.rts.model.Task;
import com.rts.model.ScheduleResult;

import java.util.*;

public class EarliestDeadlineFirstScheduler implements Scheduler {
    
    @Override
    public ScheduleResult schedule(List<Task> tasks, int simulationTime) {
        List<String> timeline = new ArrayList<>();
        List<String> missedDeadlines = new ArrayList<>();
        
        List<Task> taskList = new ArrayList<>(tasks);
        
        // Reset all tasks
        for (Task task : taskList) {
            task.reset();
        }
        
        // Track absolute deadlines for each task instance
        Map<String, Integer> absoluteDeadlines = new HashMap<>();
        for (Task task : taskList) {
            absoluteDeadlines.put(task.getId(), task.getDeadline());
        }
        
        // Simulate scheduling
        for (int time = 0; time < simulationTime; time++) {
            // Release tasks at their period intervals
            for (Task task : taskList) {
                if (time % task.getPeriod() == 0) {
                    if (task.getRemainingTime() > 0 && time > 0) {
                        // Deadline miss
                        missedDeadlines.add(String.format("Task %s missed deadline at time %d", 
                            task.getId(), time));
                    }
                    task.setRemainingTime(task.getExecutionTime());
                    absoluteDeadlines.put(task.getId(), time + task.getDeadline());
                }
            }
            
            // Select task with earliest absolute deadline
            Task selectedTask = null;
            int earliestDeadline = Integer.MAX_VALUE;
            
            for (Task task : taskList) {
                if (task.getRemainingTime() > 0) {
                    int deadline = absoluteDeadlines.getOrDefault(task.getId(), Integer.MAX_VALUE);
                    if (deadline < earliestDeadline) {
                        earliestDeadline = deadline;
                        selectedTask = task;
                    }
                }
            }
            
            if (selectedTask != null) {
                timeline.add(selectedTask.getId());
                selectedTask.setRemainingTime(selectedTask.getRemainingTime() - 1);
            } else {
                timeline.add("IDLE");
            }
        }
        
        return new ScheduleResult(timeline, missedDeadlines);
    }
    
    @Override
    public String getAlgorithmName() {
        return "Earliest Deadline First (EDF)";
    }
}