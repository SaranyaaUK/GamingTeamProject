package structures;

import structures.basic.Grid;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.CombatResult;
import utils.CombatResults;

import java.util.HashMap;
import java.util.Map;

/**
 * This class holds information about the ongoing game.
 * It's created with the GameActor.
 */
public class GameState {

	public boolean gameInitialized = false;
	public boolean something = false; // Example flag for additional state tracking
	private Grid grid; // Represents the game board
	private Player player1; // Human player
	private Player player2; // AI player
	private int turn; // Tracks the current game turn

	private Map<Unit,Tile> unitTileMap = new HashMap<>();

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

	/**
	 * Updates the association between a unit and a tile, handling occupied tiles and triggering events.
	 *
	 * @param unit The unit to associate with a tile.
	 * @param tile The tile to associate with the unit.
	 */
	public void updateUnitTileAssociation(Unit unit, Tile tile) {
		// Check if the target tile is already occupied by another unit.
		Tile currentTile = unitTileMap.get(unit);
		if (currentTile != null && currentTile.equals(tile)) {
			// The unit is already at the specified tile, so no action is needed.
			return;
		}

		Unit occupyingUnit = findUnitByTile(tile);
		if (occupyingUnit != null && !occupyingUnit.equals(unit)) {
			// The tile is occupied by another unit, handle according to your game's rules.
			handleOccupiedTile(unit, occupyingUnit, tile);
		} else {
			// If moving to a new tile, remove the unit from its current tile.
			if (currentTile != null) {
				currentTile.setUnit(null); // Assuming Tile.setUnit(null) clears the tile's unit.
			}

			// Place the unit on the new tile and update its position.
			tile.setUnit(unit);
			unit.setPositionByTile(tile);
			unitTileMap.put(unit, tile);

			// Trigger any events related to the tile.
			triggerTileEvents(unit, tile);
		}
	}

	private void triggerTileEvents(Unit unit, Tile tile) {
		// Implement logic for environmental effects, items, or other triggers.
		// Example: if (tile.hasTrap()) applyTrapEffect(unit);
	}

	/**
	 * Finds the unit occupying a specific tile.
	 *
	 * @param tile The tile to check for an occupying unit.
	 * @return The unit occupying the tile, or null if the tile is unoccupied.
	 */
	private Unit findUnitByTile(Tile tile) {
		return unitTileMap.entrySet().stream()
				.filter(entry -> entry.getValue().equals(tile))
				.map(Map.Entry::getKey)
				.findFirst()
				.orElse(null);
	}

	private void handleOccupiedTile(Unit movingUnit, Unit occupyingUnit, Tile tile) {
		CombatResult result = CombatResults.initiateCombat(movingUnit, occupyingUnit);

		switch (result) {
			case MOVING_UNIT_WINS:
				removeUnitFromGame(occupyingUnit); // Use GameState's method
				tile.setUnit(movingUnit);
				movingUnit.setPositionByTile(tile);
				unitTileMap.put(movingUnit, tile);
				break;
			case OCCUPYING_UNIT_WINS:
				removeUnitFromGame(movingUnit);
				break;
			case DRAW:
				CombatResults.handleCombatDraw(movingUnit, occupyingUnit); // Static method call
				break;
		}
	}


	public void removeUnitFromGame(Unit unit) {
		// Logic to remove the unit from the game
		Tile tile = unitTileMap.get(unit);
		if(tile != null) {
			tile.setUnit(null);
		}
		unitTileMap.remove(unit);
		// Update frontend if necessary
	}


	// Method to get a tile by unit, useful for various game logic scenarios
	public Tile getTileByUnit(Unit unit) {
		return unitTileMap.get(unit);
	}

}


