package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import gamelogic.ProcessEndTurnClicked;
import structures.GameState;
import structures.basic.GameLogic;

/**
 * Indicates that the user has clicked an object on the game canvas, in this
 * case
 * somewhere that is not on a card tile or the end-turn button.
 * 
 * {
 * messageType = “otherClicked”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class OtherClicked implements EventProcessor {

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		// Return
		// 1. if in process of summoning wraithlings for Wraithling swarm
		if (GameLogic.wraithlingSummonStatus()) {
			return;
		}

		// For testing - remove this (using the other click as a way for ai to return
		// the turn to the human player
		if (!gameState.isCurrentPlayerHuman()) {
			ProcessEndTurnClicked.processEndTurnClicked(out);
		}
	}

}
