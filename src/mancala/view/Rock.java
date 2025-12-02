package mancala.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke; 
import javax.swing.JComponent;

public class Rock extends JComponent {
    private final double r;
    private final double g;
    private final double b;

    private final double dx;
    private final double dy;

    /**
     * Constructs the rock with the specific variation for positioning and coloring
     * 
     * @param dx The relative positional offset in the x-axis
     * @param dy The relative positional offset in the x-axis
     * @param r  The color variation in the red channel
     * @param g  The color variation in the green channel
     * @param b  The color variation in the blue channel
     */
    public Rock(double dx, double dy, double r, double g, double b) {
        this.dx = dx;
        this.dy = dy;

        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Draws the stone with variation of a color theme, and some variation with its
     * position.
     * 
     * @param g2d   Graphics context for drawing
     * @param x     X position of the pit (top-left corner)
     * @param y     Y position of the pit (top-left corner)
     * @param size  Diameter of the circular pit
     * @param color Base color for the outline and stone color mixing
     */
    public void draw(Graphics2D g2d, int x, int y, int size, Color color) {
        int MIXING_STRENGTH = 10;

        int r0 = color.getRed();
        int g0 = color.getGreen();
        int b0 = color.getBlue();

        int rMix = (int) (r * MIXING_STRENGTH + r0);
        int gMix = (int) (g * MIXING_STRENGTH + g0);
        int bMix = (int) (b * MIXING_STRENGTH + b0);

        rMix = Math.clamp(rMix, 0, 255);
        gMix = Math.clamp(gMix, 0, 255);
        bMix = Math.clamp(bMix, 0, 255);

        double x0 = (double) x + (double) size * dx;
        double y0 = (double) y + (double) size * dy;

        // Fill the stone with mixed color
        g2d.setColor(new Color(rMix, gMix, bMix));
        g2d.fillOval((int) x0, (int) y0, size, size);
        
        //Add a thin black outline
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));  // Thin outline
        g2d.drawOval((int) x0, (int) y0, size, size);
    }

    /**
     * Getter
     * 
     * @return Returns the relative x-offset
     */
    public double getDx() {
        return dx;
    }

    /**
     * Getter
     * 
     * @return Returns the relative y-offset
     */
    public double getDy() {
        return dy;
    }
}
