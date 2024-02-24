package structures;
import structures.basic.Grid;
import structures.basic.Player;

/**
 * This class holds information about the ongoing game.
 * It's created with the GameActor.
 */
public class GameState {

	public boolean gameInitialized = false;
	public boolean something = false; // Example flag for additional state tracking
	private Grid grid; // Represents the game board
	private Player player1; // Human player
	private Player player2; // AI or second human player
	private int turn; // Tracks the current game turn

	// Constructors
	public GameState() {
		// Initialize default values
		this.gameInitialized = false;
		this.something = false;
		this.turn = 0; // Assuming the game starts with turn 0
	}

	// Getter and setter for gameInitialized
	public boolean isGameInitialized() {
		return gameInitialized;
	}

	public void setGameInitialized(boolean gameInitialized) {
		this.gameInitialized = gameInitialized;
	}

	// Getter and setter for something
	public boolean isSomething() {
		return something;
	}

	public void setSomething(boolean something) {
		this.something = something;
	}

	// Getter and setter for grid
	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	// Getter and setter for players
	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
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
		if (player1 != null) {
			player1.setMana(this.turn + 1); // Assuming mana is turn + 1
		}
		if (player2 != null) {
			player2.setMana(this.turn + 1);
		}
	}

}


