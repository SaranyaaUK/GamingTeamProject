package gamelogic;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;

import java.util.Collection;

public class Actions {
    public static void resetWithinOneTurn(ActorRef out) {
        GameState gameState = GameState.getInstance();
        gameState.setHandPosition(-1);
        gameState.setCurrentUnit(null);
        gameState.setSpellToCast(null);
        gameState.getHighlightedFriendlyTiles().clear();
        gameState.getHighlightedEnemyTiles().clear();
    }

    // reset all the unit's isExhausted/isMoved
    // remove all the highlight
    // reset handPosition
    // reset current player's Mana to 0
    public static void resetAfterOneTurn(ActorRef out) {

    }

    // modify the state of unit
    // modify the tile
    // move animation
    // gamelogic.associateWithTile
    public static void unitMove(ActorRef ref, Unit unit, Tile tile) {

    }

    // check if the target if within the attack distance,if not, move first.
    // then calculate and update the health of target
    // do the counter attack
    // check if avatar dies, is so, end game
    // check if any unit dies, if so, invoke DeathWatch
    // unit attack animation
    // target hit animation
    public static void unitAttack(ActorRef ref, Unit unit, Unit target) {

    }

    // create the unit first
    // modify the tile and unit
    // check if it is the Gambit
    // call repositionHandCards
    // delete one card in hand
    public static void placeUnit(ActorRef ref, Tile tile) {
    }

}
