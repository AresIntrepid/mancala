// Strategy contract the view calls for colors, fonts, sizes, and drawing/geometry hooks. 
// Finalize which methods it must provide
package mancala.style;

import java.awt.Color;

public interface BoardStyle {
    /**
     * Returns the name of this style (e.g., "Wood", "Neon")
     */
    String getName();
    
    /**
     * Returns the background color for the board
     */
    Color getBackgroundColor();
    
    /**
     * Returns the primary color palette (for future use)
     */
    Color[] getPalette();
    
    /**
     * Returns board metrics (width, height) for layout calculations
     */
    int getBoardWidth();
    int getBoardHeight();
}
