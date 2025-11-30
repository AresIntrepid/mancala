// Routes view events (pit click, undo, new game, style choice) to the model and propagates model changes back to the view. 
// Decide exact callbacks/signals and when to enable/disable Undo.

package mancala.controller;

import mancala.view.MancalaFrame;
import mancala.view.StyleSelectPanel;
import mancala.view.BoardPanel;
import mancala.view.ControlPanel;
import mancala.style.BoardStyle;
import mancala.style.StyleA;
import mancala.style.StyleB;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MancalaController {
    private MancalaFrame frame;
    private BoardStyle currentStyle;
    // TODO: Add model reference when MancalaModel is implemented
    // private MancalaModel model;
    
    public MancalaController(MancalaFrame frame) {
        this.frame = frame;
        setupListeners();
    }
    
    private void setupListeners() {
        StyleSelectPanel styleSelectPanel = frame.getStyleSelectPanel();
        ControlPanel controlPanel = frame.getControlPanel();
        
        // Listen for style selection from the initial style select screen
        // Format: "styleName:stones" (e.g., "Wood:3" or "Neon:4")
        styleSelectPanel.setStyleButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                String[] parts = command.split(":");
                String styleName = parts[0];
                @SuppressWarnings("unused")
                int stones = Integer.parseInt(parts[1]); // Will be used when model is implemented
                
                // Create style object based on name
                BoardStyle style = createStyleFromName(styleName);
                switchStyle(style);
                
                // TODO: Initialize model when MancalaModel is implemented
                // model = new MancalaModel();
                // model.startGame(stones);
                
                // Disable stones field
                styleSelectPanel.disableStonesField();
                
                // Switch to game screen
                frame.showGame();
            }
        });
        
        // Listen for style switching during gameplay
        controlPanel.getWoodButton().addActionListener(e -> switchStyle(new StyleA()));
        controlPanel.getNeonButton().addActionListener(e -> switchStyle(new StyleB()));
    }
    
    /**
     * Creates a BoardStyle instance from a style name string.
     * 
     * @param styleName "Wood" or "Neon"
     * @return The corresponding BoardStyle object
     */
    private BoardStyle createStyleFromName(String styleName) {
        if ("Wood".equals(styleName)) {
            return new StyleA();
        } else if ("Neon".equals(styleName)) {
            return new StyleB();
        }
        return new StyleA(); // Default to Wood
    }
    
    /**
     * Switches to the specified style and updates the board panel.
     * Can be called from both the initial style selection and during gameplay.
     * 
     * @param style The BoardStyle object to switch to
     */
    private void switchStyle(BoardStyle style) {
        currentStyle = style;
        
        // Apply style to board panel
        BoardPanel boardPanel = frame.getBoardPanel();
        boardPanel.setStyle(currentStyle);
    }
    
    public BoardStyle getCurrentStyle() {
        return currentStyle;
    }
}
