#!/bin/bash

# Check if maven is installed
if command -v mvn &> /dev/null; then
    echo "Building with Maven..."
    mvn clean package
    if [ $? -eq 0 ]; then
        echo "Build successful! The executable JAR is in the target directory."
        echo "Run with: java -jar target/pomodoro-timer-1.0.0.jar"
    else
        echo "Maven build failed."
    fi
else
    echo "Maven not found, using manual compilation..."
    
    # Create directories if they don't exist
    mkdir -p target/classes
    
    # Compile
    echo "Compiling Java files..."
    javac -d target/classes src/main/java/pomodoro/*.java
    
    # Copy resources
    echo "Copying resources..."
    mkdir -p target/classes
    cp -r resources/* target/classes/
    
    # Create manifest file
    echo "Creating manifest..."
    echo "Main-Class: pomodoro.Main" > target/MANIFEST.MF
    
    # Create jar
    echo "Creating JAR file..."
    jar cfm target/pomodoro-timer.jar target/MANIFEST.MF -C target/classes .
    
    if [ $? -eq 0 ]; then
        echo "Build successful! The executable JAR is in the target directory."
        echo "Run with: java -jar target/pomodoro-timer.jar"
    else
        echo "Build failed."
    fi
fi 