// Draws pits/mancalas/stones/labels and maps mouse clicks to pit indices. 
// Agree on hit-testing approach and which geometry/metrics come from the style.
package mancala.view;

import javax.swing.*;
import java.awt.*;
import mancala.style.BoardStyle;

public class BoardPanel extends JPanel {
    private BoardStyle style;
    
    public BoardPanel() {
        setPreferredSize(new Dimension(800, 400));
        setBackground(Color.LIGHT_GRAY);
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
