package events;

import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.JsonNode;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.*;
import utils.BasicObjectBuilders;

/**
 * Indicates that both the core game loop in the browser is starting, meaning
 * that it is ready to recieve commands from the back-end.
 * <p>
 * {
 * messageType = “initalize”
 * }
 *
 * @author Dr. Richard McCreadie
 */
public class Initalize implements EventProcessor {

    @Override
    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
        initializeGameGrid();
        createPlayers();

        // Set Current Player and set his mana
        gameState.setCurrentPlayer(gameState.getHumanPlayer());
        gameState.getCurrentPlayer().setMana(gameState.getTurn() + 1);

        gameState.setGameInitialised(true);

        communicateInitialState(out);
    }

    private void initializeGameGrid() {
        GameState gameState = GameState.getInstance();
        Grid grid = BasicObjectBuilders.loadGrid();
        gameState.setGrid(grid);
    }

    private void createPlayers() {

        GameState gameState = GameState.getInstance();
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
        System.out.println("humanplayer card: " + humanPlayer.getMyHandCards().get(0).getUnitConfig());
        System.out.println("AIplayer card: " + aiPlayer.getMyHandCards().get(0).getUnitConfig());

    }

    private void communicateInitialState(ActorRef out) {

        GameState gameState = GameState.getInstance();
        BasicCommands.drawGrid(out, gameState.getGrid());
        BasicCommands.drawUnitsAndSetAttributes(out);
        BasicCommands.drawHandCards(gameState.getHumanPlayer().getMyHandCards(), out, 0); // pass the mode
    }
}
