package structures.basic.creatures;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.*;
import utils.TilesGenerator;

/*
 *  SilverguardSquire.java
 *
 *  This class implements the opening gambit interface, reacts when this unit is
 *  summoned on the the board.
 *
 */

public class SilverguardSquire extends Unit implements OpeningGambit {

    /*
     *  Effects to be triggered when this unit is summoned
     */
    @Override
    public void reactToUnitsSummon(ActorRef out, Tile tile) {
        GameState gameState = GameState.getInstance();
        Grid myGrid = gameState.getGrid();

        // Give any adjacent allied unit that is directly infront
        // or behind the owning player's (AI) avatar +1 attack and
        // +1 health permanently (can increase the creatures maximum health)
        Unit avatar = gameState.getCurrentPlayer().getAvatar();
        int tilex = avatar.getPosition().getTilex();
        int tiley = avatar.getPosition().getTiley();

        // front and behind for Human and AI player
        int behind, front;
        if (gameState.isCurrentPlayerHuman()) {
            // Human Player
            front = tilex + 1;
            behind = tilex - 1;
        } else {
            // AI Player
            front = tilex - 1;
            behind = tilex + 1;
        }

        // Check for unit in front of the owning player's avatar
        Unit unit;
        if (TilesGenerator.isValidTile(front, tiley) && (myGrid.getTile(front, tiley).getUnit() != null)) {
            // Front
            unit = myGrid.getTile(front, tiley).getUnit();
            summonEffect(out, unit);
        }

        // Check for unit behind the owning player's avatar
        if (TilesGenerator.isValidTile(behind, tiley) && (myGrid.getTile(behind, tiley).getUnit() != null)) {
            // Behind
            unit = myGrid.getTile(behind, tiley).getUnit();
            summonEffect(out, unit);
        }
    }

    /*
     *  Helper method
     *  To update health and attack of the target unit
     */
    private void summonEffect(ActorRef out, Unit unit) {
        unit.setAttack(unit.getAttack() + 1);
        unit.setHealth(unit.getHealth() + 1);
        // Can increase the unit's maximum health
        unit.setMaximumHealth(Math.max(unit.getHealth(), unit.getMaximumHealth()));

        // Update health and attack in the front end
        BasicCommands.setUnitAttack(out, unit, unit.getAttack());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BasicCommands.setUnitHealth(out, unit, unit.getHealth());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
