package structures.basic.creatures;

import java.util.List;

import akka.actor.ActorRef;
import gamelogic.Actions;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.TilesGenerator;

/*
 *  BloodmoonPriestess.java
 *  
 *  This class implements the Death interface, reacts when a unit dies on the board
 *  
 */

public class BloodmoonPriestess extends Unit implements DeathWatch {

	/*
	 *  Reacts to death of any unit on the board
	 */
	@Override
	public void reactToDeath(ActorRef out) {
		// Summon a wraithling to a randomly selected unoccupied adjacent tile, 
		// if there are no unoccupied tiles, then this ability has no effect
		List<Tile> adjacentTiles = TilesGenerator.getAdjacentTiles(this);

		for (Tile tile: adjacentTiles) {
			// Can add some randomness - can try later <TO DO>
			// For now just find the first available tile (unoccupied tile)
			if (tile.getUnit() == null) {
				Actions.placeWraithling(out, tile);
				break;
			}
		}
	}

}
