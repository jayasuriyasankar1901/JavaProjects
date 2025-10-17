package com.rts.model;

import java.util.ArrayList;
import java.util.List;

public class TaskSet {
    private List<Task> tasks;

    public TaskSet() {
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Task getTaskById(String id) {
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                return task;
            }
        }
        return null;
    }

    public int size() {
        return tasks.size();
    }
    
    public int getTaskCount() {
        return tasks.size();
    }
    
    public Task getTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            return tasks.get(index);
        }
        return null;
    }

    public void clear() {
        tasks.clear();
    }
}