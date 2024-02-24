package events;

import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.JsonNode;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.*;
import utils.AppConstants;

/**
 * Indicates that both the core game loop in the browser is starting, meaning
 * that it is ready to recieve commands from the back-end.
 * 
 * { 
 *   messageType = “initalize”
 * }
 * 

 *
 */
public class Initalize implements EventProcessor {

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		initializeGameGrid(gameState);
		createPlayers(gameState);
		positionPlayersAvatars(gameState, out);
		communicateInitialState(out, gameState);
		gameState.setGameInitialized(true);
	}

	private void initializeGameGrid(GameState gameState) {
		Grid grid = new Grid(AppConstants.gridWidth, AppConstants.gridHeight);
		gameState.setGrid(grid);
	}

	private void createPlayers(GameState gameState) {
		Player humanPlayer = new Player(AppConstants.INITIAL_HEALTH, AppConstants.INITIAL_MANA);
		Player aiPlayer = new Player(AppConstants.INITIAL_HEALTH, AppConstants.INITIAL_MANA);
		gameState.setPlayer1(humanPlayer);
		gameState.setPlayer2(aiPlayer);
	}

	private void communicateInitialState(ActorRef out, GameState gameState) {
		BasicCommands.drawGrid(out, gameState.getGrid());
		drawUnitsAndSetAttributes(gameState, out);
		drawInitialHandCards(gameState, out);
	}
	private void positionPlayersAvatars(GameState gameState, ActorRef out) {
		// Assuming the Grid class has a method to retrieve a Tile by grid coordinates
		Tile humanAvatarTile = gameState.getGrid().getTile(2, 3);
		Tile aiAvatarTile = gameState.getGrid().getTile(8, 3);

		// Assuming the Player class has a method to get the Avatar (Unit) for the player
		Unit humanAvatar = gameState.getPlayer1().getAvatar();
		Unit aiAvatar = gameState.getPlayer2().getAvatar();

		// Assuming Position class handles both grid and pixel positions, but focusing on grid here
		// Update avatar positions based on grid coordinates; pixel positions are to be set elsewhere
		humanAvatar.setPosition(new Position(0, 0, 2, 3)); // Pixel positions set to 0, assuming they are handled elsewhere
		aiAvatar.setPosition(new Position(0, 0, 8, 3));

		// Logic to associate the avatars with their tiles should be handled here
		// This might involve updating the GameState or a separate manager to track which units are on which tiles
		// For simplicity, this is conceptual and needs to be implemented according to your game's architecture
		associateAvatarWithTile(humanAvatar, humanAvatarTile,gameState);
		associateAvatarWithTile(aiAvatar, aiAvatarTile,gameState);

		// Optionally, you can draw the avatars on the frontend using the ActorRef
		BasicCommands.drawUnit(out, humanAvatar, humanAvatarTile);
		BasicCommands.drawUnit(out, aiAvatar, aiAvatarTile);
	}

	/**
	 * Conceptual method to associate a Unit (avatar) with a Tile.
	 * Implement this logic according to your game's architecture.
	 */
	private void associateAvatarWithTile(Unit avatar, Tile tile,GameState gameState) {
		// Set the Unit on the Tile
		tile.setUnit(avatar);

		// Update the Unit's position to match the Tile's coordinates
		avatar.setPositionByTile(tile);

		// Update the global game state or any tracking mechanism you have for unit-tile associations
		// This assumes you have a gameState object available in this context
		gameState.updateUnitTileAssociation(avatar, tile);
	}

	private void drawUnitsAndSetAttributes(GameState gameState, ActorRef out) {
		// Assuming the game turn is stored in gameState and starts at 0 for the first turn
		int currentTurn = gameState.getTurn();

		// Draw units (avatars) for both players
		BasicCommands.drawUnit(out, gameState.getPlayer1().getAvatar(), gameState.getGrid().getTile(2, 3));
		BasicCommands.drawUnit(out, gameState.getPlayer2().getAvatar(), gameState.getGrid().getTile(8, 3));

		// Set the avatars' health to 20 for both players
		BasicCommands.setUnitHealth(out, gameState.getPlayer1().getAvatar(), 20);
		BasicCommands.setUnitHealth(out, gameState.getPlayer2().getAvatar(), 20);

		// Example: Set the avatars' attack, assuming a method or attribute exists to get this value
		// For demonstration, setting arbitrary attack values. Replace these with actual game logic.
		BasicCommands.setUnitAttack(out, gameState.getPlayer1().getAvatar(), 5); // Example attack value for Player 1's avatar
		BasicCommands.setUnitAttack(out, gameState.getPlayer2().getAvatar(), 5); // Example attack value for Player 2's avatar

		// Set players' health to 20 and mana to turn + 1
		// Assuming Player class has methods setHealth and setMana for setting health and mana
		gameState.getPlayer1().setHealth(20);
		gameState.getPlayer2().setHealth(20);
		gameState.getPlayer1().setMana(currentTurn + 1);
		gameState.getPlayer2().setMana(currentTurn + 1);

		// Communicate the updated health and mana values to the front-end
		BasicCommands.setPlayer1Health(out, gameState.getPlayer1());
		BasicCommands.setPlayer2Health(out, gameState.getPlayer2());
		BasicCommands.setPlayer1Mana(out, gameState.getPlayer1());
		BasicCommands.setPlayer2Mana(out, gameState.getPlayer2());
	}



	private void drawInitialHandCards(GameState gameState, ActorRef out) {
		// Assuming visualization mode 0 for the initial state of the cards
		int mode = 0; // Example visualization mode, adjust based on your game's logic

		int position = 1; // Start position from 1
		for (Card card : gameState.getPlayer1().getCardManager().getHandCards()) {
			if (position <= CardManager.MAX_HAND_CARDS) {
				BasicCommands.drawCard(out, card, position, mode);
				position++; // Increment position for next card
			}
		}

		// Reset position for player 2's hand cards if necessary
		position = 1;
		for (Card card : gameState.getPlayer2().getCardManager().getHandCards()) {
			if (position <= CardManager.MAX_HAND_CARDS) {
				BasicCommands.drawCard(out, card, position, mode);
				position++;
			}
		}
	}

}


