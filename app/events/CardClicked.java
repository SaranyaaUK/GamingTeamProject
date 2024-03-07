package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.GameLogic;
import structures.basic.spell.Spell;
import structures.basic.spell.WraithlingSwarm;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a card.
 * The event returns the position in the player's hand the card resides within.
 * 
 * { 
 *   messageType = “cardClicked”
 *   position = <hand index position [1-6]>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class CardClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		int handPosition = message.get("position").asInt();
		
		// If in process of summoning wrathlings do nothing else
		if (GameState.wraithlingSummonStatus()) {
			return;
		}
		// If they click the same card again and again treat as no-op 
		if (gameState.getHandPosition() == handPosition) {
			return;
		}

		// Update the handPosition in gameState
		gameState.setHandPosition(handPosition); // Remember to reset HandPosition after the card is used (should be done when we delete the card)
		// Do the highlight events following a card click
		GameLogic.highlightAfterCardClick(out);	
	}

}
