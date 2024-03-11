package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import gamelogic.ProcessEndTurnClicked;
import structures.GameState;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
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
    	
    	// Return 
    	// 1. if the end-turn being triggered during AI's turn
    	// 2. if game ended
        if (!gameState.isCurrentPlayerHuman() || gameState.isGameEnded()) {
            return;
        }
        
        // Set end-turn clicked 
        gameState.setEndTurnClicked(true);
        
        // Process events that have to triggered when the end turn button is clicked
        ProcessEndTurnClicked.processEndTurnClicked(out);
        
        // Reset end-turn in the gameState
        gameState.setEndTurnClicked(false);
    }

}
