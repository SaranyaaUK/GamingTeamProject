package structures.basic.spell;

import java.util.List;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.*;
import utils.*;

/**
 *  SundropElixir.java
 *  
 *  This class implements the Spell interface, gives the target tiles to highlight when
 *  the corresponding card is chosen and the effect to apply when the spell is casted.
 */

public class SundropElixir implements Spell {

	final int alliedHealth = 4;

	/**
	 *  Spell cast actions
	 *  
	 *  @param out (ActorRef)
	 *  @param tile (Tile)
	 *  
	 */
	@Override
	public void applySpell(ActorRef out, Tile tile) {

		Unit myTargetUnit = tile.getUnit();

		// Play the effect animation
		EffectAnimation buffEffect = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff);
		BasicCommands.playEffectAnimation(out, buffEffect, tile);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		// Heal allied unit by 4 health (capped at maximum health)
		int maxHealth = myTargetUnit.getMaximumHealth();
		int currentHealth = myTargetUnit.getHealth();

		myTargetUnit.setHealth(Math.min(maxHealth, currentHealth + alliedHealth));
		
		// Update the health in front-end
		// Update target's health
        BasicCommands.setUnitHealth(out, myTargetUnit, myTargetUnit.getHealth());
        try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
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
		// To highlight tiles
		List<Tile> toHighlightTiles = TilesGenerator.getFriendlyUnitTiles();
		gameState.setHighlightedFriendlyTiles(toHighlightTiles);

		return toHighlightTiles;
	}

}
