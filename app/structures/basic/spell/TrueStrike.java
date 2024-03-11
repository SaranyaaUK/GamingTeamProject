package structures.basic.spell;

import java.util.List;

import akka.actor.ActorRef;
import commands.BasicCommands;
import gamelogic.Actions;
import structures.GameState;
import structures.basic.*;
import utils.*;

/**
 *  TrueStrike.java
 *  
 *  This class implements the Spell interface, gives the target tiles to highlight when
 *  the corresponding card is chosen and the effect to apply when the spell is casted.
 */

public class TrueStrike implements Spell {

	final int updateHealth = 2;
	
	/**
	 *  Spell cast actions
	 *  
	 *  @param out (ActorRef)
	 *  @param tile (Tile)
	 *  
	 */
	@Override
	public void applySpell(ActorRef out, Tile tile) {
		Unit target = tile.getUnit();

		int updatedHealth = Math.max(target.getHealth() - updateHealth, 0);
		// Reduce the target unit's health by 2
		target.setHealth(updatedHealth);
		
		// Play the effect animation
		EffectAnimation buffEffect = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_inmolation);
		BasicCommands.playEffectAnimation(out, buffEffect, tile);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		
		// Update target's health
        BasicCommands.setUnitHealth(out, target, updatedHealth);
        try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

        // Update the Player's health if the avatar was target
        GameLogic.updatePlayerHealth(out, target);

        if (target.getHealth() == 0) {
        	Actions.unitDeathAction(out, target);
        }
	}

	/**
	 *  Return the tiles to be highlighted when the corresponding spell card 
	 *  is selected
	 *  
	 *  @return List<Tile>
	 *  
	 */
	@Override
	public List<Tile> getTargetTilesToHighlight() {

		GameState gameState = GameState.getInstance();
		// To highlight enemy tiles
		List<Tile> toHighlightTiles = TilesGenerator.getEnemyUnitTiles();
		gameState.setHighlightedEnemyTiles(toHighlightTiles);

		return toHighlightTiles;
	}

}
