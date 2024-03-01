package gamelogic;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Tile;
import structures.basic.spell.Spell;
import utils.TilesGenerator;

import java.util.List;

public class ProcessTileClicked {
    static GameState gameState = GameState.getInstance();

    public static void dispatchAfterTileClicked(ActorRef out, Tile tile) {
        //BasicCommands.clearHighLight();
        if (!gameState.getHighlightedEnemyTiles().contains(tile) && !gameState.getHighlightedFriendlyTiles().contains(tile)) {
            Actions.resetWithinOneTurn(out);
            //If a de-highlighted tile is clicked and there is one human unit is within this tile
            if (tile.getUnit() != null && tile.getUnit().isHumanUnit()) {
                processUnitClick(out, tile);
            }
            return;
        }

        //HumanPlayer clicked a human unit previously and clicked a highlight tile now
        if (gameState.getHandPosition() == -1) {

            //This tile is white highlighted, which means it is a movable tile
            if (gameState.getHighlightedFriendlyTiles().contains(tile)) {
                processUnitMove(out, tile);

                //This tile is red highlighted, which means it is a attackable unit
            } else if (gameState.getHighlightedEnemyTiles().contains(tile)) {
                processUnitAttack(out, tile);
            }

            //There is a 'card click' before, and a valid tile is clicked
        } else {
            processCardUse(out, tile);
        }
        Actions.resetWithinOneTurn(out);
        // BasicCommands.clearHighlight();
    }


    private static void processUnitClick(ActorRef out, Tile tile) {
        if (tile.getUnit().isExhausted()) {
            BasicCommands.addPlayer1Notification(out, "This unit is exhausted.", 1);
        } else {
            gameState.setCurrentUnit(tile.getUnit());
            if (tile.getUnit().isMoved()) {
                List<Tile> attackableTiles = TilesGenerator.getAttackableTiles(tile);
            } else {
                gameState.setHighlightedFriendlyTiles(TilesGenerator.getMovableTiles(tile));
                gameState.setHighlightedEnemyTiles(TilesGenerator.getAttackableTiles(tile));
            }
            // BasicCommands.highlightTiles();
        }


    }


    //where should I handle the mana reduce and where do I handle spell animation
    private static void processCardUse(ActorRef out, Tile tile) {
        GameState gameState = GameState.getInstance();
        Card card = gameState.getHumanPlayer().getMyHandCards().get(gameState.getHandPosition());
        if (card.getManacost() <= gameState.getCurrentPlayer().getMana()) {
            if (card instanceof Spell) {
                ((Spell) card).applySpell(tile);
            } else {
                Actions.placeUnit(out, tile);
            }
            BasicCommands.drawHandCards(gameState.getHumanPlayer().getMyHandCards(), out, 1);
        } else {
            //  BasicCommands.addPlayer1Notification();
        }

    }


    private static void processUnitMove(ActorRef out, Tile TargetTile) {
        GameState gameState = GameState.getInstance();
        Actions.unitMove(out, gameState.getCurrentUnit(), TargetTile);
    }

    private static void processUnitAttack(ActorRef out, Tile tile) {
        GameState gameState = GameState.getInstance();
        Actions.unitAttack(out, gameState.getCurrentUnit(), tile.getUnit());
    }


}
