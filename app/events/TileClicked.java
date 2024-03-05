package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import gamelogic.ProcessTileClicked;
import structures.GameState;
import structures.basic.Tile;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a tile.
 * The event returns the x (horizontal) and y (vertical) indices of the tile that was
 * clicked. Tile indices start at 1.
 * 
 * { 
 *   messageType = “tileClicked”
 *   tilex = <x index of the tile>
 *   tiley = <y index of the tile>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class TileClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		int tilex = message.get("tilex").asInt();
		int tiley = message.get("tiley").asInt();

		if (gameState.getCurrentPlayer() != gameState.getHumanPlayer()) {
			return;
		}
		
		ProcessTileClicked.dispatchAfterTileClicked(out, gameState.getGrid().getTile(tilex, tiley));
//		if (gameState.something == true) {
//			// do some logic
//		}
		
	}

}
