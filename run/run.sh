#!/bin/bash

# Check if the JAR exists in the target directory
if [ -f target/pomodoro-timer-1.0.0.jar ]; then
    java -jar target/pomodoro-timer-1.0.0.jar
elif [ -f target/pomodoro-timer.jar ]; then
    java -jar target/pomodoro-timer.jar
else
    # If no JAR is found, try to run from the classes directory
    if [ -d target/classes ]; then
        echo "No JAR file found, running from compiled classes..."
        java -cp target/classes pomodoro.Main
    else
        echo "No compiled files found. Please build the project first using:"
        echo "./build.sh"
        
        # Ask if user wants to build now
        read -p "Do you want to build the project now? (y/n): " answer
        if [ "$answer" = "y" ] || [ "$answer" = "Y" ]; then
            ./build.sh && ./run.sh
        fi
    fi
fi 