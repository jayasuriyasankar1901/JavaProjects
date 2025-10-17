# Real-Time Scheduling Simulator

## Overview
The Real-Time Scheduling Simulator is a JavaFX desktop application designed to simulate and visualize real-time scheduling algorithms, specifically Rate Monotonic Scheduling (RMS) and Earliest Deadline First (EDF). The application provides an intuitive interface for users to input task parameters, run simulations, and view the results in a Gantt chart format.

## Features
- **Task Input Interface**: Users can add, remove, and validate tasks with specific parameters such as execution time, period, and deadline.
- **Scheduling Algorithms**: Implements Rate Monotonic Scheduling (RMS) and Earliest Deadline First (EDF) algorithms to manage task scheduling.
- **Gantt Chart Visualization**: Displays the execution timeline of tasks, allowing users to visualize how tasks are scheduled over time.
- **Simulation Controls**: Start, pause, and reset simulations to analyze different task sets and scheduling behaviors.

## Project Structure
```
rts-simulator
├── src
│   └── main
│       ├── java
│       │   └── com
│       │       └── rts
│       │           ├── RealTimeSchedulerApp.java
│       │           ├── controller
│       │           │   ├── MainController.java
│       │           │   ├── TaskInputController.java
│       │           │   └── SimulationController.java
│       │           ├── model
│       │           │   ├── Task.java
│       │           │   ├── ScheduleResult.java
│       │           │   └── TaskSet.java
│       │           ├── algorithm
│       │           │   ├── Scheduler.java
│       │           │   ├── RateMonotonicScheduler.java
│       │           │   └── EarliestDeadlineFirstScheduler.java
│       │           ├── view
│       │           │   ├── GanttChart.java
│       │           │   └── TaskInputPanel.java
│       │           └── util
│       │               ├── SchedulabilityAnalyzer.java
│       │               └── ValidationUtils.java
│       └── resources
│           ├── fxml
│           │   ├── MainView.fxml
│           │   ├── TaskInputView.fxml
│           │   └── SimulationView.fxml
│           ├── css
│           │   └── styles.css
│           └── application.properties
├── pom.xml
└── README.md
```

## Setup Instructions
1. **Clone the Repository**: 
   ```
   git clone <repository-url>
   cd rts-simulator
   ```

2. **Build the Project**: 
   Use Maven to build the project:
   ```
   mvn clean install
   ```

3. **Run the Application**: 
   Execute the main application:
   ```
   mvn javafx:run
   ```

## Usage Guidelines
- Launch the application and navigate to the task input interface to enter task parameters.
- After inputting tasks, proceed to the simulation controls to start the scheduling simulation.
- View the Gantt chart to analyze the scheduling results and task execution timelines.

## Contributing
Contributions are welcome! Please submit a pull request or open an issue for any enhancements or bug fixes.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.