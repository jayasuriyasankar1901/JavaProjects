# 📚 Technical Documentation - Real-Time Scheduling Simulator

## Table of Contents
1. [Architecture Overview](#architecture-overview)
2. [Class Design](#class-design)
3. [Algorithm Implementation](#algorithm-implementation)
4. [Data Flow](#data-flow)
5. [UI Components](#ui-components)
6. [Simulation Engine](#simulation-engine)
7. [Performance Considerations](#performance-considerations)
8. [Extension Guide](#extension-guide)

---

## 🏗️ Architecture Overview

The application follows a **Model-View-Controller (MVC)** architecture pattern:

```
┌─────────────────────────────────────────────────────┐
│                  USER INTERFACE                      │
│  ┌─────────────┐  ┌──────────────┐  ┌────────────┐ │
│  │ Input Panel │  │ Gantt Chart  │  │  Log Area  │ │
│  └─────────────┘  └──────────────┘  └────────────┘ │
└────────────────────┬────────────────────────────────┘
                     │
                     ↓
        ┌────────────────────────┐
        │    CONTROLLER LAYER    │
        │  - Event Handlers      │
        │  - UI Logic            │
        │  - Validation          │
        └────────────┬───────────┘
                     │
                     ↓
        ┌────────────────────────┐
        │    BUSINESS LOGIC      │
        │  - Scheduler Interface │
        │  - RMS Implementation  │
        │  - EDF Implementation  │
        └────────────┬───────────┘
                     │
                     ↓
        ┌────────────────────────┐
        │      DATA MODEL        │
        │  - Task               │
        │  - TaskSet            │
        │  - ScheduleResult     │
        └────────────────────────┘
```

---

## 🎨 Class Design

### Model Package (`com.rts.model`)

#### **Task.java**

**Purpose:** Represents a single periodic real-time task

**Attributes:**
```java
private String id;              // Unique identifier (e.g., "T1")
private int executionTime;      // C - worst-case execution time
private int period;             // T - time between releases
private int deadline;           // D - relative deadline
private int remainingTime;      // Runtime state: time left to execute
private int nextReleaseTime;    // Runtime state: next activation time
```

**Key Methods:**
```java
// Constructor with explicit deadline
Task(String id, int executionTime, int period, int deadline)

// Constructor with implicit deadline (D = T)
Task(String id, int executionTime, int period)

// Reset task state for new simulation
void reset()

// Calculate CPU utilization (C/T)
double getUtilization()

// Standard getters and setters
String getId()
int getExecutionTime()
int getPeriod()
int getDeadline()
int getRemainingTime()
void setRemainingTime(int remainingTime)
int getNextReleaseTime()
void setNextReleaseTime(int nextReleaseTime)
```

**Design Decisions:**
- Immutable task parameters (id, executionTime, period, deadline)
- Mutable runtime state (remainingTime, nextReleaseTime)
- Automatic deadline calculation if not specified (D = T)

---

#### **TaskSet.java**

**Purpose:** Collection of tasks to be scheduled

**Attributes:**
```java
private List<Task> tasks;  // Dynamic list of tasks
```

**Key Methods:**
```java
void addTask(Task task)           // Add task to set
void removeTask(Task task)        // Remove task from set
List<Task> getTasks()             // Get all tasks
Task getTaskById(String id)       // Find task by ID
int size()                        // Number of tasks
void clear()                      // Remove all tasks
```

**Design Decisions:**
- Uses ArrayList for dynamic task management
- Provides lookup by task ID for efficient access
- Encapsulates task collection management

---

#### **ScheduleResult.java**

**Purpose:** Stores simulation results

**Attributes:**
```java
private List<String> executionTimeline;  // Sequence of task IDs executed
private List<String> missedDeadlines;    // List of deadline violations
```

**Structure:**
```java
// Timeline example for 10 time units:
["T1", "T1", "T2", "T2", "IDLE", "T1", "T3", "IDLE", "T2", "T2"]

// Missed deadlines example:
["Task T3 missed deadline at time 24", "Task T2 missed deadline at time 35"]
```

**Key Methods:**
```java
List<String> getExecutionTimeline()
List<String> getMissedDeadlines()
void setExecutionTimeline(List<String> timeline)
void setMissedDeadlines(List<String> misses)
```

---

### Algorithm Package (`com.rts.algorithm`)

#### **Scheduler.java (Interface)**

**Purpose:** Common interface for all scheduling algorithms

```java
public interface Scheduler {
    /**
     * Schedule tasks over the simulation time period
     * @param tasks List of tasks to schedule
     * @param simulationTime Total simulation duration
     * @return ScheduleResult with timeline and deadline misses
     */
    ScheduleResult schedule(List<Task> tasks, int simulationTime);
    
    /**
     * Get algorithm name for display
     * @return Algorithm name (e.g., "Rate Monotonic Scheduling (RMS)")
     */
    String getAlgorithmName();
}
```

**Design Pattern:** Strategy Pattern
- Allows runtime algorithm selection
- Easy to add new scheduling algorithms
- Consistent interface for all schedulers

---

#### **RateMonotonicScheduler.java**

**Algorithm Implementation:**

```java
@Override
public ScheduleResult schedule(List<Task> tasks, int simulationTime) {
    // 1. Sort tasks by period (ascending) - shorter period = higher priority
    List<Task> sortedTasks = new ArrayList<>(tasks);
    sortedTasks.sort(Comparator.comparingInt(Task::getPeriod));
    
    // 2. Initialize all tasks
    for (Task task : sortedTasks) {
        task.reset();
    }
    
    // 3. Simulate time-driven scheduling
    for (int time = 0; time < simulationTime; time++) {
        // 3a. Release tasks at their period intervals
        for (Task task : sortedTasks) {
            if (time % task.getPeriod() == 0) {
                if (task.getRemainingTime() > 0 && time > 0) {
                    // Deadline miss detected
                    missedDeadlines.add(...);
                }
                task.setRemainingTime(task.getExecutionTime());
            }
        }
        
        // 3b. Select highest priority ready task
        Task selectedTask = null;
        for (Task task : sortedTasks) {
            if (task.getRemainingTime() > 0) {
                selectedTask = task;  // First ready task has highest priority
                break;
            }
        }
        
        // 3c. Execute selected task for 1 time unit
        if (selectedTask != null) {
            timeline.add(selectedTask.getId());
            selectedTask.setRemainingTime(selectedTask.getRemainingTime() - 1);
        } else {
            timeline.add("IDLE");
        }
    }
    
    return new ScheduleResult(timeline, missedDeadlines);
}
```

**Complexity Analysis:**
- **Sorting:** O(n log n) - one time at start
- **Per time unit:** O(n) - check all tasks
- **Total:** O(n log n + T·n) where T = simulation time

**Memory Usage:**
- O(n) for sorted task list
- O(T) for timeline storage

---

#### **EarliestDeadlineFirstScheduler.java**

**Algorithm Implementation:**

```java
@Override
public ScheduleResult schedule(List<Task> tasks, int simulationTime) {
    // 1. Track absolute deadlines for each task
    Map<String, Integer> absoluteDeadlines = new HashMap<>();
    
    // 2. Initialize tasks
    for (Task task : tasks) {
        task.reset();
        absoluteDeadlines.put(task.getId(), task.getDeadline());
    }
    
    // 3. Simulate time-driven scheduling
    for (int time = 0; time < simulationTime; time++) {
        // 3a. Release tasks and update absolute deadlines
        for (Task task : tasks) {
            if (time % task.getPeriod() == 0) {
                if (task.getRemainingTime() > 0 && time > 0) {
                    missedDeadlines.add(...);
                }
                task.setRemainingTime(task.getExecutionTime());
                absoluteDeadlines.put(task.getId(), time + task.getDeadline());
            }
        }
        
        // 3b. Select task with earliest absolute deadline
        Task selectedTask = null;
        int earliestDeadline = Integer.MAX_VALUE;
        
        for (Task task : tasks) {
            if (task.getRemainingTime() > 0) {
                int deadline = absoluteDeadlines.get(task.getId());
                if (deadline < earliestDeadline) {
                    earliestDeadline = deadline;
                    selectedTask = task;
                }
            }
        }
        
        // 3c. Execute selected task
        if (selectedTask != null) {
            timeline.add(selectedTask.getId());
            selectedTask.setRemainingTime(selectedTask.getRemainingTime() - 1);
        } else {
            timeline.add("IDLE");
        }
    }
    
    return new ScheduleResult(timeline, missedDeadlines);
}
```

**Complexity Analysis:**
- **Per time unit:** O(n) - find task with earliest deadline
- **Total:** O(T·n) where T = simulation time
- **Space:** O(n) for deadline tracking + O(T) for timeline

---

## 🔄 Data Flow

### Simulation Execution Flow

```
┌──────────────────┐
│  User clicks     │
│  "Simulate"      │
└────────┬─────────┘
         │
         ↓
┌──────────────────────────────────────┐
│  1. Validate task set                │
│     - Check if tasks exist           │
│     - Verify all parameters valid    │
└────────┬─────────────────────────────┘
         │
         ↓
┌──────────────────────────────────────┐
│  2. Select scheduler                 │
│     - Get selected algorithm (RMS/EDF)│
│     - Instantiate scheduler object   │
└────────┬─────────────────────────────┘
         │
         ↓
┌──────────────────────────────────────┐
│  3. Reset all tasks                  │
│     - remainingTime = executionTime  │
│     - nextReleaseTime = 0            │
└────────┬─────────────────────────────┘
         │
         ↓
┌──────────────────────────────────────┐
│  4. Execute scheduling algorithm     │
│     - Simulate time units (0 to 40)  │
│     - Generate execution timeline    │
│     - Detect deadline misses         │
└────────┬─────────────────────────────┘
         │
         ↓
┌──────────────────────────────────────┐
│  5. Render Gantt chart               │
│     - Draw timeline bars             │
│     - Color-code tasks               │
│     - Display time axis              │
└────────┬─────────────────────────────┘
         │
         ↓
┌──────────────────────────────────────┐
│  6. Display results                  │
│     - CPU utilization                │
│     - Deadline misses (if any)       │
│     - Idle time percentage           │
└──────────────────────────────────────┘
```

---

## 🎨 UI Components

### Main Application Layout

```
┌─────────────────────────────────────────────────────┐
│  📋 Task Configuration                   [1300x750] │
│  ┌─────────────────────────────────────────────────┐│
│  │ Task ID: [____] C: [__] T: [__] D: [__]        ││
│  │ [➕Add] [➖Remove] [🗑️Clear] [📝Sample]         ││
│  │ Task List: T1(1,4,4), T2(2,5,5), T3(1,10,10)   ││
│  │ Algorithm: [RMS ▼] [▶️ Simulate] U: 0.75       ││
│  └─────────────────────────────────────────────────┘│
│                                                      │
│  📊 Gantt Chart                                      │
│  ┌─────────────────────────────────────────────────┐│
│  │ ┌─────┬─────┬─────┬─────┬─────┬─────┐          ││
│  │ │ T1  │ T2  │ T2  │ T1  │ T3  │IDLE │ ...      ││
│  │ └─────┴─────┴─────┴─────┴─────┴─────┘          ││
│  │ 0     5     10    15    20    25    30    ...   ││
│  │                                                  ││
│  │ Legend: ■ T1(1,4,4) ■ T2(2,5,5) ■ T3(1,10,10)  ││
│  └─────────────────────────────────────────────────┘│
│                                                      │
│  📝 Simulation Log                                   │
│  ┌─────────────────────────────────────────────────┐│
│  │ ✓ Sample tasks loaded                           ││
│  │ 🚀 Starting simulation with RMS                 ││
│  │ ✅ No missed deadlines!                         ││
│  │ 💡 CPU Utilization: 0.75 (75.0%)                ││
│  │ Status: ✅ Schedule is feasible                 ││
│  └─────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────┘
```

### Gantt Chart Drawing Algorithm

```java
private void drawGanttChart(ScheduleResult result) {
    GraphicsContext gc = ganttCanvas.getGraphicsContext2D();
    List<String> timeline = result.getExecutionTimeline();
    
    // Calculate dimensions
    double cellWidth = Math.min(30, (canvasWidth - 100) / timeline.size());
    double cellHeight = 40;
    double yStart = 50;
    
    // Assign colors to tasks
    Map<String, Color> taskColors = assignColors();
    
    // Draw each time unit
    for (int i = 0; i < timeline.size(); i++) {
        String taskId = timeline.get(i);
        Color color = taskColors.get(taskId);
        
        // Draw cell with task color
        gc.setFill(color);
        gc.fillRect(100 + i * cellWidth, yStart, cellWidth - 1, cellHeight);
        
        // Draw border
        gc.setStroke(Color.DARKGRAY);
        gc.strokeRect(100 + i * cellWidth, yStart, cellWidth - 1, cellHeight);
        
        // Draw task label
        if (!taskId.equals("IDLE")) {
            gc.setFill(Color.WHITE);
            gc.fillText(taskId, 100 + i * cellWidth + 5, yStart + 25);
        }
    }
    
    // Draw time axis
    drawTimeAxis(gc, timeline.size(), cellWidth);
    
    // Draw legend
    drawLegend(gc, taskColors);
}
```

---

## ⚙️ Simulation Engine

### Time-Driven Simulation

The simulator uses a **discrete time-driven approach**:

1. **Time advances in unit steps** (0, 1, 2, ..., T)
2. **At each time unit:**
   - Check for task releases (periodic arrivals)
   - Select highest priority ready task
   - Execute for 1 time unit
   - Update task state

### Pseudo-Code

```
ALGORITHM: Time-Driven Scheduling Simulation

INPUT: 
  - tasks: List of periodic tasks
  - simulationTime: Total simulation duration
  - scheduler: Scheduling algorithm (RMS or EDF)

OUTPUT:
  - executionTimeline: Task execution sequence
  - missedDeadlines: List of deadline violations

BEGIN
  FOR time ← 0 TO simulationTime - 1 DO
    // Phase 1: Task Release
    FOR EACH task IN tasks DO
      IF time MOD task.period = 0 THEN
        IF task.remainingTime > 0 AND time > 0 THEN
          RECORD deadline miss
        END IF
        task.remainingTime ← task.executionTime
        task.nextDeadline ← time + task.deadline
      END IF
    END FOR
    
    // Phase 2: Task Selection
    selectedTask ← scheduler.selectTask(tasks, time)
    
    // Phase 3: Execution
    IF selectedTask ≠ NULL THEN
      executionTimeline[time] ← selectedTask.id
      selectedTask.remainingTime ← selectedTask.remainingTime - 1
    ELSE
      executionTimeline[time] ← "IDLE"
    END IF
  END FOR
  
  RETURN (executionTimeline, missedDeadlines)
END
```

---

## 🚀 Performance Considerations

### Time Complexity

| Operation | Complexity | Notes |
|-----------|------------|-------|
| RMS Initialization | O(n log n) | Sort by period |
| EDF Initialization | O(n) | Initialize deadlines |
| Per time unit (RMS) | O(n) | Find first ready task |
| Per time unit (EDF) | O(n) | Find earliest deadline |
| Total simulation | O(T·n) | T time units, n tasks |
| Gantt rendering | O(T) | Draw timeline bars |

### Space Complexity

| Component | Space | Notes |
|-----------|-------|-------|
| Task list | O(n) | n tasks |
| Execution timeline | O(T) | T time units |
| Missed deadlines | O(m) | m misses |
| UI components | O(1) | Fixed |
| **Total** | **O(n + T + m)** | |

### Optimization Opportunities

1. **Event-Driven Simulation:**
   - Jump between events instead of unit time steps
   - Reduces complexity to O(k log k) where k = number of events
   
2. **Lazy Timeline Rendering:**
   - Only render visible portion of Gantt chart
   - Implement virtual scrolling for large timelines

3. **Caching:**
   - Cache CPU utilization calculations
   - Memoize schedulability tests

---

## 🔧 Extension Guide

### Adding a New Scheduling Algorithm

**Step 1:** Create a new scheduler class

```java
package com.rts.algorithm;

import com.rts.model.Task;
import com.rts.model.ScheduleResult;
import java.util.*;

public class MyCustomScheduler implements Scheduler {
    
    @Override
    public ScheduleResult schedule(List<Task> tasks, int simulationTime) {
        List<String> timeline = new ArrayList<>();
        List<String> missedDeadlines = new ArrayList<>();
        
        // Implement your scheduling logic here
        // ...
        
        return new ScheduleResult(timeline, missedDeadlines);
    }
    
    @Override
    public String getAlgorithmName() {
        return "My Custom Scheduling Algorithm";
    }
}
```

**Step 2:** Register in the main application

```java
// In RealTimeSchedulerApp.java, update runSimulation():
Scheduler scheduler;
if (selectedAlgo.contains("Rate Monotonic")) {
    scheduler = new RateMonotonicScheduler();
} else if (selectedAlgo.contains("Earliest Deadline")) {
    scheduler = new EarliestDeadlineFirstScheduler();
} else if (selectedAlgo.contains("My Custom")) {
    scheduler = new MyCustomScheduler();
}
```

**Step 3:** Add to algorithm dropdown

```java
algorithmComboBox.getItems().addAll(
    "Rate Monotonic Scheduling (RMS)", 
    "Earliest Deadline First (EDF)",
    "My Custom Scheduling Algorithm"  // Add this line
);
```

---

### Adding New Task Parameters

**Example: Adding "Priority" field**

**Step 1:** Update Task.java
```java
private int priority;

public Task(String id, int executionTime, int period, int deadline, int priority) {
    // ... existing code
    this.priority = priority;
}

public int getPriority() {
    return priority;
}
```

**Step 2:** Update UI input fields
```java
Label priorityLabel = new Label("Priority:");
TextField priorityField = new TextField();
priorityField.setPromptText("e.g., 1");
```

**Step 3:** Update task creation
```java
int priority = Integer.parseInt(priorityField.getText().trim());
Task task = new Task(id, exec, period, deadline, priority);
```

---

### Exporting Simulation Results

**Add export functionality:**

```java
private void exportResultsToCSV(ScheduleResult result) {
    try (PrintWriter writer = new PrintWriter(new File("simulation_results.csv"))) {
        StringBuilder sb = new StringBuilder();
        sb.append("Time,Task\n");
        
        List<String> timeline = result.getExecutionTimeline();
        for (int i = 0; i < timeline.size(); i++) {
            sb.append(i).append(",").append(timeline.get(i)).append("\n");
        }
        
        writer.write(sb.toString());
        logArea.appendText("✓ Results exported to simulation_results.csv\n");
    } catch (FileNotFoundException e) {
        logArea.appendText("❌ Export failed: " + e.getMessage() + "\n");
    }
}
```

---

## 📊 Testing Guidelines

### Unit Testing

```java
@Test
public void testRMSScheduling() {
    // Arrange
    List<Task> tasks = Arrays.asList(
        new Task("T1", 1, 4, 4),
        new Task("T2", 2, 5, 5)
    );
    Scheduler scheduler = new RateMonotonicScheduler();
    
    // Act
    ScheduleResult result = scheduler.schedule(tasks, 20);
    
    // Assert
    assertTrue(result.getMissedDeadlines().isEmpty());
    assertEquals(20, result.getExecutionTimeline().size());
}
```

### Integration Testing

1. Test task addition/removal
2. Test algorithm switching
3. Test Gantt chart rendering
4. Test deadline detection
5. Test CPU utilization calculation

---

## 🎓 Best Practices

1. **Validation:** Always validate user input before processing
2. **Error Handling:** Provide clear error messages
3. **Performance:** Limit simulation time for responsive UI
4. **Logging:** Log all significant events for debugging
5. **Code Style:** Follow Java naming conventions
6. **Documentation:** Comment complex algorithms
7. **Testing:** Write unit tests for schedulers

---

## 📖 Further Reading

- [JavaFX Documentation](https://openjfx.io/)
- [Real-Time Systems (Buttazzo)](https://link.springer.com/book/10.1007/978-1-4614-0676-1)
- [Operating System Concepts](https://www.os-book.com/)

---

**Last Updated:** October 15, 2025
