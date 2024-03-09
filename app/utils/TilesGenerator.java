package utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import structures.GameState;
import structures.basic.*;
import structures.basic.creatures.Provoke;
import structures.basic.creatures.Flying;

/**
 *  TilesGenerator - holds logic to generate valid tiles for highlighting
 *  for summoning, spell casting, moving and attacking.
 *
 *  Can be used by AI to get the tiles for AI Logic
 */

public class TilesGenerator {

	/**
	 *  Gives a list of tiles containing current player's
	 *  enemy units
	 *  
	 *  @return List<Tile> List of enemy unit tiles
	 */
	public static List<Tile> getEnemyUnitTiles() {

		GameState gameState = GameState.getInstance();

		List<Tile> enemyTiles = new ArrayList<Tile>();
		if (gameState.isCurrentPlayerHuman()) {
			for (Unit unit: gameState.getAIPlayer().getMyUnits()) {
				enemyTiles.add(getUnitTile(unit));
			}
		} else {
			for (Unit unit: gameState.getHumanPlayer().getMyUnits()) {
				enemyTiles.add(getUnitTile(unit));
			}
		}
		return enemyTiles;
	}

	/**
	 *  Gives a list of tiles containing current player's
	 *  friendly/allied units
	 *  
	 *  @return List<Tile> List of allied unit tiles
	 */
	public static List<Tile> getFriendlyUnitTiles() {

		GameState gameState = GameState.getInstance();

		List<Tile> alliedTiles = new ArrayList<Tile>();
		if (gameState.isCurrentPlayerHuman()) {
			for (Unit unit: gameState.getHumanPlayer().getMyUnits()) {
				alliedTiles.add(getUnitTile(unit));
			}
		} else {
			for (Unit unit: gameState.getAIPlayer().getMyUnits()) {
				alliedTiles.add(getUnitTile(unit));
			}
		}
		return alliedTiles;
	}

	/**
	 *  Get the avatar's tile
	 *  
	 *  @param avatar (Unit)
	 *  @return List<Tile> avatar's tile
	 *
	 */
	public static List<Tile> getAvatarTile(Unit avatar) {

		List<Tile> avatarTile = new ArrayList<Tile>();

		// Get the avatar's tile information
		Tile tile = getUnitTile(avatar);
		avatarTile.add(tile);

		return avatarTile;
	}

	/**
	 * 	Get the unit's tile object
	 * 
	 *  @param myunit (Unit)
	 *  @return Tile returns the unit's tile object
	 *
	 */
	public static Tile getUnitTile(Unit myUnit) {

		GameState gameState = GameState.getInstance();
		Position unitPosition = myUnit.getPosition();
		int tilex = unitPosition.getTilex();
		int tiley = unitPosition.getTiley();

		// Get the unit's tile information
		return gameState.getGrid().getTile(tilex, tiley);
	}

	/**
	 *  Gives a list of tiles to summon the unit 
	 *  corresponding to the creature card
	 *  
	 *  @return List<Tile> List tiles for summoning units
	 *
	 */
	public static List<Tile> getTilesToSummon() {

		GameState gameState = GameState.getInstance();
		Player currentPlayer = gameState.getCurrentPlayer();
		List<Unit> playerUnits = currentPlayer.getMyUnits();

		// List of tiles to highlight
		List<Tile> unitTiles = new ArrayList<Tile>();

		// Find the unit's adjacent positions
		for (int i = 0; i < playerUnits.size(); i++) {
			unitTiles.addAll(getAdjacentTiles(playerUnits.get(i)));
		}
		// Remove Tiles with units in it
		unitTiles.removeAll(getFriendlyUnitTiles());
		unitTiles.removeAll(getEnemyUnitTiles());

		// Remove duplicates if any
		Set<Tile> set = new HashSet<Tile>(unitTiles);
		unitTiles.clear(); // Clear the original list
		unitTiles.addAll(set);

		return unitTiles;
	}

	/**
	 *  Gives a list of tiles to move
	 *  
	 *  @param myunit (Unit)
	 *  @return List<Tile> List possible tile to move
	 *
	 */
	public static List<Tile> getMovableTiles(Unit unit) {
		GameState gameState = GameState.getInstance();
		Grid myGrid = gameState.getGrid();

		Position unitPosition = unit.getPosition(); // Get the unit's position
		int tilex = unitPosition.getTilex();
		int tiley = unitPosition.getTiley();

		List<Tile> unitTiles = getAdjacentTiles(unit);

		// If front, behind and above and below tile are free I highlight the 
		// second cardinal tile
		int front, behind, above, below;
		above = tiley-1;
		below = tiley+1;
		if(gameState.isCurrentPlayerHuman()) {
			front = tilex+1;
			behind = tilex-1;    	
		} else {
			front = tilex-1;
			behind = tilex+1;
		}

		// Second Cardinal Tile addition logic
		// Front and behind validity
		if(TilesGenerator.isValidTile(front, tiley)) {
			int newX;
			if(gameState.isCurrentPlayerHuman()) {
				newX = front + 1;
			} else {
				newX = front - 1;
			}
			if (TilesGenerator.isValidTile(newX, tiley) && ((myGrid.getTile(front, tiley).getUnit() == null) ||
					(myGrid.getTile(front, tiley).getUnit() != null &&
					!GameLogic.isEnemyUnit(myGrid.getTile(front, tiley).getUnit())))) {
				unitTiles.add(myGrid.getTile(newX, tiley));
			}
		}
		if(TilesGenerator.isValidTile(behind, tiley)) {
			int newX;
			if(gameState.isCurrentPlayerHuman()) {
				newX = behind - 1;
			} else {
				newX = behind + 1;
			}
			if (TilesGenerator.isValidTile(newX, tiley) && ((myGrid.getTile(behind, tiley).getUnit() == null) ||
					(myGrid.getTile(behind, tiley).getUnit() != null &&
					!GameLogic.isEnemyUnit(myGrid.getTile(behind, tiley).getUnit())))) {
				unitTiles.add(myGrid.getTile(newX, tiley));
			}
		}
		// Above and below validity
		if(TilesGenerator.isValidTile(tilex, above)) {
			int newY = above-1;
			if (TilesGenerator.isValidTile(tilex, newY) && ((myGrid.getTile(tilex, above).getUnit() == null) ||
					(myGrid.getTile(tilex, above).getUnit() != null &&
					!GameLogic.isEnemyUnit(myGrid.getTile(tilex, above).getUnit())))) {
				unitTiles.add(myGrid.getTile(tilex, newY));
			}
		}
		if(TilesGenerator.isValidTile(tilex, below)) {
			int newY = below + 1;
			if (TilesGenerator.isValidTile(tilex, newY) && ((myGrid.getTile(tilex, below).getUnit() == null) ||
					(myGrid.getTile(tilex, below).getUnit() != null &&
					!GameLogic.isEnemyUnit(myGrid.getTile(tilex, below).getUnit())))) {
				unitTiles.add(myGrid.getTile(tilex, newY));
			}
		}

		// Check if diagonal tile needs to be removed
		// front and above || front and below occupied
		unitTiles = removeDiagonalTiles(unitTiles, tilex, tiley, above, below, front, behind);

		// Remove any tiles if it already occupied
		unitTiles.removeAll(getFriendlyUnitTiles());
		unitTiles.removeAll(getEnemyUnitTiles());

		return unitTiles;
	}

	/**
	 *  Remove diagonal tiles
	 *  
	 *  Logic to check if the diagonal tiles have to be removed from the 
	 *  movable tiles because of obstructions around the unit that has to move.
	 *  
	 *  @param unitTiles(List<Tile>)
	 *  @param tilex (int)
	 *  @param tiley (int)
	 *  @param above (int)
	 *  @param below (int)
	 *  @param front (int)
	 *  @param behind (int)
	 *  @return List<Tile> 
	 *  
	 */
	public static List<Tile> removeDiagonalTiles(List<Tile> unitTiles, int tilex, int tiley, int above, int below, int front, int behind) {
		GameState gameState = GameState.getInstance();
		Grid myGrid = gameState.getGrid();

		// Logic to check if the diagonal tiles have to be included
		if((TilesGenerator.isValidTile(front, tiley) && (myGrid.getTile(front, tiley).getUnit() != null &&
				GameLogic.isEnemyUnit(myGrid.getTile(front, tiley).getUnit())))
				&& (TilesGenerator.isValidTile(tilex, above) && (myGrid.getTile(tilex, above).getUnit() != null &&
				GameLogic.isEnemyUnit(myGrid.getTile(tilex, above).getUnit())))) {
			unitTiles.remove(myGrid.getTile(front, above));
		}

		if((TilesGenerator.isValidTile(front, tiley) && (myGrid.getTile(front, tiley).getUnit() != null &&
				GameLogic.isEnemyUnit(myGrid.getTile(front, tiley).getUnit())))
				&& (TilesGenerator.isValidTile(tilex, below) && (myGrid.getTile(tilex, below).getUnit() != null &&
				GameLogic.isEnemyUnit(myGrid.getTile(tilex, below).getUnit())))) {
			unitTiles.remove(myGrid.getTile(front, below));
		}

		if((TilesGenerator.isValidTile(behind, tiley) && (myGrid.getTile(behind, tiley).getUnit() != null &&
				GameLogic.isEnemyUnit(myGrid.getTile(behind, tiley).getUnit())))
				&& (TilesGenerator.isValidTile(tilex, above) && (myGrid.getTile(tilex, above).getUnit() != null &&
				GameLogic.isEnemyUnit(myGrid.getTile(tilex, above).getUnit())))) {
			unitTiles.remove(myGrid.getTile(behind, above));
		}

		if((TilesGenerator.isValidTile(behind, tiley) && (myGrid.getTile(behind, tiley).getUnit() != null &&
				GameLogic.isEnemyUnit(myGrid.getTile(behind, tiley).getUnit())))
				&& (TilesGenerator.isValidTile(tilex, below) && (myGrid.getTile(tilex, below).getUnit() != null &&
				GameLogic.isEnemyUnit(myGrid.getTile(tilex, below).getUnit())))) {
			unitTiles.remove(myGrid.getTile(behind, below));
		}

		return unitTiles;
	}

	/**
	 *  Logic to check a tile's validity
	 *  
	 *  @param tilex (int)
	 *  @param tiley (int)
	 *  @return true (boolean) if the tile is valid
	 *  
	 */
	public static boolean isValidTile(int tilex, int tiley) {
		GameState gameState = GameState.getInstance();

		Grid myGrid = gameState.getGrid();
		int x = myGrid.getGridXSize();
		int y = myGrid.getGridYSize();

		if (tilex > x || tiley > y || tilex < 1 || tiley < 1) {
			return false;
		}
		return true;
	}

	/**
	 *  getAdjacentTiles
	 *  
	 *  @parma unit (Unit)
	 *  @return List<Tile>
	 *  
	 */
	public static List<Tile> getAdjacentTiles(Unit unit) {
		// Get the unit's tile
		Tile tile = TilesGenerator.getUnitTile(unit);

		// Return the tiles adjacent to the given unit
		return TilesGenerator.getAdjacentTiles(tile);
	}

	/**
	 *  getAdjacentTiles
	 *  
	 *  @param myTile (Tile)
	 *  @return List<Tile>
	 *  
	 */
	public static List<Tile> getAdjacentTiles(Tile myTile) {	
		// List of tiles to highlight
		List<Tile> unitTiles = new ArrayList<Tile>();

		GameState gameState = GameState.getInstance();
		int tilex = myTile.getTilex();
		int tiley = myTile.getTiley();

		Grid myGrid = gameState.getGrid();
		for (int j = -1; j <= 1; j++) {
			for (int k = -1; k <= 1; k++) {
				if (!isValidTile(tilex, tiley) && !(j == 0 && k == 0)) {
					continue;
				}
				Tile tile = myGrid.getTile(tilex + j, tiley + k);
				if (tile == null) {
					continue;
				}
				unitTiles.add(tile);
			}
		}

		return unitTiles;
	}


	/**
	 *  getAttackableTiles
	 *  
	 *  @param unit (Unit)
	 *  @return List<Tile>
	 * 
	 * 	if the tile is already moved, only get enemy tiles within attack distance
	 * 	if the tile has not moved, get enemy tiles within movable distance
	 * 	if the unit is already moved, only get enemy unit within attack distance
	 * 	if the unit has not moved, get enemy unit within movable distance
	 * 	check if there is provoke unit around
	 * 	check if is YoungFlamewing
	 * 
	 */

	public static List<Tile> getAttackableTiles(Unit unit) {
		List<Tile> attackableTiles = new ArrayList<Tile>();
		if (unit.isMoved()) {
			if (hasProvokeUnitAround(unit)) {
				if(!getProvokeUnitSurroundingTiles(unit).isEmpty()) {
					attackableTiles.addAll(getProvokeUnitSurroundingTiles(unit));
				}
			} else {
				if(!getEnemiesWithinAttackDistance(unit).isEmpty()) {
					attackableTiles.addAll(getEnemiesWithinAttackDistance(unit));
				}
			}
		} else {
			if (unit instanceof Flying) {
				if (hasProvokeUnitAround(unit)) {
					if(!getProvokeUnitSurroundingTiles(unit).isEmpty()) {
						attackableTiles.addAll(getProvokeUnitSurroundingTiles(unit));
					}
				} else {
					if(!getYoungFlamewingAttackableTiles(unit).isEmpty()) {
						attackableTiles.addAll(getYoungFlamewingAttackableTiles(unit));
					}
				}
			} else {
				if (hasProvokeUnitAround(unit)) {
					if(!getProvokeUnitSurroundingTiles(unit).isEmpty()) {
						attackableTiles.addAll(getProvokeUnitSurroundingTiles(unit));
					}
				} else {
					if(!getEnemiesWithinMovableDistance(unit).isEmpty()) {
						attackableTiles.addAll(getEnemiesWithinMovableDistance(unit));
					}
				}
			}
		}
		return attackableTiles;
	}

	/**
	 *  getEnemiesWithinAttackDistance
	 *  
	 *  @param unit (Unit)
	 *  @return List<Tile>
	 * 
	 */
	public static List<Tile> getEnemiesWithinAttackDistance(Unit unit) {
		return getAdjacentEnemyTiles(unit);
	}

	/**
	 *  getEnemiesWithinMovableDistance
	 *  
	 *  @param unit (Unit)
	 *  @return List<Tile>
	 * 
	 */
	public static Set<Tile> getEnemiesWithinMovableDistance(Unit unit) {
		Set<Tile> withinMovableEnemyTiles = new HashSet<Tile>();

		List<Tile> moveableTiles = TilesGenerator.getMovableTiles(unit);
		for (Tile adjacentTile: moveableTiles) {
			List<Tile> enemyInToMoveAdjacentTiles = getAdjacentEnemyTiles(adjacentTile);
			for (Tile newTile: enemyInToMoveAdjacentTiles) {
				withinMovableEnemyTiles.add(newTile);              
			}
		}
		return withinMovableEnemyTiles;
	}

	/**
	 *  getProvokeUnitSurroundingTiles
	 *  
	 *  @param unit (Unit)
	 *  @return List<Tile>
	 * 
	 */
	public static List<Tile> getProvokeUnitSurroundingTiles(Unit myUnit) {
		List<Tile> tilesWithProvoke = new ArrayList<>();
		List<Tile> adjacentEnemyTiles = getEnemiesWithinAttackDistance(myUnit);
		for (Tile adjacentTile : adjacentEnemyTiles) {
			Unit unit = adjacentTile.getUnit();
			if (unit != null && unit instanceof Provoke) {
				tilesWithProvoke.add(adjacentTile); // add provoke only to the attackable tile list
			}
		}
		return tilesWithProvoke;
	}

	/**
	 *  getYoungFlamewingAttackableTiles
	 *  
	 *  @param unit (Unit)
	 *  @return List<Tile>
	 * 
	 */
	public static List<Tile> getYoungFlamewingAttackableTiles(Unit unit) {
		List<Tile> yfwAttackableTiles = new ArrayList<>();
		// scan all tiles in the board and store in List
		Tile[][] allTiles = GameState.getInstance().getGrid().getBoardTiles();
		List<Tile> allTileList = new ArrayList<>();
		for (Tile[] row : allTiles) {
			for (Tile tiles : row) {
				allTileList.add(tiles);
			}
		}
		// scan all tiles that has enemy unit but surrounding with unoccupied tile
		for (Tile currentTile : allTileList) {
			// check is has enemy unit
			if (currentTile.getUnit() != null && currentTile.getUnit().isHumanUnit() != unit.isHumanUnit()) {
				List<Tile> surroundingTiles = getAdjacentTiles(currentTile.getUnit());
				// check if surrounding is unoccupied
				for (Tile surroundingTile : surroundingTiles) {
					if (surroundingTile.getUnit() == null) {
						// add to the young flamewing attackable list
						yfwAttackableTiles.add(currentTile);
						break;
					}
				}
			}
		}

		return yfwAttackableTiles;
	}

	/**
	 *  hasProvokeUnitAround
	 *  
	 *  @param unit (Unit)
	 *  @return boolean, true if there is provoke unit around the given unit
	 * 
	 */
	public static boolean hasProvokeUnitAround(Unit myUnit) {
		List<Tile> surroundingTiles = getAdjacentTiles(myUnit);
		for (Tile adjacentTile : surroundingTiles) {
			Unit unit = adjacentTile.getUnit();
			if (unit != null && unit.isHumanUnit() != myUnit.isHumanUnit() && unit instanceof Provoke) {
				return true;
			}
		}
		return false;
	}

	/**
	 *  getAdjacentEnemyTiles
	 *  
	 *  @param tile (Tile)
	 *  @return List<Tile>
	 * 
	 */
	public static List<Tile> getAdjacentEnemyTiles(Tile tile) {
		List<Tile> adjacentTiles = TilesGenerator.getAdjacentTiles(tile);
		List<Tile> enemyUnitsInAdjacentTiles = TilesGenerator.getEnemyUnitTiles();
		enemyUnitsInAdjacentTiles.retainAll(adjacentTiles);

		return enemyUnitsInAdjacentTiles;
	}

	/**
	 *  getAdjacentEnemyTiles
	 *  
	 *  @param unit (Unit)
	 *  @return List<Tile>
	 * 
	 */
	public static List<Tile> getAdjacentEnemyTiles(Unit unit) {
		List<Tile> adjacentTiles = TilesGenerator.getAdjacentTiles(unit);
		List<Tile> enemyUnitsInAdjacentTiles = TilesGenerator.getEnemyUnitTiles();
		enemyUnitsInAdjacentTiles.retainAll(adjacentTiles);

		return enemyUnitsInAdjacentTiles;
	}   

}
