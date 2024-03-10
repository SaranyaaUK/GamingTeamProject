package gamelogic;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.*;
import structures.basic.spell.Spell;
import structures.basic.spell.WraithlingSwarm;
import utils.TilesGenerator;

/**
 * ProcessTileClicked.java
 * <p>
 * Process various actions related to Tile click event
 */

public class ProcessTileClicked {
    static GameState gameState = GameState.getInstance();

    /**
     * DispatchAfterTileClicked
     * <p>
     * Actions that occur after a tile is clicked
     *
     * @param out  (ActorRef)
     * @param tile (Tile)
     */
    public static void dispatchAfterTileClicked(ActorRef out, Tile tile) {
        if (!gameState.getHighlightedEnemyTiles().contains(tile) && !gameState.getHighlightedFriendlyTiles().contains(tile)) {

            if (GameLogic.wraithlingSummonStatus()) {
                return; // Do not reset, just return
            }
            Actions.resetWithinOneTurn(out);
            BasicCommands.dehighlightCards(out);
            //If a de-highlighted tile is clicked and there is one human unit in this tile
            if (tile.getUnit() != null && tile.getUnit().isHumanUnit()) {
                processUnitClick(out, tile);
            }
            return;
        }

        //HumanPlayer clicked a human unit previously and clicked a highlight tile now
        if (!gameState.isCardClicked()) {

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
    }

    /**
     * ProcessUnitClick
     * <p>
     * Actions that occur after unit is clicked
     *
     * @param out  (ActorRef)
     * @param tile (Tile)
     */
    private static void processUnitClick(ActorRef out, Tile tile) {
        if (tile.getUnit().isExhausted()) {
            BasicCommands.addPlayer1Notification(out, "This unit is exhausted.", 1);
        } else {
            // Update the current clicked unit
            gameState.setCurrentUnit(tile.getUnit());

            // Set the appropriate tiles to highlight
            if (tile.getUnit().isMoved()) {
                // If selected unit has moved already just get adjacent enemy units
                gameState.setHighlightedEnemyTiles(TilesGenerator.getAttackableTiles(gameState.getCurrentUnit()));
            } else {
                // If selected unit has not moved already - get possible move and attack tiles too.
                if (!TilesGenerator.hasProvokeUnitAround(gameState.getCurrentUnit())) {
                    if (!TilesGenerator.getMovableTiles(gameState.getCurrentUnit()).isEmpty()) {
                        gameState.setHighlightedFriendlyTiles(TilesGenerator.getMovableTiles(gameState.getCurrentUnit()));
                    }
                }
                if (!TilesGenerator.getAttackableTiles(gameState.getCurrentUnit()).isEmpty()) {
                    gameState.setHighlightedEnemyTiles(TilesGenerator.getAttackableTiles(gameState.getCurrentUnit()));
                }
            }
            BasicCommands.highlightTiles(out);
        }
    }

    /**
     * ProcessCardUse
     *
     * @param out  (ActorRef)
     * @param tile (Tile)
     */
    public static void processCardUse(ActorRef out, Tile tile) {
        GameState gameState = GameState.getInstance();
        Card card = gameState.getClickedCard();
        processCardUse(out, tile, card);
    }

    /**
     * HandleWraithlingSwarm
     * <p>
     * Handle the wraithling swarm summoning
     *
     * @param out  (ActorRef)
     * @param tile (Tile)
     */
    private static void handleWraithlingSwarm(ActorRef out, Tile tile) {
        Spell mySpellInstance = gameState.getSpellToCast();
        mySpellInstance.applySpell(out, tile);
        if (((WraithlingSwarm) mySpellInstance).getNumWraithlings() != 3) {
            BasicCommands.dehighlightTiles(out);
            mySpellInstance.getTargetTilesToHighlight();
            BasicCommands.highlightTiles(out);
        }
    }

    /**
     * ProcessUnitMove
     *
     * @param out        (ActorRef)
     * @param targetTile (Tile)
     */
    private static void processUnitMove(ActorRef out, Tile targetTile) {
        GameState gameState = GameState.getInstance();
        Actions.unitMove(out, gameState.getCurrentUnit(), targetTile);
    }

    /**
     * ProcessUnitAttack
     *
     * @param out  (ActorRef)
     * @param tile (Tile)
     */
    private static void processUnitAttack(ActorRef out, Tile tile) {
        GameState gameState = GameState.getInstance();
        Actions.unitAttack(out, gameState.getCurrentUnit(), tile.getUnit());
    }


    public static void processCardUse(ActorRef out, Tile tile, Card card) {
        GameState gameState = GameState.getInstance();
        if (card.getManacost() <= gameState.getCurrentPlayer().getMana()) {
            // Update Mana
            GameLogic.updateCurrentPlayerMana(out, gameState.getCurrentPlayer().getMana() - card.getManacost());

            if (!card.isCreature()) {
                Spell mySpellInstance = gameState.getSpellToCast();
                // Inside the processCardUse method
                if (gameState.isSpellWraithlingSwarm()) {
                    ProcessTileClicked.handleWraithlingSwarm(out, tile);
                } else {
                    mySpellInstance.applySpell(out, tile);
                }
            } else {
                Actions.placeUnit(out, tile);
            }
            // Delete card from hand after it has summoned or casted spell on board
            if (!GameLogic.wraithlingSummonStatus()) {
                GameLogic.updateHandCardsView(out);
            }
        } else {
            if (GameLogic.wraithlingSummonStatus()) {
                ProcessTileClicked.handleWraithlingSwarm(out, tile);
                // Delete card from hand after it has summoned or casted spell on board
                if (!GameLogic.wraithlingSummonStatus()) {
                    GameLogic.updateHandCardsView(out);
                }
            } else {
                BasicCommands.addPlayer1Notification(out, "Not enough Mana", 10);
            }
        }
    }
}
