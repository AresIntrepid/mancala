package mancala.view;

import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.JComponent;

public class Rock extends JComponent {
    private double r;
    private double g;
    private double b;

    private double dx;
    private double dy;

    public Rock(double dx, double dy, double r, double g, double b) {
        this.dx = dx;
        this.dy = dy;
        
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void draw(Graphics2D g2d, int x, int y, int size, Color color) {
        int MIXING_STRENGTH = 10;

        int r0 = color.getRed();
        int g0 = color.getGreen();
        int b0 = color.getBlue();

        int rMix = (int)(r * MIXING_STRENGTH + r0);
        int gMix = (int)(g * MIXING_STRENGTH + g0);
        int bMix = (int)(b * MIXING_STRENGTH + b0);

        rMix = Math.clamp(rMix, 0, 255);
        gMix = Math.clamp(gMix, 0, 255);
        bMix = Math.clamp(bMix, 0, 255);

        double x0 = (double)x + (double)size * dx;
        double y0 = (double)y + (double)size * dy;

        g2d.setColor(new Color(rMix, gMix, bMix));
        g2d.fillOval((int)x0, (int)y0, size, size);
    }
}

