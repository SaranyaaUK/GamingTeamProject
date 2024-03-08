package structures.basic.spell;

import java.util.List;

import akka.actor.ActorRef;
import commands.BasicCommands;
import gamelogic.Actions;
import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.TilesGenerator;

/**
 *  DarkTerminus.java
 *  
 *  This class implements the Spell interface, gives the target tiles to highlight when
 *  the corresponding card is chosen and the effect to apply when the spell is casted.
 */

public class DarkTerminus implements Spell {

	final int health = 0;
	
	/**
	 *  Spell cast actions
	 *  
	 *  @param out (ActorRef)
	 *  @param tile (Tile)
	 */
	@Override
	public void applySpell(ActorRef out, Tile tile) {
		Unit target = tile.getUnit();
		// Kill the unit on the targeted tile
		target.setHealth(health);
		
		// Front-end communication
		// Update target's health
        BasicCommands.setUnitHealth(out, target, Math.max(target.getHealth(),0));
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        
        Actions.unitDeathAction(out, target);

		// Create a Wraithling and position it on the same tile
		Actions.placeWraithling(out, tile);
	}

	/**
	 *  Return the tiles to be highlighted when the corresponding spell card 
	 *  is selected
	 *  
	 *  @return List<Tile>
	 */
	@Override
	public List<Tile> getTargetTilesToHighlight() {

		GameState gameState = GameState.getInstance();
		Unit aiAvatar = gameState.getAIPlayer().getAvatar();

		// To highlight enemy tiles
		List<Tile> toHighlightTiles = TilesGenerator.getEnemyUnitTiles();
		
		// Eliminate the enemy avatar from the list
		toHighlightTiles.removeAll(TilesGenerator.getAvatarTile(aiAvatar));
		gameState.setHighlightedEnemyTiles(toHighlightTiles);

		return toHighlightTiles;
	}

}
