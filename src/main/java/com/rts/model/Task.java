package com.rts.model;

public class Task {
    private String id;
    private int executionTime;
    private int period;
    private int deadline;
    private int remainingTime;
    private int nextReleaseTime;

    public Task(String id, int executionTime, int period, int deadline) {
        this.id = id;
        this.executionTime = executionTime;
        this.period = period;
        this.deadline = deadline;
        this.remainingTime = executionTime;
        this.nextReleaseTime = 0;
    }

    public String getId() {
        return id;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public int getPeriod() {
        return period;
    }

    public int getDeadline() {
        return deadline;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public int getNextReleaseTime() {
        return nextReleaseTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void setNextReleaseTime(int nextReleaseTime) {
        this.nextReleaseTime = nextReleaseTime;
    }
    
    public void reset() {
        this.remainingTime = executionTime;
        this.nextReleaseTime = 0;
    }
    
    public double getUtilization() {
        return (double) executionTime / period;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", executionTime=" + executionTime +
                ", period=" + period +
                ", deadline=" + deadline +
                ", remainingTime=" + remainingTime +
                ", nextReleaseTime=" + nextReleaseTime +
                '}';
    }
}