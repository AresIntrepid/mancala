// Routes view events (pit click, undo, new game, style choice) to the model and propagates model changes back to the view. 
// Decide exact callbacks/signals and when to enable/disable Undo.

package mancala.controller;

import mancala.view.MancalaFrame;
import mancala.view.StyleSelectPanel;
import mancala.view.BoardPanel;
import mancala.view.ControlPanel;
import mancala.model.MancalaModel;
import mancala.style.BoardStyle;
import mancala.style.StyleA;
import mancala.style.StyleB;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MancalaController {
    private MancalaFrame frame;
    private BoardStyle currentStyle;
    private MancalaModel model;
    
    public MancalaController(MancalaFrame frame) {
        this.frame = frame;
        setupListeners();
    }
    
    /**
     * Updates the view based on the current model state.
     * Called whenever the model state changes.
     */
    private void updateView() {
        if (model == null) {
            return;
        }
        
        ControlPanel controlPanel = frame.getControlPanel();
        BoardPanel boardPanel = frame.getBoardPanel();
        
        // Update status label based on game state
        if (model.isGameOver()) {
            int winner = model.getWinner();
            if (winner == 1) {
                controlPanel.setStatusText("Player A wins!");
            } else if (winner == 2) {
                controlPanel.setStatusText("Player B wins!");
            } else {
                controlPanel.setStatusText("It's a tie!");
            }
        } else {
            int currentPlayer = model.getCurrentPlayer();
            if (currentPlayer == 1) {
                controlPanel.setStatusText("Player A's turn");
            } else if (currentPlayer == 2) {
                controlPanel.setStatusText("Player B's turn");
            }
        }
        
        // Update board panel with current board state
        int[] boardState = model.getBoardState();
        boardPanel.setBoardState(boardState);
        
        // Update undo button (basic implementation)
        // For now, just enable it if game is not over
        controlPanel.setUndoEnabled(!model.isGameOver());
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
                int stones = Integer.parseInt(parts[1]);
                
                // Create style object based on name
                BoardStyle style = createStyleFromName(styleName);
                switchStyle(style);
                
                // Initialize model with user-selected stones per pit
                model = new MancalaModel(6, stones); // 6 pits per side
                model.startGame(1); // Start with Player 1 (Player A)
                
                // Add ChangeListener to model to update view when state changes
                model.addListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        updateView();
                    }
                });
                
                // Initial view update
                updateView();
                
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
    
    /**
     * Returns the model instance. Used by view components that need direct model access.
     * 
     * @return The MancalaModel instance, or null if not initialized
     */
    public MancalaModel getModel() {
        return model;
    }
}
