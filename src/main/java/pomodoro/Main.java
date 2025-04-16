package pomodoro;

import javax.swing.SwingUtilities;

/**
 * Main class for the Pomodoro Timer application.
 * This class serves as the entry point for the application.
 */
public class Main {
    
    /**
     * The main method that starts the Pomodoro Timer application.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Run the GUI in the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            // Create and show the main application window
            PomodoroApp app = new PomodoroApp();
            app.setVisible(true);
        });
    }
} 