package com.rts.algorithm;

import com.rts.model.Task;
import com.rts.model.ScheduleResult;
import java.util.List;

public interface Scheduler {
    ScheduleResult schedule(List<Task> tasks, int simulationTime);
    String getAlgorithmName();
}