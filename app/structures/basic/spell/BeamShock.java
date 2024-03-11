package structures.basic.spell;

import java.util.List;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.TilesGenerator;

/**
 *  BeamShock.java
 *  
 *  This class implements the Spell interface, gives the target tiles to highlight when
 *  the corresponding card is chosen and the effect to apply when the spell is casted.
 *  
 */

public class BeamShock implements Spell {

	/**
	 *  Spell cast actions
	 *  
	 *  @param out (ActorRef)
	 *  @param tile (Tile)
	 *  
	 */
	@Override
	public void applySpell(ActorRef out, Tile tile) {
		// Stunt an enemy unit in the given tile
		
		Unit myTargetUnit = tile.getUnit();
		myTargetUnit.setStunned(true);
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
		
		// Remove avatar
		Unit avatar = null;
		if (gameState.isCurrentPlayerHuman()) {
			avatar = gameState.getAIPlayer().getAvatar();
		} else {
			avatar = gameState.getHumanPlayer().getAvatar();
		}

		// Except avatar all units can be stunned, hence removing the avatar
		toHighlightTiles.removeAll(TilesGenerator.getAvatarTile(avatar));
		gameState.setHighlightedEnemyTiles(toHighlightTiles);

		return toHighlightTiles;
	}

}
