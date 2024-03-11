package events;

import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.JsonNode;
import gamelogic.ProcessTileClicked;
import structures.GameState;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a tile.
 * The event returns the x (horizontal) and y (vertical) indices of the tile that was
 * clicked. Tile indices start at 1.
 * <p>
 * {
 * messageType = “tileClicked”
 * tilex = <x index of the tile>
 * tiley = <y index of the tile>
 * }
 *
 * @author Dr. Richard McCreadie
 */
public class TileClicked implements EventProcessor {

    @Override
    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

        int tilex = message.get("tilex").asInt();
        int tiley = message.get("tiley").asInt();

        // Return
        // 1. if the game ended or
        // 2. if tiles are clicked during AI's turn
        if (gameState.isGameEnded() || !gameState.isCurrentPlayerHuman()) {
            return;
        }

        // Process events that happen upon clicking a tile
        ProcessTileClicked.dispatchAfterTileClicked(out, gameState.getGrid().getTile(tilex, tiley));

    }

}
