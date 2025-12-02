// Entry point that launches the UI on the EDT and switches from style select → stones prompt → game screen. 
// Implement main() after we agree on toolkit and window flow.

package mancala;

import javax.swing.*;
import mancala.view.MancalaFrame;
import mancala.controller.MancalaController;

public class MancalaTest {
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }
        
        // Launch UI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MancalaFrame frame = new MancalaFrame();
            @SuppressWarnings("unused")
            MancalaController controller = new MancalaController(frame); // Sets up event listeners
            frame.setVisible(true);
        });
    }
}
