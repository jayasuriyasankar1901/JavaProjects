# 📋 Quick Reference Card - Real-Time Scheduling Simulator

## ⚡ Quick Start (30 seconds)

```bash
1. Launch application
2. Click "📝 Load Sample"
3. Click "▶️ Simulate"
4. Observe Gantt chart!
```

---

## 📝 Task Parameters

| Parameter | Symbol | Meaning | Example |
|-----------|--------|---------|---------|
| **Task ID** | - | Unique identifier | T1, TaskA |
| **Execution Time** | C | CPU time needed | 2 units |
| **Period** | T | Release interval | 10 units |
| **Deadline** | D | Time constraint | 10 units |
| **Utilization** | U | C/T ratio | 0.20 |

---

## 🧮 Key Formulas

### CPU Utilization
```
U = Σ(Ci/Ti) for all tasks

Example:
T1: 1/4 = 0.25
T2: 2/5 = 0.40
Total U = 0.65
```

### RMS Schedulability Bound
```
U ≤ n(2^(1/n) - 1)

n=2: U ≤ 0.828
n=3: U ≤ 0.780
n→∞: U ≤ 0.693
```

### EDF Schedulability Condition
```
U ≤ 1.0 (necessary & sufficient)
```

---

## 🎯 Algorithm Comparison

| Feature | RMS | EDF |
|---------|-----|-----|
| **Priority** | Static (by period) | Dynamic (by deadline) |
| **Optimality** | Among static | Among all |
| **Max Utilization** | ~69% | 100% |
| **Overhead** | Low | Higher |
| **Predictability** | High | Lower |
| **Use Case** | Fixed workload | Variable workload |

---

## 🎨 UI Sections

```
┌─────────────────────────────────────┐
│ 📋 TASK CONFIGURATION               │
│  Input → Add → List → Algorithm    │
└─────────────────────────────────────┘
┌─────────────────────────────────────┐
│ 📊 GANTT CHART                      │
│  Timeline visualization             │
└─────────────────────────────────────┘
┌─────────────────────────────────────┐
│ 📝 SIMULATION LOG                   │
│  Results & status                   │
└─────────────────────────────────────┘
```

---

## 🎮 Button Quick Reference

| Button | Function |
|--------|----------|
| ➕ **Add Task** | Create new task |
| ➖ **Remove Task** | Delete selected |
| 🗑️ **Clear All** | Remove all tasks |
| 📝 **Load Sample** | Load example tasks |
| ▶️ **Simulate** | Run simulation |

---

## ✅ Validation Rules

```
✓ Task ID: Non-empty string
✓ C, T, D: Positive integers
✓ C ≤ T (recommended)
✓ D ≤ T (recommended)
✓ D defaults to T if empty
```

---

## 📊 Interpreting Results

### ✅ Success
```
✅ No missed deadlines!
💡 CPU Utilization: 0.75 (75.0%)
⏸️ Idle Time: 10 / 40 units (25.0%)
Status: ✅ Schedule is feasible
```

### ❌ Failure
```
❌ Deadline misses detected:
  • Task T3 missed deadline at time 20
💡 CPU Utilization: 1.20 (120.0%)
Status: ❌ Deadline misses detected
```

---

## 🎯 Sample Task Sets

### Easy (Always Schedulable)
```
T1: C=1, T=4  → U=0.25
T2: C=1, T=5  → U=0.20
Total U = 0.45 < 0.69 ✅
```

### Medium (EDF Only)
```
T1: C=2, T=5  → U=0.40
T2: C=3, T=7  → U=0.43
Total U = 0.83 (RMS❌ EDF✅)
```

### Hard (Not Schedulable)
```
T1: C=3, T=5  → U=0.60
T2: C=3, T=6  → U=0.50
Total U = 1.10 > 1.0 ❌
```

---

## 🔍 Decision Tree

```
Should I use RMS or EDF?
│
├─ U ≤ 0.69? → Yes → Use RMS ✅
│
├─ 0.69 < U ≤ 1.0? → Use EDF ✅
│
└─ U > 1.0? → Not schedulable ❌
```

---

## 🐛 Common Issues

| Problem | Solution |
|---------|----------|
| No tasks visible | Click "Add Task" |
| Blank Gantt chart | Click "Simulate" |
| High deadline misses | Reduce U or use EDF |
| Can't add task | Check all fields filled |
| Wrong algorithm | Check dropdown selection |

---

## 💡 Pro Tips

1. **Start small:** 2-3 tasks first
2. **Check U first:** Before simulating
3. **Compare algorithms:** Try both RMS & EDF
4. **Read logs:** Detailed info there
5. **Use samples:** Learn by example

---

## ⌨️ Keyboard Shortcuts

| Key | Action |
|-----|--------|
| `Tab` | Next field |
| `Shift+Tab` | Previous field |
| `Enter` | Submit/Simulate |
| `Delete` | Remove selected |

---

## 📏 Gantt Chart Legend

```
Color Bars = Task execution
Gray Bars = CPU idle
Numbers = Time units
Vertical lines = Time boundaries
```

---

## 🎓 Educational Use

### For Learning:
- ✓ Visualize scheduling concepts
- ✓ Compare algorithms
- ✓ Understand deadline misses
- ✓ Practice schedulability tests

### For Teaching:
- ✓ Interactive demonstrations
- ✓ Hands-on exercises
- ✓ Assignment validation
- ✓ Exam preparation

---

## 📞 Quick Help

**Documentation:**
- `README.md` - Installation & setup
- `USER_GUIDE.md` - Detailed tutorial
- `DOCUMENTATION.md` - Technical details
- `HOW_IT_WORKS.md` - Algorithm explanation

**Support:**
- GitHub Issues
- Email support
- User community

---

## 🚀 Example Workflow

```
1. Load Sample Tasks
   ↓
2. Select RMS
   ↓
3. Simulate
   ↓
4. Note: U=0.75, No misses ✅
   ↓
5. Switch to EDF
   ↓
6. Simulate Again
   ↓
7. Compare Results
```

---

## 📊 Status Indicators

| Indicator | Meaning |
|-----------|---------|
| 🚀 | Simulation starting |
| ✅ | Success |
| ❌ | Failure/Error |
| ⏸️ | Idle time info |
| 💡 | Utilization info |
| 📈 | Results section |
| ✓ | Task added/action completed |

---

## 🎯 Goals by User Type

### Student Goals:
- Understand RMS & EDF
- Calculate utilization
- Predict schedulability
- Verify homework

### Educator Goals:
- Demonstrate concepts
- Create exercises
- Show comparisons
- Generate examples

### Engineer Goals:
- Analyze task sets
- Test schedulability
- Compare algorithms
- Validate designs

---

## 📈 Performance Tips

| Situation | Recommendation |
|-----------|----------------|
| Too many tasks | Limit to 10-15 |
| Slow rendering | Reduce sim time |
| Can't see all | Scroll horizontally |
| Need more time | Modify code (SIMULATION_TIME) |

---

## 🔢 Sample Calculations

### Example 1: Calculate Utilization
```
Given: T1(2,10), T2(3,15)
U = C1/T1 + C2/T2
U = 2/10 + 3/15
U = 0.20 + 0.20
U = 0.40 (40%)
```

### Example 2: RMS Test
```
Given: 3 tasks, U=0.75
Bound = 3(2^(1/3)-1) = 0.78
0.75 < 0.78 ✅ Schedulable
```

### Example 3: EDF Test
```
Given: 2 tasks, U=0.95
Condition: U ≤ 1.0
0.95 ≤ 1.0 ✅ Schedulable
```

---

## 🎨 Color Coding

- **Blue/Green/Yellow:** Task colors
- **Gray:** IDLE time
- **Red text:** Errors/misses (logs)
- **Green text:** Success (logs)

---

## 📅 Typical Session

```
Duration: 5-10 minutes

1. Launch (30s)
2. Add tasks (1-2m)
3. Run RMS (10s)
4. Analyze (30s)
5. Run EDF (10s)
6. Compare (1m)
7. Experiment (2-5m)
```

---

## 🏆 Success Criteria

✅ **Successful Simulation:**
- All tasks added correctly
- Algorithm selected
- Gantt chart displays
- Results logged
- No errors shown

---

## 📖 Learning Path

```
Beginner → Intermediate → Advanced
   ↓            ↓            ↓
Sample      Add tasks    Custom
tasks       Calculate U  workloads
            Compare      Optimize
            algorithms   parameters
```

---

**Print this page for quick reference while using the simulator!**

**Version:** 1.0.0 | **Date:** October 15, 2025
