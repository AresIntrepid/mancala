package mancala.view;

import java.awt.*;
import javax.swing.*;

/**
 * Main application window that manages the game screens.
 * Uses CardLayout to switch between style selection and game screens.
 * 
 * @author CS151 Group Project
 * @version 1.0
 */
public class MancalaFrame extends JFrame {
    private static final String STYLE_SELECT_CARD = "STYLE_SELECT";
    private static final String GAME_CARD = "GAME";
    
    private CardLayout cardLayout;
    private StyleSelectPanel styleSelectPanel;
    private JPanel gameScreen;
    private BoardPanel boardPanel;
    private ControlPanel controlPanel;
    
    /**
     * Constructs a new MancalaFrame and initializes all screens.
     * Starts with the style selection screen visible.
     */
    public MancalaFrame() {
        setTitle("Mancala");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        
        // Create style select screen
        styleSelectPanel = new StyleSelectPanel();
        add(styleSelectPanel, STYLE_SELECT_CARD);
        
        // Create game screen
        gameScreen = new JPanel(new BorderLayout());
        boardPanel = new BoardPanel();
        controlPanel = new ControlPanel();

        gameScreen.add(boardPanel, BorderLayout.CENTER);
        gameScreen.add(controlPanel, BorderLayout.SOUTH);
        
        add(gameScreen, GAME_CARD);
        
        // Start with style select screen
        showStyleSelect();
    }
    
    /**
     * Shows the style selection screen.
     */
    public void showStyleSelect() {
        cardLayout.show(getContentPane(), STYLE_SELECT_CARD);
    }
    
    /**
     * Shows the main game screen with board and controls.
     */
    public void showGame() {
        cardLayout.show(getContentPane(), GAME_CARD);
    }
    
    /**
     * Gets the style selection panel.
     * 
     * @return The StyleSelectPanel instance
     */
    public StyleSelectPanel getStyleSelectPanel() {
        return styleSelectPanel;
    }
    
    /**
     * Gets the board panel for game rendering.
     * 
     * @return The BoardPanel instance
     */
    public BoardPanel getBoardPanel() {
        return boardPanel;
    }
    
    /**
     * Gets the control panel for game controls.
     * 
     * @return The ControlPanel instance
     */
    public ControlPanel getControlPanel() {
        return controlPanel;
    }
}
