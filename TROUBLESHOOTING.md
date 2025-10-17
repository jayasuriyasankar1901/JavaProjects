# 🔧 Troubleshooting Guide - Real-Time Scheduling Simulator

## Common Problems & Solutions

---

## 🚫 Application Won't Start

### Problem 1: "JavaFX runtime components are missing"

**Error Message:**
```
Error: JavaFX runtime components are missing, and are required to run this application
```

**Cause:** JavaFX SDK not properly configured

**Solutions:**

✅ **Solution 1:** Verify JavaFX SDK path
```powershell
# Check if JavaFX exists
Test-Path "C:\javafx-sdk-25\lib"

# Should show: True
```

✅ **Solution 2:** Add correct VM options
```
--module-path "C:\javafx-sdk-25\lib"
--add-modules javafx.controls,javafx.fxml
```

✅ **Solution 3:** Check Java version compatibility
```powershell
java -version
# Should be Java 11 or higher
```

✅ **Solution 4:** Reinstall JavaFX
1. Download from [gluonhq.com/products/javafx](https://gluonhq.com/products/javafx/)
2. Extract to known location
3. Update path in run command

---

### Problem 2: "Module not found" Error

**Error Message:**
```
Error: Module javafx.controls not found
```

**Solutions:**

✅ **Check module path:**
```powershell
# Windows
dir "C:\javafx-sdk-25\lib\*.jar"

# Should list:
# javafx.base.jar
# javafx.controls.jar
# javafx.fxml.jar
# javafx.graphics.jar
```

✅ **Use full path (not relative):**
```
❌ --module-path "javafx-sdk\lib"
✅ --module-path "C:\javafx-sdk-25\lib"
```

---

### Problem 3: Application Window Doesn't Appear

**Symptoms:** Command runs but no window appears

**Solutions:**

✅ **Check for background errors:**
```powershell
# Run in foreground to see errors
java --module-path "C:\javafx-sdk-25\lib" --add-modules javafx.controls -cp bin com.rts.RealTimeSchedulerApp
```

✅ **Verify DISPLAY variable (Linux/Mac):**
```bash
echo $DISPLAY
# Should show something like :0 or :1
```

✅ **Check for multiple Java installations:**
```powershell
where java
# Should point to correct Java 11+ installation
```

---

## 🔨 Compilation Errors

### Problem 4: "Package does not exist"

**Error Message:**
```
error: package javafx.application does not exist
```

**Solutions:**

✅ **Add JavaFX to classpath during compilation:**
```powershell
javac --module-path "C:\javafx-sdk-25\lib" --add-modules javafx.controls,javafx.fxml -d bin src\main\java\com\rts\*.java
```

✅ **Check if compiling all necessary files:**
```powershell
# Compile in correct order:
javac --module-path "C:\javafx-sdk-25\lib" --add-modules javafx.controls -d bin src\main\java\com\rts\model\*.java
javac --module-path "C:\javafx-sdk-25\lib" --add-modules javafx.controls -d bin -cp bin src\main\java\com\rts\algorithm\*.java
javac --module-path "C:\javafx-sdk-25\lib" --add-modules javafx.controls -d bin -cp bin src\main\java\com\rts\*.java
```

---

### Problem 5: "Cannot find symbol"

**Error Message:**
```
error: cannot find symbol Task
```

**Solutions:**

✅ **Compile dependencies first:**
```powershell
# Order matters!
1. Model classes (Task, ScheduleResult, TaskSet)
2. Algorithm classes (Scheduler, RMS, EDF)
3. Main application
```

✅ **Clean and rebuild:**
```powershell
# Remove old compiled files
Remove-Item -Recurse -Force bin\*

# Recompile all
javac --module-path ... (full command)
```

---

## 🎨 UI Issues

### Problem 6: Blank Gantt Chart

**Symptoms:** Gantt chart area is empty/white

**Solutions:**

✅ **Add tasks first:**
```
1. Click "📝 Load Sample" or manually add tasks
2. Verify tasks appear in task list
3. Then click "▶️ Simulate"
```

✅ **Check simulation was run:**
```
Look for "🚀 Starting simulation..." in log area
If missing → Click "▶️ Simulate"
```

✅ **Verify tasks have execution time > 0:**
```
Check task list shows C > 0 for all tasks
```

---

### Problem 7: Tasks Not Appearing in List

**Symptoms:** Click "Add Task" but nothing happens

**Solutions:**

✅ **Check all required fields:**
```
❌ Empty Task ID
❌ Empty Execution Time
❌ Empty Period

✅ All fields must have values
```

✅ **Verify numeric values:**
```
Execution Time: Must be integer (1, 2, 3...)
Period: Must be integer
Deadline: Must be integer or empty
```

✅ **Look for error dialogs:**
```
Check if warning dialog appeared
Read error message
Correct the issue
```

---

### Problem 8: "Invalid Input" Error

**Error Message:** Dialog shows "Invalid Input"

**Causes & Solutions:**

✅ **Non-numeric values:**
```
❌ Execution Time: "abc"
✅ Execution Time: "2"

❌ Period: "10.5"
✅ Period: "10"
```

✅ **Negative or zero values:**
```
❌ Execution Time: 0
✅ Execution Time: 1

❌ Period: -5
✅ Period: 5
```

✅ **Invalid relationships:**
```
❌ C > T (execution > period)
✅ C ≤ T
```

---

## 📊 Simulation Issues

### Problem 9: Unexpected Deadline Misses

**Symptoms:** Deadline misses even though U < 1.0

**Possible Causes:**

✅ **Using RMS with U > 0.69:**
```
Calculate: n(2^(1/n) - 1)
For 3 tasks: 0.78
If U = 0.75 < 0.78 → Should work
If U = 0.80 > 0.78 → May fail with RMS

Solution: Switch to EDF
```

✅ **Execution time > Period:**
```
Check: C ≤ T for all tasks
If C > T → Task cannot meet deadline
```

✅ **Deadline < Execution time:**
```
Check: D ≥ C for all tasks
If D < C → Impossible to meet deadline
```

---

### Problem 10: All Tasks Show IDLE

**Symptoms:** Gantt chart only shows gray IDLE bars

**Solutions:**

✅ **Verify tasks were added:**
```
Check task list view
Should show: "Task T1: C=..., T=..., D=..."
If empty → Add tasks first
```

✅ **Check task parameters:**
```
All tasks must have:
- Execution Time > 0
- Period > 0
- Valid remainingTime (set by reset())
```

✅ **Restart simulation:**
```
1. Click "🗑️ Clear All"
2. Add tasks again
3. Click "▶️ Simulate"
```

---

### Problem 11: Wrong Algorithm Running

**Symptoms:** Results don't match expected algorithm behavior

**Solutions:**

✅ **Verify algorithm selection:**
```
Check dropdown shows correct algorithm:
- "Rate Monotonic Scheduling (RMS)"
- "Earliest Deadline First (EDF)"
```

✅ **Clear previous results:**
```
Click "🗑️ Clear All" to reset
Add tasks fresh
Select correct algorithm
Simulate
```

---

## 🐛 Runtime Errors

### Problem 12: NullPointerException

**Error Message:**
```
Exception in thread "JavaFX Application Thread" java.lang.NullPointerException
```

**Solutions:**

✅ **Check task list not empty:**
```java
if (tasks.isEmpty()) {
    showAlert("No Tasks", "Please add tasks before running simulation.");
    return;
}
```

✅ **Verify algorithm selected:**
```java
if (algorithmComboBox.getSelectionModel().getSelectedItem() == null) {
    algorithmComboBox.getSelectionModel().selectFirst();
}
```

---

### Problem 13: OutOfMemoryError

**Error Message:**
```
java.lang.OutOfMemoryError: Java heap space
```

**Solutions:**

✅ **Increase heap size:**
```powershell
java -Xmx512m --module-path "C:\javafx-sdk-25\lib" --add-modules javafx.controls -cp bin com.rts.RealTimeSchedulerApp
```

✅ **Reduce simulation time:**
```java
// In RealTimeSchedulerApp.java
private final int SIMULATION_TIME = 20; // Instead of 40
```

---

## 🔍 Data/Logic Issues

### Problem 14: Incorrect CPU Utilization

**Symptoms:** Displayed utilization doesn't match calculation

**Solution:**

✅ **Verify calculation:**
```
Manual: U = Σ(Ci/Ti)

Example:
T1: 2/10 = 0.20
T2: 3/15 = 0.20
Total = 0.40

If display shows different → Check task parameters
```

✅ **Check for duplicate tasks:**
```
Look for same Task ID multiple times
Remove duplicates
Recalculate
```

---

### Problem 15: Missing Deadline Not Detected

**Symptoms:** Expected deadline miss but none reported

**Solutions:**

✅ **Understand deadline miss definition:**
```
Miss occurs when:
- New task instance released
- Previous instance still has remainingTime > 0

Not when:
- Task finishes late but before next release
```

✅ **Check simulation time:**
```
Simulation might be too short to show miss
Increase SIMULATION_TIME or check later time units
```

---

## 🖥️ Platform-Specific Issues

### Windows

**Problem:** PowerShell execution policy
```powershell
# If scripts blocked:
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

**Problem:** Path with spaces
```powershell
# Use quotes:
java --module-path "C:\Program Files\javafx-sdk-25\lib" ...
```

---

### macOS

**Problem:** Security warning
```bash
# Allow app in Security & Privacy settings
System Preferences → Security & Privacy → Allow
```

**Problem:** Incorrect Java version
```bash
# List installed Java versions
/usr/libexec/java_home -V

# Use specific version
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
```

---

### Linux

**Problem:** Missing graphics libraries
```bash
# Install OpenJFX
sudo apt-get install openjfx  # Ubuntu/Debian
sudo dnf install openjfx      # Fedora
```

**Problem:** Display not set
```bash
export DISPLAY=:0
```

---

## 📝 Maven-Specific Issues

### Problem 16: Maven not found

**Error:**
```
mvn: command not found
```

**Solutions:**

✅ **Install Maven:**
```powershell
# Windows (Chocolatey)
choco install maven

# Or download from: https://maven.apache.org/
```

✅ **Add to PATH:**
```powershell
# Windows
$env:Path += ";C:\apache-maven-3.9.0\bin"

# Verify
mvn -version
```

---

### Problem 17: Maven build fails

**Error:**
```
[ERROR] Failed to execute goal...
```

**Solutions:**

✅ **Clean and rebuild:**
```bash
mvn clean install
```

✅ **Update dependencies:**
```bash
mvn clean compile
```

✅ **Check pom.xml:**
```xml
<!-- Verify correct versions -->
<java.version>11</java.version>
<javafx.version>17.0.1</javafx.version>
```

---

## 🎯 Best Practices to Avoid Issues

### ✅ Before Starting:
1. Install Java 11+
2. Download JavaFX SDK
3. Note exact paths
4. Test Java installation

### ✅ During Development:
1. Compile frequently
2. Test with sample data
3. Read error messages
4. Check logs

### ✅ When Running:
1. Start with samples
2. Add tasks incrementally
3. Verify each step
4. Save working configurations

---

## 🆘 Still Having Issues?

### Diagnostic Steps:

1. **Collect Information:**
   ```
   - OS and version
   - Java version (java -version)
   - JavaFX version
   - Exact error message
   - Steps to reproduce
   ```

2. **Check Logs:**
   ```
   - Application log area
   - Console output
   - System error logs
   ```

3. **Minimal Test:**
   ```
   - Create new task set with just 1 task
   - Run simulation
   - If works → problem with original task set
   - If fails → problem with setup
   ```

4. **Get Help:**
   ```
   - GitHub Issues
   - Stack Overflow
   - JavaFX community
   - Course instructor/TA
   ```

---

## 📋 Error Code Reference

| Code | Error | Solution |
|------|-------|----------|
| E001 | JavaFX missing | Install JavaFX SDK |
| E002 | Module not found | Check module-path |
| E003 | Class not found | Recompile |
| E004 | Null pointer | Add tasks first |
| E005 | Invalid input | Check numeric values |
| E006 | Out of memory | Increase heap size |

---

## 🔧 Developer Debug Mode

**Enable detailed logging:**

```java
// Add to RealTimeSchedulerApp.java
private static final boolean DEBUG = true;

if (DEBUG) {
    System.out.println("Tasks: " + tasks);
    System.out.println("Algorithm: " + selectedAlgo);
    System.out.println("Timeline: " + result.getExecutionTimeline());
}
```

---

## ✅ Verification Checklist

Before reporting an issue, verify:

- [ ] Java 11+ installed
- [ ] JavaFX SDK downloaded
- [ ] Correct paths used
- [ ] All files compiled
- [ ] Tasks added to list
- [ ] Algorithm selected
- [ ] Simulation button clicked
- [ ] Error message read carefully
- [ ] Sample tasks tested
- [ ] Documentation reviewed

---

**Most issues can be solved by:**
1. Checking paths
2. Recompiling
3. Reading error messages
4. Using sample data

**Happy debugging! 🐛→✅**
