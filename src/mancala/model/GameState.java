// Holds board data, current turn, and undo counters; supports snapshot/restore for undo. 
// Confirm final board shape implementation and what fields a snapshot must include.

package mancala.model;

public class GameState {
  public int[] board;
  public int currentTurn;
  public int currentPlayer;

  public GameState(int[] board, int currentTurn, int currentPlayer) {
    // Deep copy the board array to prevent external modification
    this.board = new int[board.length];
    System.arraycopy(board, 0, this.board, 0, board.length);
    this.currentTurn = currentTurn;
    this.currentPlayer = currentPlayer;
  }
  

  public int getCurrentTurn() {
    return currentTurn;
  }

  public int[] getBoard() {
    return board;
  }

  public int getCurrentPlayer() {
    return currentPlayer;
  }
}
