package com.rts.model;

import java.util.List;

public class ScheduleResult {
    private List<String> executionTimeline;
    private List<String> missedDeadlines;

    public ScheduleResult(List<String> executionTimeline, List<String> missedDeadlines) {
        this.executionTimeline = executionTimeline;
        this.missedDeadlines = missedDeadlines;
    }

    public List<String> getExecutionTimeline() {
        return executionTimeline;
    }

    public void setExecutionTimeline(List<String> executionTimeline) {
        this.executionTimeline = executionTimeline;
    }

    public List<String> getMissedDeadlines() {
        return missedDeadlines;
    }

    public void setMissedDeadlines(List<String> missedDeadlines) {
        this.missedDeadlines = missedDeadlines;
    }

    @Override
    public String toString() {
        return "ScheduleResult{" +
                "executionTimeline=" + executionTimeline +
                ", missedDeadlines=" + missedDeadlines +
                '}';
    }
}