// Public API: startGame(n), applyMove(pit, player), undo(), isGameOver(), and winner/state accessors. 
// Implement sowing/skip/extra-turn/capture/sweep and the undo limits after rules are finalized.
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

  public MancalaModel() {
    this(DEFAULT_PITS_AMOUNT, DEFAULT_STONE_PER_PIT);
  }

  public MancalaModel(int pitsPerSide, int stonesPerPit) {
    this.pitsPerSide = pitsPerSide;
    this.stonesPerPit = stonesPerPit;

    isGameOver = false;
    this.history = new Stack<>();
    this.listeners = new ArrayList<>();

    init();
  }

  public void startGame(int player) {
    currentPlayer = player;
  }

  public void undo() {
    if (!history.empty()) {
      GameState state = history.pop();
      currentMove = state.getCurrentTurn();
      board = state.getBoard();
      currentPlayer = state.getCurrentPlayer();
      notifyListeners();
    }
  }

  public boolean isGameOver() {
    return isGameOver;
  }

  public int getWinner() {
    return winner;
  }

  public void switchPlayer() {
    currentPlayer = 3 - currentPlayer;
  }

  public int getCurrentPlayer() {
    return currentPlayer;
  }

  public int[] getBoardState() {
    int[] arr = new int[board.length];
    System.arraycopy(board, 0, arr, 0, board.length);

    return arr;
  }

  public void addListener(ChangeListener listener) {
    listeners.add(listener);
  }

  public void setCurrentPlayer(int player) {
    currentPlayer = player;
  }

  /**
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

  public int getPlayerStore(int player) {
    if (player == 1) {
      return pitsPerSide;
    } else {
      return pitsPerSide * 2 + 1;
    }
  }

  public void notifyListeners() {
    for (ChangeListener listener : listeners) {
      listener.stateChanged(new ChangeEvent(this));
    }
  }

  public int getStonesAtPit(int idx) {
    return board[idx];
  }

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

  private int getOppositePit(int pit) {
    return pitsPerSide * 2 - pit;
  }

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

  private void init() {
    board = new int[pitsPerSide * 2 + 2];

    // Set stones to pits
    for (int i = 0; i < pitsPerSide; ++i) {
      board[i] = board[i + pitsPerSide + 1] = stonesPerPit;
    }

    // Player 1 and 2 mancala
    board[pitsPerSide] = board[pitsPerSide * 2 + 1] = 0;
  }

  private boolean isPitOwnedBy(int idx, int player) {
    int start = getPlayerPitStart(player);
    int end = getPlayerPitEnd(player);

    return start <= idx && idx <= end;
  }

  private int getPlayerPitStart(int player) {
    if (player == 1) {
      return 0;
    } else {
      return pitsPerSide + 1;
    }
  }

  private int getPlayerPitEnd(int player) {
    if (player == 1) {
      return pitsPerSide - 1;  // 5 (last regular pit for Player 1)
    } else {
    	return pitsPerSide * 2;  // 12 (last regular pit for Player 2)
    }
  }

  private void addToHistory() {
	    int[] currentBoard = getBoardState();
	    GameState state = new GameState(currentBoard, currentMove, currentPlayer);
	    history.push(state);
	}

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
