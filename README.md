# Pomodoro Timer Application

A simple Java-based Pomodoro Timer application to help you manage your work and break times effectively.

## Features

- Customizable work and break durations
- Custom task descriptions
- Start/Pause functionality
- Reset timer option
- Visual countdown display
- Sound notifications when timer ends (continues until user acknowledges)
- Automatically starts break timer after work session
- Task-specific notifications when sessions end
- Settings dialog for easy customization

## Project Structure

```
pomodoro-timer/
├── src/
│   └── main/
│       └── java/
│           └── pomodoro/
│               ├── Main.java        # Application entry point
│               └── PomodoroApp.java # Main application logic
├── resources/
│   └── appicon.svg                  # Application icon
├── pom.xml                          # Maven build file
└── README.md                        # This file
```

## Requirements

- Java 11 or higher
- Maven for building (optional)

## How to Build

Using Maven:

```
mvn clean package
```

This will create an executable JAR file in the `target/` directory.

## How to Run

After building:

```
java -jar target/pomodoro-timer-1.0.0.jar
```

Or directly using the source:

```
javac -d target/classes src/main/java/pomodoro/*.java
java -cp target/classes pomodoro.Main
```

## How to Use

1. Click the "Settings" button to customize:
   - Work duration (in minutes)
   - Break duration (in minutes)
   - Task description
   - Enable/disable sound notifications
2. Click the "Start" button to begin a Pomodoro session
3. The timer will count down from your set work duration
4. You can pause the timer at any time by clicking the "Pause" button
5. Use the "Reset" button to reset the timer to the beginning
6. When the work session ends, you'll hear a sound notification (if enabled) and see a message
   - The sound will continue until you acknowledge the message
   - The break timer will start automatically
7. After the break session, you'll get another notification to start working again

## Controls

- Settings Button: Customize work/break times, task description, and sound settings
- Start/Pause Button: Toggle the timer
- Reset Button: Reset the timer to the beginning of a work session

## Default Settings

- Work Time: 25 minutes
- Break Time: 5 minutes
- Task Description: "Work Session"
- Sound Notifications: Enabled 