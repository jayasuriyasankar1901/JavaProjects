# 📖 User Guide - Real-Time Scheduling Simulator

## Quick Start Guide

### First Time Setup

1. **Launch the Application**
   - Double-click the application icon, OR
   - Run from command line (see README.md)

2. **Verify the Window Opens**
   - You should see three main sections:
     - Top: Task Configuration Panel
     - Center: Gantt Chart Visualization
     - Bottom: Simulation Log

3. **Try the Sample**
   - Click **"📝 Load Sample"** button
   - Click **"▶️ Simulate"** button
   - Observe the Gantt chart animation

---

## Step-by-Step Tutorial

### Tutorial 1: Understanding the Interface

#### Task Configuration Panel (Top Section)

```
┌──────────────────────────────────────────────────┐
│ Task ID: [T1__] C: [1_] T: [4_] D: [4_]          │
│ [➕ Add Task] [➖ Remove] [🗑️ Clear] [📝 Sample] │
│                                                   │
│ Current Tasks:                                    │
│ • Task T1: C=1, T=4, D=4                         │
│ • Task T2: C=2, T=5, D=5                         │
│                                                   │
│ Algorithm: [Rate Monotonic ▼] [▶️ Simulate]      │
│ CPU Utilization: 0.60                            │
└──────────────────────────────────────────────────┘
```

**Fields Explained:**
- **Task ID:** Unique name (e.g., T1, TaskA, Job1)
- **Execution Time (C):** How many time units the task needs to run
- **Period (T):** How often the task repeats (every T time units)
- **Deadline (D):** When the task must finish (leave blank for D=T)

---

### Tutorial 2: Creating Your First Task Set

**Scenario:** Create a simple schedulable task set

**Step 1:** Enter first task
```
Task ID: T1
Execution Time: 2
Period: 10
Deadline: (leave blank)
Click [➕ Add Task]
```

**Step 2:** Enter second task
```
Task ID: T2
Execution Time: 3
Period: 15
Deadline: (leave blank)
Click [➕ Add Task]
```

**Step 3:** Verify task list
```
Current Tasks:
• Task T1: C=2, T=10, D=10
• Task T2: C=3, T=15, D=15
CPU Utilization: 0.40
```

**Step 4:** Run simulation
```
1. Select "Rate Monotonic Scheduling (RMS)"
2. Click [▶️ Simulate]
3. Watch the Gantt chart appear!
```

**Expected Result:**
```
✅ No missed deadlines!
💡 CPU Utilization: 0.40 (40.0%)
⏸️ Idle Time: 24 / 40 units (60.0%)
```

---

### Tutorial 3: Comparing RMS vs EDF

**Scenario:** See the difference between algorithms

**Task Set:**
```
T1: C=2, T=7, D=7   (Add this task)
T2: C=4, T=12, D=12 (Add this task)
Total U = 0.62
```

**Step 1:** Test with RMS
```
1. Select "Rate Monotonic Scheduling (RMS)"
2. Click [▶️ Simulate]
3. Observe the timeline
4. Note any deadline misses
```

**Step 2:** Test with EDF
```
1. Select "Earliest Deadline First (EDF)"
2. Click [▶️ Simulate]
3. Compare with RMS results
4. Note differences in execution order
```

**Observation:**
- RMS: Fixed priorities based on period
- EDF: Dynamic priorities based on deadline

---

### Tutorial 4: Detecting Overload

**Scenario:** Create an overloaded system

**Step 1:** Create heavy task set
```
Task T1: C=3, T=5, D=5  → U=0.60
Task T2: C=3, T=7, D=7  → U=0.43
Task T3: C=3, T=10, D=10 → U=0.30
Total U = 1.33 > 1.0 ❌
```

**Step 2:** Run simulation
```
Select any algorithm
Click [▶️ Simulate]
```

**Expected Result:**
```
❌ Deadline misses detected:
  • Task T3 missed deadline at time 10
  • Task T2 missed deadline at time 14
💡 CPU Utilization: 1.33 (133.0%)
Status: ❌ Deadline misses detected
```

**Gantt Chart:** Red indicators show deadline misses

---

## Common Usage Patterns

### Pattern 1: Testing Schedulability

**Goal:** Determine if a task set is schedulable

**Method:**
```
1. Add all tasks
2. Check CPU Utilization:
   - U ≤ 0.69 → RMS likely schedulable
   - U ≤ 1.00 → EDF might be schedulable
   - U > 1.00 → Not schedulable
3. Run simulation to confirm
4. Check for deadline misses
```

---

### Pattern 2: Finding the Best Algorithm

**Goal:** Choose between RMS and EDF

**Decision Tree:**
```
                    Start
                      │
                      ↓
              ┌───────────────┐
              │ U ≤ 0.69?     │
              └───────┬───────┘
                      │
        ┌─────────────┴─────────────┐
        ↓ Yes                       ↓ No
   ┌─────────┐              ┌──────────────┐
   │ Use RMS │              │ 0.69 < U ≤ 1?│
   └─────────┘              └──────┬───────┘
                                   │
                     ┌─────────────┴──────────┐
                     ↓ Yes                    ↓ No
              ┌─────────────┐         ┌─────────────┐
              │  Try EDF    │         │ Not         │
              │  (may work) │         │ Schedulable │
              └─────────────┘         └─────────────┘
```

---

### Pattern 3: Optimizing Task Parameters

**Goal:** Adjust tasks to avoid deadline misses

**Strategies:**
1. **Reduce Execution Times (C):**
   - Optimize task code
   - Use faster algorithms

2. **Increase Periods (T):**
   - Relax timing requirements
   - Reduce task frequency

3. **Extend Deadlines (D):**
   - If deadline < period is acceptable
   - Allows more scheduling flexibility

4. **Remove Non-Critical Tasks:**
   - Identify low-priority tasks
   - Consider background scheduling

---

## Understanding the Gantt Chart

### Reading the Timeline

```
Time:  0    1    2    3    4    5    6    7    8    9   10
Task: [T1] [T1] [T2] [T2] [T1] [T1] [T3] [IDLE][T2] [T2] [T1]
       │    │    │    │    │    │    │     │    │    │    │
       └────┴────┴────┴────┴────┴────┴─────┴────┴────┴────┘
       Blue  Blue Green Green Blue Blue Yellow Gray Green Green Blue
```

**Color Coding:**
- Each task has a unique color
- IDLE is always gray
- Same task = same color throughout

**Timeline Markers:**
- Numbers show time units (0, 5, 10, 15, ...)
- Vertical lines separate time units
- Task labels inside colored bars

---

### Legend Interpretation

```
Legend:
■ T1 (C=1, T=4, D=4)   ← Blue bars
■ T2 (C=2, T=5, D=5)   ← Green bars
■ T3 (C=1, T=10, D=10) ← Yellow bars
■ IDLE                  ← Gray bars
```

---

## Simulation Log Interpretation

### Success Message Format
```
🚀 Starting simulation with Rate Monotonic Scheduling (RMS)
Simulation time: 40 units
============================================================

📈 Simulation Results:
------------------------------------------------------------
✅ No missed deadlines!

💡 CPU Utilization: 0.75 (75.0%)
⏸️  Idle Time: 10 / 40 units (25.0%)

============================================================
✓ Simulation completed successfully!
```

### Failure Message Format
```
🚀 Starting simulation with Rate Monotonic Scheduling (RMS)
Simulation time: 40 units
============================================================

📈 Simulation Results:
------------------------------------------------------------
❌ Deadline misses detected:
  • Task T3 missed deadline at time 20
  • Task T3 missed deadline at time 30

💡 CPU Utilization: 1.20 (120.0%)
⏸️  Idle Time: 0 / 40 units (0.0%)

============================================================
✓ Simulation completed successfully!
Status: ❌ Deadline misses detected
```

---

## Tips and Tricks

### Tip 1: Quick Task Entry
- Use Tab key to move between fields
- Press Enter after typing to auto-focus next field
- Click "Add Task" or press button shortcut

### Tip 2: Efficient Testing
- Load sample tasks first
- Modify one parameter at a time
- Compare results before/after changes

### Tip 3: Understanding Utilization
```
CPU Utilization = Σ(Ci/Ti)

Example:
T1: 2/10 = 0.20
T2: 3/15 = 0.20
Total U = 0.40 (40% busy, 60% idle)
```

### Tip 4: Deadline Miss Analysis
When you see deadline misses:
1. Identify which tasks miss deadlines
2. Check task priorities (RMS) or deadlines (EDF)
3. Calculate CPU utilization
4. Reduce load or change algorithm

### Tip 5: Visual Comparison
- Run simulation with RMS → take note
- Run same tasks with EDF → compare
- Look for differences in:
  - Execution order
  - Deadline misses
  - Idle time distribution

---

## Keyboard Shortcuts

| Key | Action |
|-----|--------|
| Tab | Next input field |
| Shift+Tab | Previous input field |
| Enter | Trigger default button |
| Delete | Remove selected task |
| Ctrl+C | (Future: Copy results) |

---

## Common Scenarios

### Scenario 1: Academic Assignment
**Task:** "Verify if task set {T1(3,10), T2(4,15), T3(2,20)} is schedulable with RMS"

**Solution:**
1. Add tasks: T1(3,10), T2(4,15), T3(2,20)
2. Calculate U = 3/10 + 4/15 + 2/20 = 0.667
3. RMS bound for n=3: U ≤ 0.78 ✅
4. Run simulation with RMS
5. Check for deadline misses → None
6. **Answer:** Yes, schedulable with RMS

---

### Scenario 2: System Design
**Task:** "Design a task set for an embedded controller with 70% CPU utilization"

**Solution:**
1. Start with simple tasks
2. Add tasks incrementally
3. Monitor CPU utilization label
4. Stop when U ≈ 0.70
5. Test with both RMS and EDF
6. Choose algorithm with better performance

---

### Scenario 3: Research Comparison
**Task:** "Compare RMS and EDF for task set with U=0.85"

**Solution:**
1. Create task set with U=0.85
   - Example: T1(3,5), T2(4,10), T3(2,8)
   - U = 0.6 + 0.4 + 0.25 = 1.25 (oops, too high!)
   - Adjust: T1(2,5), T2(3,10), T3(2,10)
   - U = 0.4 + 0.3 + 0.2 = 0.9
2. Test with RMS → observe misses
3. Test with EDF → observe differences
4. Document findings

---

## Troubleshooting Guide

### Problem: "Tasks not executing"

**Symptoms:** Gantt chart shows all IDLE

**Solutions:**
1. Verify tasks are added (check task list)
2. Ensure execution time > 0
3. Check that period > 0
4. Click "Simulate" button

---

### Problem: "Unexpected deadline misses"

**Symptoms:** Deadline misses even though U < 1

**Possible Causes:**
1. Using RMS with U > 0.69 → Switch to EDF
2. Deadline < execution time → Invalid task
3. Sporadic task behavior → Not supported

---

### Problem: "Can't see full timeline"

**Symptoms:** Timeline cut off

**Solutions:**
1. Scroll horizontally in Gantt chart area
2. Reduce simulation time (modify code)
3. Increase window width

---

## Best Practices

1. ✅ **Start Simple:** Begin with 2-3 tasks
2. ✅ **Verify Utilization:** Check U before simulating
3. ✅ **Use Samples:** Load samples to understand behavior
4. ✅ **Compare Algorithms:** Try both RMS and EDF
5. ✅ **Read Logs:** Check simulation log for details
6. ✅ **Save Results:** Document your findings
7. ✅ **Experiment:** Modify parameters and observe changes

---

## Frequently Asked Questions (FAQ)

**Q: What is the maximum number of tasks?**
A: No hard limit, but 10-15 tasks recommended for clear visualization.

**Q: Can I change simulation time?**
A: Yes, modify `SIMULATION_TIME` constant in code (default: 40).

**Q: Why do I see IDLE time?**
A: CPU has no ready tasks to execute at that moment.

**Q: What if deadline > period?**
A: Allowed, but uncommon in real-time systems.

**Q: Can tasks have same period?**
A: Yes, RMS assigns arbitrary priority; EDF handles dynamically.

**Q: How to export results?**
A: Currently manual (screenshot). CSV export can be added.

**Q: Does it support aperiodic tasks?**
A: No, only periodic tasks are supported.

**Q: Can I pause/step through simulation?**
A: Not currently supported. Run completes instantly.

---

## Educational Use Cases

### For Students:
- Learn scheduling algorithms visually
- Verify homework calculations
- Understand deadline misses
- Compare algorithm performance

### For Educators:
- Demonstrate real-time concepts
- Create interactive lectures
- Assign hands-on exercises
- Generate exam questions

### For Researchers:
- Quick prototyping
- Algorithm comparison
- Task set analysis
- Educational tool development

---

## Sample Exercise

**Exercise:** Find the schedulability limit

**Instructions:**
1. Start with T1(1,4)
2. Add T2(1,5)
3. Calculate U = 0.25 + 0.20 = 0.45
4. Add T3(1,10) → U = 0.55
5. Add T4(1,8) → U = 0.68
6. Keep adding tasks until deadline misses occur
7. Record the maximum schedulable utilization

**Expected Learning:**
- RMS limit ≈ 0.69 for many tasks
- EDF limit ≈ 1.00
- Practical limits may differ from theoretical

---

## Advanced Usage

### Creating Challenging Task Sets

**Hard for RMS, Easy for EDF:**
```
T1: C=4, T=10, D=10  (U=0.40)
T2: C=5, T=12, D=12  (U=0.42)
Total U = 0.82
→ RMS fails (U > 0.78)
→ EDF succeeds (U < 1.0)
```

**Hard for Both:**
```
T1: C=5, T=10, D=10  (U=0.50)
T2: C=6, T=12, D=12  (U=0.50)
Total U = 1.00
→ RMS fails
→ EDF barely succeeds (edge case)
```

---

## Getting Help

- Check this guide first
- Review DOCUMENTATION.md for technical details
- Check README.md for installation help
- Open an issue on GitHub
- Contact support

---

**Happy Scheduling! 🎉**

**Remember:** Real-time scheduling is both science and art. Experiment, learn, and have fun!
