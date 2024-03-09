package gamelogic;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Card;
import structures.basic.GameLogic;
import structures.basic.Tile;
import structures.basic.spell.Spell;
import structures.basic.spell.WraithlingSwarm;
import utils.TilesGenerator;

public class ProcessTileClicked {
    static GameState gameState = GameState.getInstance();

    public static void dispatchAfterTileClicked(ActorRef out, Tile tile) {

        //BasicCommands.clearHighLight();
        if (!gameState.getHighlightedEnemyTiles().contains(tile) && !gameState.getHighlightedFriendlyTiles().contains(tile)) {

            if (GameState.wraithlingSummonStatus()) {
                return; // Do not reset, just return
            }
            Actions.resetWithinOneTurn(out);
            BasicCommands.dehighlightCards(out);
            //If a de-highlighted tile is clicked and there is one human unit is within this tile
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
            // Delete card from hand after it has summoned or casted spell on board      
            if (!GameState.wraithlingSummonStatus()) {
                GameLogic.updateHandCardsView(out);
            }
        }
        Actions.resetWithinOneTurn(out);
    }

    private static void processUnitClick(ActorRef out, Tile tile) {
        if (tile.getUnit().isExhausted()) {
            BasicCommands.addPlayer1Notification(out, "This unit is exhausted.", 1);
        } else {
            gameState.setCurrentUnit(tile.getUnit());
            if (tile.getUnit().isMoved()) {
                gameState.setHighlightedEnemyTiles(TilesGenerator.getAttackableTiles(gameState.getCurrentUnit()));
            } else {
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


    //where should I handle the mana reduce and where do I handle spell animation
    public static void processCardUse(ActorRef out, Tile tile) {
        GameState gameState = GameState.getInstance();
        Card card = gameState.getClickedCard();
        processCardUse(out, tile, card);
    }

    //This one if for AI use
    public static void processCardUse(ActorRef out, Tile tile, Card card) {

        if (card.getManacost() <= gameState.getCurrentPlayer().getMana()) {
            // Update Mana
            Actions.updateCurrentPlayerMana(out, gameState.getCurrentPlayer().getMana() - card.getManacost());

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
        } else {
            if (GameState.wraithlingSummonStatus()) {
                ProcessTileClicked.handleWraithlingSwarm(out, tile);
            } else {
                BasicCommands.addPlayer1Notification(out, "Not enough Mana", 100);
            }
        }
    }

    private static void handleWraithlingSwarm(ActorRef out, Tile tile) {
        Spell mySpellInstance = gameState.getSpellToCast();
        mySpellInstance.applySpell(out, tile);
        if (((WraithlingSwarm) mySpellInstance).getNumWraithlings() != 3) {
            BasicCommands.dehighlightTiles(out);
            mySpellInstance.getTargetTilesToHighlight();
            BasicCommands.highlightTiles(out);
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
