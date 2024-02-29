package utils;

import java.util.ArrayList;
import java.util.List;

import structures.GameState;
import structures.basic.Grid;
import structures.basic.Player;
import structures.basic.Position;
import structures.basic.Tile;
import structures.basic.Unit;

/*
 *  TilesGenerator - holds logic to generate valid tiles for highlighting 
 *  for summoning, spell casting, moving and attacking.
 *  
 *  Can be used for AI to get the tiles for AI Logic
 */
public class TilesGenerator {

	/*
	 *  Gives a list of tiles containing current player's 
	 *  enemy units
	 */
	public static List<Tile> getEnemyUnitTiles() {
		
//		If you need gameState uncomment below line
//		GameState gameState = GameState.getInstance();
		
		return null;
	}

	/*
	 *  Gives a list of tiles containing current player's 
	 *  friendly/allied units
	 */
	public static List<Tile> getFriendlyUnitTiles() {
		
//		If you need gameState uncomment below line
//		GameState gameState = GameState.getInstance();

		return null;

	}

	/*
	 *  Get the avatar's tile
	 *  
	 */
	public static List<Tile> getAvatarTile(Unit myAvatar){

		List<Tile> avatarTile = new ArrayList<Tile>();

		// Get the avatar's tile information
		Tile tile = getUnitTile(myAvatar);
		avatarTile.add(tile);

		return avatarTile;
	}
	
	/*
	 * 	Get the unit's tile object
	 *  @return Tile object
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

	/*
	 *  Gives a list of tiles to summon the unit corresponding to the creature card
	 *  
	 */
	public static List<Tile> getTilesToSummon() {

		GameState gameState = GameState.getInstance();
		Player currentPlayer = gameState.getCurrentPlayer();
		List<Unit> playerUnits = currentPlayer.getMyUnits();
		
		// List of tiles to highlight
		List<Tile> unitTiles = new ArrayList<Tile>();
		
		// Find the unit's adjacent positions
		for (int i= 0; i < playerUnits.size(); i++) {
			Position unitPosition = playerUnits.get(i).getPosition(); // Get the unit's position
			int tilex = unitPosition.getTilex();
			int tiley = unitPosition.getTiley();

			Grid myGrid = gameState.getGrid();
			for (int j = -1; j <= 1; j++) {
				for (int k = -1; k <= 1; k++) {
					if (tilex + i > 9 || tiley + j > 5 || tilex + i < 1 || tiley + j < 1) {
						continue;
					}
					Tile tile = myGrid.getTile(tilex + j, tiley + k);
					unitTiles.add(tile);
				}
			}
		}

		// Remove Tiles with units in it
		// This is a workaround, the below two lines should be uncommented after its implementation is done and we 
		// can get rid of this workaround.
		unitTiles.removeAll(getAvatarTile(gameState.getHumanPlayer().getAvatar()));
//		unitTiles.removeAll(getFriendlyUnitTiles());
//		unitTiles.removeAll(getEnemyUnitTiles());

		return unitTiles;
	}

	

}
