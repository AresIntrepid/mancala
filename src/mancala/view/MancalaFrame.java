// Top-level window that swaps between the style select screen and the main game screen. Decide layout manager (e.g., CardLayout) and what getters the controller needs.
package mancala.view;

import javax.swing.*;
import java.awt.*;

public class MancalaFrame extends JFrame {
    private static final String STYLE_SELECT_CARD = "STYLE_SELECT";
    private static final String GAME_CARD = "GAME";
    
    private CardLayout cardLayout;
    private StyleSelectPanel styleSelectPanel;
    private JPanel gameScreen;
    private BoardPanel boardPanel;
    private ControlPanel controlPanel;
    
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
    
    public void showStyleSelect() {
        cardLayout.show(getContentPane(), STYLE_SELECT_CARD);
    }
    
    public void showGame() {
        cardLayout.show(getContentPane(), GAME_CARD);
    }
    
    public StyleSelectPanel getStyleSelectPanel() {
        return styleSelectPanel;
    }
    
    public BoardPanel getBoardPanel() {
        return boardPanel;
    }
    public ControlPanel getControlPanel() {
        return controlPanel;
    }
}
