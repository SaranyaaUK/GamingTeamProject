package structures;

import structures.basic.Grid;
import structures.basic.Player;

public class GameState {
	public static final int INITIAL_HEALTH = 20;
	public static final int INITIAL_MANA = 0;
	public static final int INITIAL_ATTACK = 2;
	
	public boolean gameInitialised = false;
	public boolean something = false;

	private Grid grid; // Represents the game grid (Tiles on the board)
	private Player humanPlayer; // Human player
	private Player AIPlayer; // AI player
	private Player currentPlayer; // CurrentPlayer
	private int turn; // Tracks the current game turn

	// Constructors
	public GameState() {
		// Initialize default values
		this.gameInitialised = false;
		this.turn = 1; // Assuming the game starts with turn 0
	}

	// Getter and setter for gameInitialized
	public boolean isGameInitialised() {
		return gameInitialised;
	}

	public void setGameInitialised(boolean gameInitialized) {
		this.gameInitialised = gameInitialised;
	}

	// Getter and setter for grid
	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	// Getter and setter for players
	public void setHumanPlayer(Player humanPlayer) {
		this.humanPlayer = humanPlayer;
	}

	public Player getHumanPlayer() {
		return humanPlayer;
	}

	public void setAIPlayer(Player AIPlayer) {
		this.AIPlayer = AIPlayer;
	}

	public Player getAIPlayer() {
		return AIPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	// Getter and setter for turn
	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	// Method to increment the turn
	public void nextTurn() {
		this.turn++;
		// Additional logic for turn transition can be added here
		// For example, updating player mana based on the new turn
		updatePlayersMana();
	}

	// method to update players' mana at the start of each turn
	private void updatePlayersMana() {
		if (humanPlayer != null) {
			humanPlayer.setMana(this.turn + 1); // Assuming mana is turn + 1
		}
		if (AIPlayer != null) {
			AIPlayer.setMana(this.turn + 1);
		}
	}
	
	
}