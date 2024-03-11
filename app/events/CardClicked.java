package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.GameLogic;

/**
 * Indicates that the user has clicked an object on the game canvas, in this
 * case a card.
 * The event returns the position in the player's hand the card resides within.
 * <p>
 * {
 * messageType = “cardClicked”
 * position = <hand index position [1-6]>
 * }
 *
 * @author Dr. Richard McCreadie
 */
public class CardClicked implements EventProcessor {

    @Override
    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
        if (gameState.getCurrentPlayer() != gameState.getHumanPlayer()) {
            return;
        }

        int handPosition = message.get("position").asInt();

        // Return
        // 1. if we are in process of summoning wraithlings, or
        // 2. if the game ended or
        // 3. if the same card is clicked multiple times
        // 4. if the cards are clicked during AI's turn
        if (GameLogic.wraithlingSummonStatus() || gameState.isGameEnded()
                || gameState.getHandPosition() == handPosition || !gameState.isCurrentPlayerHuman()) {
            return;
        }

        // Update the handPosition in gameState
        gameState.setHandPosition(handPosition);

        // Do the highlight events following a card click
        GameLogic.highlightAfterCardClick(out);
    }

}
