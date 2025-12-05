// Holds board data, current turn, and undo counters; supports snapshot/restore for undo. 
// Confirm final board shape implementation and what fields a snapshot must include.

/**
 * This is the GameState class. 
 * This is used to store the game state at some specific point in time. 
 * This allows the player to undo to a previous state.
 * 
 * <p>Responsibilities:
 * <ul>
 *   <li>Holds all of the game state data</li>
 * </ul>
 * 
 * @author CS151 Group Project
 * @version 1.0
 */

package mancala.model;

public class GameState {
  public int[] board;
  public int currentTurn;
  public int currentPlayer;

  /**
   * Initializes the game state
   * 
   * @param board         Board data
   * @param currentTurn   The current turn
   * @param currentPlayer The current player
   */
  public GameState(int[] board, int currentTurn, int currentPlayer) {
    // Deep copy the board array to prevent external modification
    this.board = new int[board.length];
    System.arraycopy(board, 0, this.board, 0, board.length);
    this.currentTurn = currentTurn;
    this.currentPlayer = currentPlayer;
  }

  /**
   * Getter
   * 
   * @return Returns the current turn
   */
  public int getCurrentTurn() {
    return currentTurn;
  }

  /**
   * Getter
   * 
   * @return Return the board state
   */
  public int[] getBoard() {
    return board;
  }

  /**
   * Getter
   * 
   * @return Returns the current player
   */
  public int getCurrentPlayer() {
    return currentPlayer;
  }
}
