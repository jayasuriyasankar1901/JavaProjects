package com.rts.util;

import com.rts.model.TaskSet;

public class SchedulabilityAnalyzer {

    public static boolean isSchedulable(TaskSet taskSet) {
        double utilization = calculateUtilization(taskSet);
        int taskCount = taskSet.getTaskCount();

        // Utilization bound for RMS
        double rmsBound = taskCount * (Math.pow(2, (1.0 / taskCount)) - 1);
        
        return utilization <= rmsBound;
    }

    private static double calculateUtilization(TaskSet taskSet) {
        double utilization = 0.0;
        for (int i = 0; i < taskSet.getTaskCount(); i++) {
            utilization += (double) taskSet.getTask(i).getExecutionTime() / taskSet.getTask(i).getPeriod();
        }
        return utilization;
    }
}