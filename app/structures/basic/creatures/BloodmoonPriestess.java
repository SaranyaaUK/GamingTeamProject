package structures.basic.creatures;

import java.util.List;

import akka.actor.ActorRef;
import gamelogic.Actions;
import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.TilesGenerator;

public class BloodmoonPriestess extends Unit implements DeathWatch {

	@Override
	public void reactToDeath(ActorRef out) {
		// Summon a wraithling to a randomly selected unoccupied adjacent tile, 
		// if there are no unoccupied tiles, then this ability has no effect
		List<Tile> adjacentTiles = TilesGenerator.getAdjacentTiles(this);

		for (Tile tile: adjacentTiles) {
			// Can add some randomness - can try later <TO DO>
			// For now just find the first available tile
			if (tile.getUnit() == null) {
				Actions.placeUnit(out, tile);
				break;
			}
		}
	}

}
