package structures.basic.spell;

import java.util.List;

import akka.actor.ActorRef;
import commands.BasicCommands;
import gamelogic.Actions;
import structures.GameState;
import structures.basic.GameLogic;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import utils.TilesGenerator;

public class DarkTerminus implements Spell {

	final int health = 0;
	@Override
	public void applySpell(ActorRef out, Tile tile) {
		Unit target = tile.getUnit();
		// Kill the unit on the target tile
		target.setHealth(health);
		
		// Front-end communication
		// Update target's health
        BasicCommands.setUnitHealth(out, target, Math.max(target.getHealth(),0));
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        
        Actions.unitDeathAction(out, target);

		// Create a Wraithling and position it on the same tile
		Actions.placeUnit(out, tile);
	}

	@Override
	public List<Tile> getTargetTilesToHighlight() {

		GameState gameState = GameState.getInstance();
		Unit aiAvatar = gameState.getAIPlayer().getAvatar();

		// To highlight enemy tiles
		List<Tile> toHighlightTiles = TilesGenerator.getEnemyUnitTiles();
		toHighlightTiles.removeAll(TilesGenerator.getAvatarTile(aiAvatar));
		gameState.setHighlightedEnemyTiles(toHighlightTiles);

		return toHighlightTiles;
	}

}
