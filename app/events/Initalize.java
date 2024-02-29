package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.AIPlayer;
import structures.basic.GameLogic;
//import structures.basic.GameLogic;
import structures.basic.Grid;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;

/**
 * Indicates that both the core game loop in the browser is starting, meaning
 * that it is ready to recieve commands from the back-end.
 * 
 * { 
 *   messageType = “initalize”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Initalize implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		initializeGameGrid(gameState);
		createPlayers(gameState);
		
		// Set Current Player and set his mana
		gameState.setCurrentPlayer(gameState.getHumanPlayer());
		gameState.getCurrentPlayer().setMana(gameState.getTurn() + 1);

		communicateInitialState(out, gameState);
		gameState.setGameInitialised(true);
	}
	
	private void initializeGameGrid(GameState gameState) {
		Grid grid = BasicObjectBuilders.loadGrid();
		gameState.setGrid(grid);
	}

	private void createPlayers(GameState gameState) {
		Player humanPlayer = new Player();
		Player aiPlayer = new AIPlayer();
		Grid myGrid = gameState.getGrid();

		// Position the players in their start position
		Tile humanStartPositionTile = myGrid.getTile(2, 3);
		GameLogic.associateUnitWithTile(humanPlayer.getAvatar(), humanStartPositionTile);

		Tile AIStartPositionTile = myGrid.getTile(8, 3);
		GameLogic.associateUnitWithTile(aiPlayer.getAvatar(), AIStartPositionTile);

		// Update the handCards
		humanPlayer.getCardManager().drawCardFromDeck(3);
		aiPlayer.getCardManager().drawCardFromDeck(3);
		
		// Update the gameState
		gameState.setHumanPlayer(humanPlayer);
		gameState.setAIPlayer(aiPlayer);
	}

	private void communicateInitialState(ActorRef out, GameState gameState) {
		BasicCommands.drawGrid(out, gameState.getGrid());
		BasicCommands.drawUnitsAndSetAttributes(gameState, out);
		BasicCommands.drawHandCards(gameState.getHumanPlayer().getMyHandCards(), out);
	}
}
