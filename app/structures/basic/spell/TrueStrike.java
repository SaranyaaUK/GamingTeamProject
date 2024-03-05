package structures.basic.spell;

import java.util.List;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.GameLogic;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import utils.TilesGenerator;

public class TrueStrike implements Spell {

	final int updateHealth = 2;
	@Override
	public void applySpell(ActorRef out, Tile tile) {

		Unit target = tile.getUnit();

		int updatedHealth = Math.max(target.getHealth() - updateHealth, 0);
		// Reduce the target unit's health by 2
		target.setHealth(updatedHealth);
		
		// Update target's health
        BasicCommands.setUnitHealth(out, target, updatedHealth);
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}

		if (target.getHealth() == 0) {
        	// Target plays the death animation
            BasicCommands.playUnitAnimation(out, target, UnitAnimationType.death);
            try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
            
        	//Remove unit
            // If actual attack remove unit from the player's opponent list
            if(GameState.getInstance().isCurrentPlayerHuman()) {
            	tile.setUnit(null);
                GameState.getInstance().getAIPlayer().getMyUnits().remove(target);
            } else {
            	tile.setUnit(null);
            	GameState.getInstance().getHumanPlayer().getMyUnits().remove(target);
            }
        	BasicCommands.deleteUnit(out, target);
        	try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        	
        	// Check for End game - avatars dying <TO ADD>
        	
        	// Notify DeathWatchers
        	GameLogic.notifyDeathWatchers(out);
        }
	}

	@Override
	public List<Tile> getTargetTilesToHighlight() {

		GameState gameState = GameState.getInstance();
		// To highlight enemy tiles
		List<Tile> toHighlightTiles = TilesGenerator.getEnemyUnitTiles();
		gameState.setHighlightedEnemyTiles(toHighlightTiles);

		return toHighlightTiles;
	}

}
