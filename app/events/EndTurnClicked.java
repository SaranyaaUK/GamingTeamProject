package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import gamelogic.ProcessEndTurnClicked;
import structures.GameState;

/**
 * Indicates that the user has clicked an object on the game canvas, in this
 * case
 * the end-turn button.
 * <p>
 * {
 * messageType = “endTurnClicked”
 * }
 *
 * @author Dr. Richard McCreadie
 */
public class EndTurnClicked implements EventProcessor {

    @Override
    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

        if (gameState.getCurrentPlayer() != gameState.getHumanPlayer()) {
            return;
        }

        ProcessEndTurnClicked.processEndTurnClicked(out);
    }

}
