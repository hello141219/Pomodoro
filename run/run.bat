@echo off
setlocal

:: Check if the JAR exists in the target directory
if exist target\pomodoro-timer-1.0.0.jar (
    java -jar target\pomodoro-timer-1.0.0.jar
) else if exist target\pomodoro-timer.jar (
    java -jar target\pomodoro-timer.jar
) else (
    :: If no JAR is found, try to run from the classes directory
    if exist target\classes (
        echo No JAR file found, running from compiled classes...
        java -cp target\classes pomodoro.Main
    ) else (
        echo No compiled files found. Please build the project first using:
        echo build.bat
        
        :: Ask if user wants to build now
        set /p answer=Do you want to build the project now? (y/n): 
        if /i "%answer%"=="y" (
            call build.bat
            if %ERRORLEVEL% == 0 (
                call run.bat
            )
        )
    )
)

endlocal 