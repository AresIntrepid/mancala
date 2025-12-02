// Hosts Undo/New Game buttons and a status label for turn/game-over messages. 
// Implement setters for status text and logic to toggle Undo based on model state.
package mancala.view;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    private JButton undoButton;
    private JLabel statusLabel;
    private JButton woodButton;
    private JButton neonButton;
    private JButton newGameButton; 
    
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
    
    public void setStatusText(String text) {
        statusLabel.setText(text);
    }
    
    public void setUndoEnabled(boolean enabled) {
        undoButton.setEnabled(enabled);
    }
    
    public JButton getUndoButton() {
        return undoButton;
    }
    
    public JButton getWoodButton() {
        return woodButton;
    }
    
    public JButton getNeonButton() {
        return neonButton;
    }
    
    public JButton getNewGameButton() {
        return newGameButton;
    }
}
