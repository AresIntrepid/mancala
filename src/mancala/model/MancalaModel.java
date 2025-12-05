// Public API: startGame(n), applyMove(pit, player), undo(), isGameOver(), and winner/state accessors. 
// Implement sowing/skip/extra-turn/capture/sweep and the undo limits after rules are finalized.
/**
 * Manages the data structures that is used to store the current game state.
 * Manages the move function by calculating how the rocks are distributed, captures, and extra turns.
 * Stores the in game history which is nesseary for the undo function
 * Manages the winner and how the game ends
 * 
 * <p>Responsibilities:
 * <ul>
 *   <li>Holds the current game state data</li>
 *   <li>Calculate the moves, stone distribution, captures, and extra turns</li>
 *   <li>Manages how the game ends</li>
 *   <li>Notifies the listeners for any changes</li>
 * </ul>
 * 
 * @author CS151 Group Project
 * @version 1.0
 */

package mancala.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MancalaModel {
  private int stonesPerPit;
  private int pitsPerSide;
  private List<ChangeListener> listeners;
  private Stack<GameState> history;
  private boolean isGameOver;

  private int[] board;
  private int winner = -1;
  private int currentMove = -1;
  private int currentPlayer = -1;

  private static final int DEFAULT_PITS_AMOUNT = 6;
  private static final int DEFAULT_STONE_PER_PIT = 4;

  /**
   * Initializes with the default parameters
   */
  public MancalaModel() {
    this(DEFAULT_PITS_AMOUNT, DEFAULT_STONE_PER_PIT);
  }

  /**
   * Initializes the data structures and the parameters
   * 
   * @param pitsPerSide  Number of pits each side has
   * @param stonesPerPit The initial amount of stones each pits has
   */
  public MancalaModel(int pitsPerSide, int stonesPerPit) {
    this.pitsPerSide = pitsPerSide;
    this.stonesPerPit = stonesPerPit;

    isGameOver = false;
    this.history = new Stack<>();
    this.listeners = new ArrayList<>();

    init();
  }

  /**
   * This start the game by initializing the starting player
   * 
   * @param player The starting player 1 for A, 2 for B
   */
  public void startGame(int player) {
    currentPlayer = player;
  }

  /**
   * This undo the game to the previous game state
   */
  public void undo() {
    if (!history.empty()) {
      GameState state = history.pop();
      currentMove = state.getCurrentTurn();
      board = state.getBoard();
      currentPlayer = state.getCurrentPlayer();
      notifyListeners();
    }
  }

  /**
   * Getter
   * 
   * @return Returns true if the game is over
   */
  public boolean isGameOver() {
    return isGameOver;
  }

  /**
   * Getter
   * 
   * @return This returns the winner of the game. -1 is there is no winner yet.
   */
  public int getWinner() {
    return winner;
  }

  /**
   * This switch the players
   */
  public void switchPlayer() {
    currentPlayer = 3 - currentPlayer;
  }

  /**
   * Getter
   * 
   * @return This get the current player making the moves
   */
  public int getCurrentPlayer() {
    return currentPlayer;
  }

  /**
   * Getter
   * 
   * @return This copies the board state and returns it
   */
  public int[] getBoardState() {
    int[] arr = new int[board.length];
    System.arraycopy(board, 0, arr, 0, board.length);

    return arr;
  }

  /**
   * This is where the listener is added
   * 
   * @param listener Listens for updates in the game states
   */
  public void addListener(ChangeListener listener) {
    listeners.add(listener);
  }

  /**
   * This set the current player
   * 
   * @param player The current player
   */
  public void setCurrentPlayer(int player) {
    currentPlayer = player;
  }

  /**
   * This function is where a move is made
   * 
   * @param pitIndex
   * @return true if there is an extra move to do
   */
  public boolean applyMove(int pitIndex) {
    // save state BEFORE making changes
    addToHistory();
    // Save the original selected pit for history
    currentMove = pitIndex;

    int stones = getStonesAtPit(pitIndex);
    board[pitIndex] = 0;

    int opponentStore = getPlayerStore(3 - currentPlayer);

    // Distribute stones and track final landing position
    int finalLandingPit = pitIndex;
    while (stones != 0) {
      finalLandingPit = (finalLandingPit + 1) % board.length;

      if (opponentStore != finalLandingPit) {
        board[finalLandingPit]++;
        stones--;
      }
    }

    // Check capture and extra turn based on final landing pit
    boolean extraMove = checkCaptureAndExtraTurn(finalLandingPit);

    if (checkGameEnd()) {
      endGame();
    } else if (!extraMove) {
      switchPlayer();
    }

    // Save state to history after move is complete
    notifyListeners();

    return extraMove;
  }

  /**
   * This get the mancala index for the specific player
   * 
   * @param player The specific player
   * @return Mancala index for the pit
   */
  public int getPlayerStore(int player) {
    if (player == 1) {
      return pitsPerSide;
    } else {
      return pitsPerSide * 2 + 1;
    }
  }

  /**
   * This notifies all of the listeners
   */
  public void notifyListeners() {
    for (ChangeListener listener : listeners) {
      listener.stateChanged(new ChangeEvent(this));
    }
  }

  /**
   * This get the amount of stones at a specific pit
   * 
   * @param idx The pit index
   * @return The amount of stones
   */
  public int getStonesAtPit(int idx) {
    return board[idx];
  }

  /**
   * This attempts to capture and test if there is an extra turn
   * 
   * @param currentPit The current pit that the last stone land on
   * @return True if there is an extra turn
   */
  private boolean checkCaptureAndExtraTurn(int currentPit) {
    if (currentPit == getPlayerStore(currentPlayer)) {
      return true;
    }

    if (isPitOwnedBy(currentPit, currentPlayer) && board[currentPit] == 1) {
      int opposite = getOppositePit(currentPit);
      if (board[opposite] > 0) {
        int captured = board[currentPit] + board[opposite];
        board[opposite] = 0;
        board[currentPit] = 0;
        board[getPlayerStore(currentPlayer)] += captured;
      }
    }

    return false;
  }

  /**
   * This get the opposing pit index
   * 
   * @param pit The current pit index
   * @return The opposing pit index
   */
  private int getOppositePit(int pit) {
    return pitsPerSide * 2 - pit;
  }

  /**
   * This checks if the game ended
   * 
   * @return True if the game ended
   */
  private boolean checkGameEnd() {
    boolean player1 = true, player2 = true;

    for (int i = getPlayerPitStart(1); i <= getPlayerPitEnd(1); ++i) {
      if (board[i] > 0) {
        player1 = false;
        break;
      }
    }

    for (int i = getPlayerPitStart(2); i <= getPlayerPitEnd(2); ++i) {
      if (board[i] > 0) {
        player2 = false;
        break;
      }
    }

    return player1 || player2;
  }

  /**
   * This initialize the board with the data structures
   */
  private void init() {
    board = new int[pitsPerSide * 2 + 2];

    // Set stones to pits
    for (int i = 0; i < pitsPerSide; ++i) {
      board[i] = board[i + pitsPerSide + 1] = stonesPerPit;
    }

    // Player 1 and 2 mancala
    board[pitsPerSide] = board[pitsPerSide * 2 + 1] = 0;
  }

  /**
   * This check the player owns a specific pit
   * 
   * @param idx    The pit index
   * @param player The specific player
   * @return True if the player owns it
   */
  private boolean isPitOwnedBy(int idx, int player) {
    int start = getPlayerPitStart(player);
    int end = getPlayerPitEnd(player);

    return start <= idx && idx <= end;
  }

  /**
   * This get the starting index for the row relative to the player
   * 
   * @param player The player id
   * @return The starting index for row
   */
  private int getPlayerPitStart(int player) {
    if (player == 1) {
      return 0;
    } else {
      return pitsPerSide + 1;
    }
  }

  /**
   * This gets the ending index for the row relative to the player
   * 
   * @param player The player id
   * @return The index for the last pit in the row for the specific player
   */
  private int getPlayerPitEnd(int player) {
    if (player == 1) {
      return pitsPerSide - 1; // 5 (last regular pit for Player 1)
    } else {
      return pitsPerSide * 2; // 12 (last regular pit for Player 2)
    }
  }

  /**
   * This copies the state of the board and push it onto the stack, saving its
   * history.
   */
  private void addToHistory() {
    int[] currentBoard = getBoardState();
    GameState state = new GameState(currentBoard, currentMove, currentPlayer);
    history.push(state);
  }

  /**
   * This ends the game.
   * Calculate the scores and checks for the winner.
   */
  private void endGame() {
    isGameOver = true;

    for (int player = 1; player <= 2; ++player) {
      int store = getPlayerStore(player);
      for (int idx = getPlayerPitStart(player); idx <= getPlayerPitEnd(player); ++idx) {
        board[store] += board[idx];
      }
    }

    int player1score = board[getPlayerStore(1)];
    int player2score = board[getPlayerStore(2)];

    if (player1score > player2score) {
      winner = 1;
    } else if (player1score < player2score) {
      winner = 2;
    } else {
      winner = 0;
    }
  }

  /**
   * Checks if there is any history available for undo.
   * 
   * @return true if history stack is not empty
   */
  public boolean hasHistory() {
    return !history.empty();
  }
}
