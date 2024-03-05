package structures.basic.spell;

import java.util.List;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.TilesGenerator;

public class SundropElixir implements Spell {
	
	final int alliedHealth = 4;
	@Override
	public void applySpell(ActorRef out, Tile tile) {

		Unit myTargetUnit = tile.getUnit();
		
		// Heal allied unit by 4 health (capped at maximum health)
		int maxHealth = myTargetUnit.getMaximumHealth();
		int currentHealth = myTargetUnit.getHealth();
		
		myTargetUnit.setHealth(Math.min(maxHealth, currentHealth + alliedHealth));
	}

	@Override
	public List<Tile> getTargetTilesToHighlight() {

		GameState gameState = GameState.getInstance();
		// To highlight tiles
		List<Tile> toHighlightTiles = TilesGenerator.getFriendlyUnitTiles();
		gameState.setHighlightedFriendlyTiles(toHighlightTiles);

		return toHighlightTiles;
	}

}
