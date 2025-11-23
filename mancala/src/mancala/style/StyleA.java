// Concrete styles implementing BoardStyle for two distinct looks. 
// Decide the two visual themes (names/palette/shapes) and implement once the interface is locked.
package mancala.style;

import java.awt.Color;

public class StyleA implements BoardStyle {
    private static final String NAME = "Wood";
    private static final Color BACKGROUND = new Color(139, 90, 43); // Brown wood color
    
    // Palette indices for future board rendering:
    private static final int PIT_COLOR_INDEX = 0;      // Color for pit backgrounds
    private static final int STONE_COLOR_INDEX = 1;     // Color for stones/marbles
    private static final int BORDER_COLOR_INDEX = 2;    // Color for borders/outlines
    
    private static final Color[] PALETTE = {
        new Color(139, 90, 43),  // [0] PIT_COLOR - Brown for pit backgrounds
        new Color(160, 82, 45),  // [1] STONE_COLOR - Sienna for stones/marbles
        new Color(101, 67, 33)   // [2] BORDER_COLOR - Dark brown for borders/outlines
    };
    
    @Override
    public String getName() {
        return NAME;
    }
    
    @Override
    public Color getBackgroundColor() {
        return BACKGROUND;
    }
    
    @Override
    public Color[] getPalette() {
        return PALETTE.clone();
    }
    
    // Convenience methods for accessing specific palette colors
    /**
     * Returns the color for pit backgrounds
     */
    public Color getPitColor() {
        return PALETTE[PIT_COLOR_INDEX];
    }
    
    /**
     * Returns the color for stones/marbles
     */
    public Color getStoneColor() {
        return PALETTE[STONE_COLOR_INDEX];
    }
    
    /**
     * Returns the color for borders and outlines
     */
    public Color getBorderColor() {
        return PALETTE[BORDER_COLOR_INDEX];
    }
    
    @Override
    public int getBoardWidth() {
        return 800; // Placeholder width
    }
    
    @Override
    public int getBoardHeight() {
        return 400; // Placeholder height
    }
}
