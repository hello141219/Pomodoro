@echo off
setlocal

:: Check if mvn command is available
where mvn >nul 2>nul
if %ERRORLEVEL% == 0 (
    echo Building with Maven...
    call mvn clean package
    if %ERRORLEVEL% == 0 (
        echo Build successful! The executable JAR is in the target directory.
        echo Run with: java -jar target\pomodoro-timer-1.0.0.jar
    ) else (
        echo Maven build failed.
    )
) else (
    echo Maven not found, using manual compilation...
    
    :: Create directories if they don't exist
    if not exist target\classes mkdir target\classes
    
    :: Compile
    echo Compiling Java files...
    javac -d target\classes src\main\java\pomodoro\*.java
    
    :: Copy resources
    echo Copying resources...
    if not exist target\classes mkdir target\classes
    xcopy /E /Y resources\* target\classes\
    
    :: Create manifest file
    echo Creating manifest...
    echo Main-Class: pomodoro.Main> target\MANIFEST.MF
    
    :: Create jar
    echo Creating JAR file...
    jar cfm target\pomodoro-timer.jar target\MANIFEST.MF -C target\classes .
    
    if %ERRORLEVEL% == 0 (
        echo Build successful! The executable JAR is in the target directory.
        echo Run with: java -jar target\pomodoro-timer.jar
    ) else (
        echo Build failed.
    )
)

endlocal 