// Draws pits/mancalas/stones/labels and maps mouse clicks to pit indices. 
// Agree on hit-testing approach and which geometry/metrics come from the style.
package mancala.view;

import java.awt.*;
import javax.swing.*;
import mancala.style.BoardStyle;

public class BoardPanel extends JPanel {
    private BoardStyle style;
    private int pitsPerSide;
    private Pit[][] pits;

    public BoardPanel() {
        pitsPerSide = 6;
        initializePits();
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

        int padding = width / 64;
        int pitSpacing = width / 64;
        int xStart = rectX + padding;
        int yStart = rectY + padding;
        int pitWidth = (rectWidth - padding * 2 - pitSpacing * (pitsPerSide - 1)) / pitsPerSide;
        int pitHeight = (rectHeight - padding * 2 - pitSpacing) / 2;
        int pitSize = Math.min(pitWidth, pitHeight);


        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < pitsPerSide; ++j) {
                int x = xStart + j * (pitWidth + pitSpacing);
                int y = yStart + i * (pitHeight + pitSpacing);

                pits[i][j].draw(g2d, x, y, pitSize, style.getBackgroundColor());
            }   
        }

    }

    private void initializePits() {
        pits = new Pit[2][pitsPerSide];

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < pitsPerSide; ++j) {
                pits[i][j] = new Pit(4);
            }
        }
    }
}
