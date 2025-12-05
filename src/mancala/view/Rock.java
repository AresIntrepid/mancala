package mancala.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke; 
import javax.swing.JComponent;

/**
 * Represents a single stone/rock in a pit.
 * Each rock has random color and position variations for visual variety.
 * 
 * @author CS151 Group Project
 * @version 1.0
 */
public class Rock extends JComponent {
    private final double r;
    private final double g;
    private final double b;

    private final double dx;
    private final double dy;

    /**
     * Constructs a rock with specific position and color variations.
     * 
     * @param dx The relative positional offset in the x-axis
     * @param dy The relative positional offset in the y-axis
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
     * Draws the stone with color and position variations.
     * Color is mixed with the base color, and position is offset from center.
     * 
     * @param g2d   Graphics context for drawing
     * @param x     X position of the pit center (top-left corner)
     * @param y     Y position of the pit center (top-left corner)
     * @param size  Diameter of the stone
     * @param color Base color for color mixing
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
     * Gets the relative x-axis position offset.
     * 
     * @return The relative x-offset
     */
    public double getDx() {
        return dx;
    }

    /**
     * Gets the relative y-axis position offset.
     * 
     * @return The relative y-offset
     */
    public double getDy() {
        return dy;
    }
}
