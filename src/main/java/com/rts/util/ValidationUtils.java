package com.rts.util;

public class ValidationUtils {

    public static boolean validateTaskInput(String id, String executionTime, String period, String deadline) {
        if (id == null || id.isEmpty()) {
            return false;
        }
        try {
            int execTime = Integer.parseInt(executionTime);
            int per = Integer.parseInt(period);
            
            // Deadline is optional, can be empty
            if (deadline == null || deadline.isEmpty()) {
                return execTime > 0 && per > 0 && execTime <= per;
            }
            
            int dead = Integer.parseInt(deadline);
            return execTime > 0 && per > 0 && dead > 0 && execTime <= per && dead <= per;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isValidTaskInput(String id, String executionTime, String period, String deadline) {
        return validateTaskInput(id, executionTime, period, deadline);
    }
    
    public static boolean isValidTaskSetSize(int size) {
        return size > 0;
    }
}