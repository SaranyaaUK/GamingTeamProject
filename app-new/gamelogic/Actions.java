package gamelogic;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Position;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;

import java.util.Collection;

import static commands.BasicCommands.*;
import static utils.TilesGenerator.getAttackableTiles;

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
    // do the counter-attack
    // check if avatar dies, is so, end game
    // check if any unit dies, if so, invoke DeathWatch
    // unit attack animation
    // target hit animation
    public static void unitAttack(ActorRef ref, Unit unit, Unit target) {
        // 1. Check if the target is within the attack distance
        if (isAdjacent(unit,target)) {
            unitAdjacentAttack(ref,unit,target);
        }else if(!isAdjacent(unit,target)){
            unitMoveAttack(ref,unit,target);
        }
    }
    // Helper method to check if two units are adjacent to each other
    private static boolean isAdjacent(Unit unit1, Unit unit2) {
        // Assuming units are adjacent if they share the same X or Y coordinate
        return Math.abs(unit1.getPosition().getTilex() - unit2.getPosition().getTilex()) == 1 ||
                Math.abs(unit1.getPosition().getTiley() - unit2.getPosition().getTiley()) == 1;
    }
    public static void unitAdjacentAttack(ActorRef ref, Unit unit, Unit target){
        //enemy unit get damage
        int damageToTarget = unit.getAttack();
        int updatedTargetHealth = target.getHealth() - damageToTarget;
        target.setHealth(updatedTargetHealth);
        //attack animation
        playUnitAnimation(ref, unit, UnitAnimationType.attack);
        //if target is still alive then counter-attack
        if (updatedTargetHealth>0){
            counterAttack(ref, unit, target);
        }
        playUnitAnimation(ref,target,UnitAnimationType.hit);
        if (target.getHealth() <= 0) {
            // Set the target unit's status to dead
            target.setHealth(0);
            //remove unit
            deleteUnit(ref,target);
        }
        //set status
        unit.setExhausted(true);
    }

    public static void unitMoveAttack(ActorRef ref, Unit unit, Unit target){
        // Move unit to a position adjacent to the target unit
        moveToAdjacentPosition(ref, unit, target.getPosition());
        //wait for move
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Perform attack after moving
        unitAdjacentAttack(ref, unit, target);
    }

    // Move the unit to the adjacent position
    public static void moveToAdjacentPosition(ActorRef ref,Unit unit, Position targetPosition){
        Tile adjacentPositionTile = findEmptyAdjacentTile(targetPosition);
        unitMove(ref,unit,adjacentPositionTile);
    }
    public static Tile findEmptyAdjacentTile(Position target){
        // Define the relative offsets for adjacent positions
        int[][] offsets = { {0, -1}, {1, 0}, {0, 1},  {-1, 0}, {-1, -1}, {1, -1}, {1, 1}, {-1, 1}}; // adjacent tiles
        // Iterate through the offsets to check adjacent positions
        for (int[] offset : offsets) {
            int adjacentX = target.getTilex() + offset[0];
            int adjacentY = target.getTiley() + offset[1];
            // Check if the adjacent position is within the bounds of the game board
            // 5 is the gridmax-xsize, 9 is the girdmax-ysize
            // here I change the Grid class's x size and y size to public
            int x = GameState.getInstance().getGrid().gridxsize;
            int y = GameState.getInstance().getGrid().gridysize;
            if (adjacentX >= 0 && adjacentX < x && adjacentY >= 0 && adjacentY < y) {
                // Get the tile corresponding to the adjacent position
                Tile adjacentTile = GameState.getInstance().getGrid().getTile(adjacentX, adjacentY);
                // Check if the tile is empty (not occupied by any unit)
                if (adjacentTile != null && adjacentTile.getUnit() == null) {
                    return adjacentTile;
                }
            }
        }
        return null; // No empty adjacent tile found
    }
    public static void counterAttack(ActorRef ref, Unit unit, Unit target){
        int counterAttackDamage = target.getAttack();
        int updatedUnitHealth = unit.getHealth()-counterAttackDamage;
        unit.setHealth(updatedUnitHealth);
        playUnitAnimation(ref,unit,UnitAnimationType.hit);
        //if died after counter-attack
        if(unit.getHealth()<=0){
            unit.setHealth(0);
            deleteUnit(ref,unit);
        }
    }

    // create the unit first
    // modify the tile and unit
    // check if it is the Gambit
    // call repositionHandCards
    // delete one card in hand
    public static void placeUnit(ActorRef ref, Tile tile) {
    }

}
