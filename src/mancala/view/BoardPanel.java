// Draws pits/mancalas/stones/labels and maps mouse clicks to pit indices. 
// Agree on hit-testing approach and which geometry/metrics come from the style.
package mancala.view;

import javax.swing.*;
import java.awt.*;
import mancala.style.BoardStyle;

public class BoardPanel extends JPanel {
    private BoardStyle style;
    private int[] boardState; // Board state from model (indices 0-13)
    
    public BoardPanel() {
        setPreferredSize(new Dimension(800, 400));
        setBackground(Color.LIGHT_GRAY);
        boardState = null; // No board state initially
    }
    
    public void setStyle(BoardStyle style) {
        this.style = style;
        if (style != null) {
            setBackground(style.getBackgroundColor());
        }
        repaint();
    }
    
    public BoardStyle getStyle() {
        return style;
    }
    
    /**
     * Sets the board state from the model.
     * This will be used for rendering pits and stones in Priority 5.
     * 
     * @param boardState Array of 14 integers representing the board state
     */
    public void setBoardState(int[] boardState) {
        this.boardState = boardState;
        repaint(); // Repaint to show updated state (will be used when rendering is implemented)
    }
    
    /**
     * Gets the current board state.
     * 
     * @return Array of board state, or null if not set
     */
    public int[] getBoardState() {
        return boardState;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw placeholder rectangle
        int width = getWidth();
        int height = getHeight();
        int rectX = width / 8;
        int rectY = height / 4;
        int rectWidth = width * 3 / 4;
        int rectHeight = height / 2;
        
        g2d.setColor(getBackground().darker());
        g2d.fillRoundRect(rectX, rectY, rectWidth, rectHeight, 20, 20);
        
        g2d.setColor(getForeground());
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(rectX, rectY, rectWidth, rectHeight, 20, 20);
        
        // Draw placeholder text
        g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "Board Area";
        int textX = (width - fm.stringWidth(text)) / 2;
        int textY = height / 2 + fm.getAscent() / 2;
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, textX, textY);
    }
}
