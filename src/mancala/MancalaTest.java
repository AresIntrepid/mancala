/**
 * Entry point for the Mancala game application.
 * Launches the UI on the Event Dispatch Thread (EDT) and manages the flow from
 * style selection → stones prompt → game screen.
 * 
 * <p>To run from command line:
 * <pre>
 * javac -d . -sourcepath src src/mancala/MancalaTest.java && java mancala.MancalaTest
 * </pre>
 * 
 * <p>Or from IDE: Run MancalaTest.main()
 * 
 * <p>Test flow:
 * <ol>
 *   <li>Style selection screen appears</li>
 *   <li>Enter 3 or 4 stones per pit</li>
 *   <li>Click "Wood" or "Neon" to start game</li>
 *   <li>Click on pits to play (Player A starts on bottom row)</li>
 *   <li>Board updates automatically after each move</li>
 * </ol>
 * 
 * @author CS151 Group Project
 * @version 1.0
 */
package mancala;

import javax.swing.*;
import mancala.view.MancalaFrame;
import mancala.controller.MancalaController;

public class MancalaTest {
    /**
     * Main entry point for the application.
     * Sets up the system look and feel, creates the main frame and controller,
     * and displays the UI on the Event Dispatch Thread.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Set system look and feel for native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }
        
        // Launch UI on Event Dispatch Thread (required for Swing)
        SwingUtilities.invokeLater(() -> {
            MancalaFrame frame = new MancalaFrame();
            // Controller sets up all event listeners and connects model to view
            @SuppressWarnings("unused")
            MancalaController controller = new MancalaController(frame);
            frame.setVisible(true);
            
            // Test flow:
            // 1. Style selection screen appears
            // 2. Enter 3 or 4 stones per pit
            // 3. Click "Wood" or "Neon" to start game
            // 4. Click on pits to play (Player A starts on bottom row)
            // 5. Board updates automatically after each move
        });
    }
}
