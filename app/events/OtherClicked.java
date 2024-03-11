package events;

import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.JsonNode;
import structures.GameState;
import structures.basic.GameLogic;

/**
 * Indicates that the user has clicked an object on the game canvas, in this
 * case
 * somewhere that is not on a card tile or the end-turn button.
 * <p>
 * {
 * messageType = “otherClicked”
 * }
 *
 * @author Dr. Richard McCreadie
 */
public class OtherClicked implements EventProcessor {

    @Override
    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

        // Return
        // 1. if in process of summoning wraithlings for Wraithling swarm
        if (GameLogic.wraithlingSummonStatus()) {
            return;
        }

    }

}
