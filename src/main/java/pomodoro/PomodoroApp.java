package pomodoro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.imageio.ImageIO;

public class PomodoroApp extends JFrame {
    private JLabel timeLabel;
    private JButton startButton;
    private JButton resetButton;
    private JButton settingsButton;
    private Timer timer;
    private int timeLeft;
    private boolean isRunning;
    private int workTime = 25 * 60; // Default 25 minutes in seconds
    private int breakTime = 5 * 60; // Default 5 minutes in seconds
    private boolean isWorkTime;
    private String taskDescription = "Work Session";
    private JLabel taskLabel;
    private boolean soundEnabled = true;
    private AtomicBoolean shouldStopSound = new AtomicBoolean(false);
    private Thread soundThread;

    public PomodoroApp() {
        setTitle("Pomodoro Timer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        // Set application icon if available
        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/appicon.svg"));
            if (icon != null) {
                setIconImage(icon);
            }
        } catch (Exception e) {
            System.err.println("Could not load application icon: " + e.getMessage());
        }
        
        // Initialize components
        timeLabel = new JLabel("25:00", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 40));
        
        taskLabel = new JLabel(taskDescription, SwingConstants.CENTER);
        taskLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        startButton = new JButton("Start");
        resetButton = new JButton("Reset");
        settingsButton = new JButton("Settings");
        
        // Layout
        setLayout(new BorderLayout(10, 10));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(taskLabel, BorderLayout.NORTH);
        topPanel.add(timeLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(settingsButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Initialize timer state
        timeLeft = workTime;
        isRunning = false;
        isWorkTime = true;
        
        // Add button listeners
        startButton.addActionListener(e -> toggleTimer());
        resetButton.addActionListener(e -> resetTimer());
        settingsButton.addActionListener(e -> showSettingsDialog());
        
        // Create timer
        timer = new Timer(1000, e -> updateTimer());
        
        // Add some padding
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void playNotificationSound() {
        if (!soundEnabled) return;
        
        shouldStopSound.set(false);
        soundThread = new Thread(() -> {
            while (!shouldStopSound.get()) {
                try {
                    Toolkit.getDefaultToolkit().beep();
                    Thread.sleep(1000); // Beep every second
                } catch (Exception e) {
                    // If sound fails, just continue silently
                }
            }
        });
        soundThread.start();
    }
    
    private void stopNotificationSound() {
        shouldStopSound.set(true);
        if (soundThread != null) {
            try {
                soundThread.join(100); // Wait for the sound thread to stop
            } catch (InterruptedException e) {
                // Ignore interruption
            }
        }
    }
    
    private void showSettingsDialog() {
        JDialog dialog = new JDialog(this, "Settings", true);
        dialog.setLayout(new GridLayout(0, 2, 10, 10));
        
        // Work time settings
        JTextField workMinutes = new JTextField(String.valueOf(workTime / 60));
        JTextField breakMinutes = new JTextField(String.valueOf(breakTime / 60));
        JTextField taskField = new JTextField(taskDescription);
        JCheckBox soundToggle = new JCheckBox("Enable Sound", soundEnabled);
        
        dialog.add(new JLabel("Work Time (minutes):"));
        dialog.add(workMinutes);
        dialog.add(new JLabel("Break Time (minutes):"));
        dialog.add(breakMinutes);
        dialog.add(new JLabel("Task Description:"));
        dialog.add(taskField);
        dialog.add(new JLabel("Notification Sound:"));
        dialog.add(soundToggle);
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                int newWorkTime = Integer.parseInt(workMinutes.getText()) * 60;
                int newBreakTime = Integer.parseInt(breakMinutes.getText()) * 60;
                
                if (newWorkTime <= 0 || newBreakTime <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Please enter positive numbers!");
                    return;
                }
                
                workTime = newWorkTime;
                breakTime = newBreakTime;
                taskDescription = taskField.getText().trim();
                soundEnabled = soundToggle.isSelected();
                
                if (taskDescription.isEmpty()) {
                    taskDescription = "Work Session";
                }
                
                resetTimer();
                taskLabel.setText(taskDescription);
                dialog.dispose();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers!");
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        dialog.add(new JLabel("")); // Empty label for grid alignment
        dialog.add(buttonPanel);
        
        dialog.setSize(300, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void toggleTimer() {
        if (isRunning) {
            timer.stop();
            startButton.setText("Start");
        } else {
            timer.start();
            startButton.setText("Pause");
        }
        isRunning = !isRunning;
    }
    
    private void resetTimer() {
        timer.stop();
        isRunning = false;
        isWorkTime = true;
        timeLeft = workTime;
        startButton.setText("Start");
        taskLabel.setText(taskDescription);
        updateDisplay();
    }
    
    private void updateTimer() {
        if (timeLeft > 0) {
            timeLeft--;
            updateDisplay();
        } else {
            timer.stop();
            isRunning = false;
            String status = isWorkTime ? "Work" : "Break";
            String message = isWorkTime ? 
                "Time's up for: " + taskDescription + "\nTake a break!" :
                "Break time is over!\nReady to start: " + taskDescription;
            
            // Start playing notification sound
            playNotificationSound();
            
            // Show notification
            JOptionPane.showMessageDialog(this, message);
            
            // Stop the sound after user acknowledges
            stopNotificationSound();
            
            // Switch between work and break time
            isWorkTime = !isWorkTime;
            timeLeft = isWorkTime ? workTime : breakTime;
            taskLabel.setText(isWorkTime ? taskDescription : "Break Time");
            updateDisplay();
            
            // Automatically start the next session
            if (isWorkTime) {
                startButton.setText("Start");
            } else {
                // If it's break time, start automatically
                timer.start();
                isRunning = true;
                startButton.setText("Pause");
            }
        }
    }
    
    private void updateDisplay() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PomodoroApp app = new PomodoroApp();
            app.setVisible(true);
        });
    }
} 