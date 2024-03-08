package structures.basic.creatures;

import akka.actor.ActorRef;
import gamelogic.Actions;
import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.TilesGenerator;

/*
 *  GloomChaser.java
 *  
 *  This class implements the OpeningGambit interface, triggers an effect when this unit
 *  is summoned on to the board.
 *  
 */

public class GloomChaser extends Unit implements OpeningGambit {

	/*
	 *  Effects to be triggered when this unit is summoned
	 */
	@Override
	public void reactToUnitsSummon(ActorRef out, Tile tile) {
		
		GameState gameState = GameState.getInstance();
		// Should summon wraithlings to a tile behind this unit if that tile is empty
		// If the tile is occupied do nothing.
		int tilex = tile.getTilex();
		int tiley = tile.getTiley();
		int behind;

		Tile toSummonTile = null;
		if (gameState.isCurrentPlayerHuman()) {
			behind = tilex - 1;
			
		} else {
			behind = tilex + 1;
		}
		
		// If the tile is not valid return
		if (!TilesGenerator.isValidTile(behind, tiley)) {
			return;
		} else {
			// If valid and occupied, return
			toSummonTile = gameState.getGrid().getTile(behind, tiley);
			if(toSummonTile.getUnit() != null) {
				return;
			}
		}
		
		// Summon wraithlings to the tile
		// Create Unit and associate it with the tile
		Actions.placeWraithling(out, toSummonTile);

	}

}
