package mancala.view;

/**
 * Interface for handling pit click events from BoardPanel.
 * The controller should implement this to receive pit click notifications.
 */
public interface PitClickListener {
    /**
     * Called when a pit is clicked.
     * 
     * @param pitIndex The model index of the clicked pit (0-13)
     */
    void onPitClicked(int pitIndex);
}

