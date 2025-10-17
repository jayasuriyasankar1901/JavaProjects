package com.rts.algorithm;

import com.rts.model.Task;
import com.rts.model.ScheduleResult;

import java.util.*;

public class RateMonotonicScheduler implements Scheduler {
    
    @Override
    public ScheduleResult schedule(List<Task> tasks, int simulationTime) {
        List<String> timeline = new ArrayList<>();
        List<String> missedDeadlines = new ArrayList<>();
        
        // Sort tasks by period (RMS - shorter period = higher priority)
        List<Task> sortedTasks = new ArrayList<>(tasks);
        sortedTasks.sort(Comparator.comparingInt(Task::getPeriod));
        
        // Reset all tasks
        for (Task task : sortedTasks) {
            task.reset();
        }
        
        // Track absolute deadlines for each task instance
        Map<String, Integer> absoluteDeadlines = new HashMap<>();
        for (Task task : sortedTasks) {
            absoluteDeadlines.put(task.getId(), task.getDeadline());
        }
        
        // Simulate scheduling
        for (int time = 0; time < simulationTime; time++) {
            // Release tasks that are ready at this time
            for (Task task : sortedTasks) {
                if (time % task.getPeriod() == 0) {
                    if (task.getRemainingTime() > 0 && time > 0) {
                        // Deadline miss - task not completed before next release
                        missedDeadlines.add(String.format("Task %s missed deadline at time %d", 
                            task.getId(), time));
                    }
                    task.setRemainingTime(task.getExecutionTime());
                    absoluteDeadlines.put(task.getId(), time + task.getDeadline());
                }
            }
            
            // Select highest priority ready task (lowest period)
            Task selectedTask = null;
            for (Task task : sortedTasks) {
                if (task.getRemainingTime() > 0) {
                    selectedTask = task;
                    break;
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
        return "Rate Monotonic Scheduling (RMS)";
    }
}