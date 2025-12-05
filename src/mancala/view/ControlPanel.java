package mancala.view;

import javax.swing.*;
import java.awt.*;

/**
 * Control panel that displays game controls and status information.
 * Contains undo button, new game button, status label, and style switching buttons.
 * 
 * @author CS151 Group Project
 * @version 1.0
 */
public class ControlPanel extends JPanel {
    private JButton undoButton;
    private JLabel statusLabel;
    private JButton woodButton;
    private JButton neonButton;
    private JButton newGameButton; 
    
    /**
     * Constructs a new ControlPanel with all UI components.
     * Sets up undo button, new game button, status label, and style buttons.
     */
    public ControlPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // WEST: Undo button + new game button
        JPanel leftButtons = new JPanel(new FlowLayout());
        undoButton = new JButton("Undo");
        undoButton.setEnabled(false);
        newGameButton = new JButton("New Game");
        leftButtons.add(undoButton);
        leftButtons.add(newGameButton); 
        add(leftButtons, BorderLayout.WEST);
        
        // CENTER: Status label
        statusLabel = new JLabel("Player A's turn");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusLabel, BorderLayout.CENTER);
        
        // EAST: Style buttons for switching during gameplay
        JPanel styleButtonsPanel = new JPanel(new FlowLayout());
        woodButton = new JButton("Wood");
        neonButton = new JButton("Neon");
        styleButtonsPanel.add(woodButton);
        styleButtonsPanel.add(neonButton);
        add(styleButtonsPanel, BorderLayout.EAST);
    }
    
    /**
     * Updates the status label text.
     * 
     * @param text The text to display in the status label
     */
    public void setStatusText(String text) {
        statusLabel.setText(text);
    }
    
    /**
     * Enables or disables the undo button.
     * 
     * @param enabled true to enable the button, false to disable it
     */
    public void setUndoEnabled(boolean enabled) {
        undoButton.setEnabled(enabled);
    }
    
    /**
     * Gets the undo button for attaching action listeners.
     * 
     * @return The undo button
     */
    public JButton getUndoButton() {
        return undoButton;
    }
    
    /**
     * Gets the Wood style button for attaching action listeners.
     * 
     * @return The Wood style button
     */
    public JButton getWoodButton() {
        return woodButton;
    }
    
    /**
     * Gets the Neon style button for attaching action listeners.
     * 
     * @return The Neon style button
     */
    public JButton getNeonButton() {
        return neonButton;
    }
    
    /**
     * Gets the new game button for attaching action listeners.
     * 
     * @return The new game button
     */
    public JButton getNewGameButton() {
        return newGameButton;
    }
}
