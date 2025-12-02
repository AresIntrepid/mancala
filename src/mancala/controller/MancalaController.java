/**
 * Controller class implementing the MVC pattern for the Mancala game.
 * Routes view events (pit clicks, undo, style changes) to the model and
 * propagates model state changes back to the view components.
 * 
 * <p>Responsibilities:
 * <ul>
 *   <li>Handles pit click events from BoardPanel</li>
 *   <li>Validates moves before applying them to the model</li>
 *   <li>Updates view components when model state changes</li>
 *   <li>Manages style switching (Wood/Neon)</li>
 *   <li>Initializes game with user-selected stone count</li>
 * </ul>
 * 
 * @author CS151 Group Project
 * @version 1.0
 */
package mancala.controller;

import mancala.view.MancalaFrame;
import mancala.view.StyleSelectPanel;
import mancala.view.BoardPanel;
import mancala.view.ControlPanel;
import mancala.view.PitClickListener;
import mancala.model.MancalaModel;
import mancala.style.BoardStyle;
import mancala.style.StyleA;
import mancala.style.StyleB;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MancalaController implements PitClickListener {
    private MancalaFrame frame;
    private BoardStyle currentStyle;
    private MancalaModel model;
    
    private int undosThisTurn = 0;
    private boolean lastActionWasUndo = false;
    private static final int MAX_UNDOS_PER_TURN = 3;
    
    /**
     * Constructs a new MancalaController and sets up all event listeners.
     * 
     * @param frame The main application frame containing all view components
     */
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
            int undosLeft = MAX_UNDOS_PER_TURN - undosThisTurn;
            String undoInfo = " (Undos left: " + undosLeft + ")";
            if (currentPlayer == 1) {
                controlPanel.setStatusText("Player A's turn" + undoInfo);
            } else if (currentPlayer == 2) {
                controlPanel.setStatusText("Player B's turn" + undoInfo);
            }
        }
        
        // Update board panel with current board state
        int[] boardState = model.getBoardState();
        int currentPlayer = model.getCurrentPlayer();
        boolean gameOver = model.isGameOver();
        boardPanel.setBoardState(boardState, currentPlayer, gameOver);
        
        // Update undo button based on all constraints
        controlPanel.setUndoEnabled(canUndo());
    }
    
    /**
     * Handles pit click events from BoardPanel.
     * Validates the click and calls model.applyMove if valid.
     * 
     * @param pitIndex The model index of the clicked pit (0-13)
     */
    @Override
    public void onPitClicked(int pitIndex) {
        if (model == null) {
            return;
        }
        
        ControlPanel controlPanel = frame.getControlPanel();
        
        // Validate: game must not be over
        if (model.isGameOver()) {
        	controlPanel.setStatusText("Game is over! No more moves allowed.");
            return; // Ignore clicks when game is over
        }
        
        // Validate: pit must belong to current player
        int currentPlayer = model.getCurrentPlayer();
        boolean isPlayerPit = false;
        if (currentPlayer == 1) {
            // Player 1 owns indices 0-5 (pits) and 6 (Mancala, but not clickable)
            isPlayerPit = (pitIndex >= 0 && pitIndex <= 5);
        } else if (currentPlayer == 2) {
            // Player 2 owns indices 7-12 (pits) and 13 (Mancala, but not clickable)
            isPlayerPit = (pitIndex >= 7 && pitIndex <= 12);
        }
        
        if (!isPlayerPit) {
        	String playerName = (currentPlayer == 1) ? "Player A" : "Player B";
            controlPanel.setStatusText("Not your pit! It's " + playerName + "'s turn.");
            return; // Ignore clicks on opponent's pits
        }
        
        // Validate: pit must have stones
        int stones = model.getStonesAtPit(pitIndex);
        if (stones == 0) {
        	controlPanel.setStatusText("That pit is empty! Choose a pit with stones.");
            return; // Ignore clicks on empty pits
        }
        // Track player before move to detect turn changes
        int playerBefore = model.getCurrentPlayer();
        
        // All validations passed, apply the move
        model.applyMove(pitIndex);
        
        // Reset lastActionWasUndo flag after a successful move
        lastActionWasUndo = false;
        
        // Check if turn changed after the move
        int playerAfter = model.getCurrentPlayer();
        if (playerBefore != playerAfter) {
            // Turn changed - reset undo counter for the new player's turn
            undosThisTurn = 0;
        }
     // Note: updateView() will be called automatically via ChangeListener
    }
    
    /**
     * Sets up all event listeners for view components.
     * Configures listeners for style selection, pit clicks, and style switching during gameplay.
     */
    private void setupListeners() {
        StyleSelectPanel styleSelectPanel = frame.getStyleSelectPanel();
        ControlPanel controlPanel = frame.getControlPanel();
        BoardPanel boardPanel = frame.getBoardPanel();
        
        // Set up pit click listener
        boardPanel.setPitClickListener(this);
        
     // Listen for undo button clicks
        controlPanel.getUndoButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model != null && canUndo()) {
                    model.undo();
                    undosThisTurn++;
                    lastActionWasUndo = true;
                    updateView(); // Make sure view refreshes
                }
            }
        });
        
     // Listen for new game button clicks
        controlPanel.getNewGameButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Go back to style select screen
                frame.showStyleSelect();
                
                // Reset controller state
                undosThisTurn = 0;
                lastActionWasUndo = false;
                model = null;
                
                // Re-enable the stones field
                styleSelectPanel.enableStonesField(); 
                
                // Go back to style select screen
                frame.showStyleSelect();
            }
        });
        
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
    
    /**
     * Gets the currently active board style.
     * 
     * @return The current BoardStyle, or null if no style has been selected
     */
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
    /**
     * Checks if undo is allowed based on game rules:
     * - Game must not be over
     * - Maximum 3 undos per turn
     * - Cannot undo twice in a row (must make a move between undos)
     * - Must have history to undo
     * 
     * @return true if undo is currently allowed
     */
    private boolean canUndo() {
        if (model == null || model.isGameOver()) {
            return false;
        }
        if (lastActionWasUndo) {
            return false; // No consecutive undos
        }
        if (undosThisTurn >= MAX_UNDOS_PER_TURN) {
            return false; // Max 3 undos per turn
        }
        return model.hasHistory(); // Check if there's anything to undo
    }
}
