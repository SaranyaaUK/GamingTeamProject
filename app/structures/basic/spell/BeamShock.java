package structures.basic.spell;

import java.util.List;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.TilesGenerator;

public class BeamShock implements Spell {

	@Override
	public void applySpell(ActorRef out, Tile tile) {
		// TODO Auto-generated method stub
		Unit myTargetUnit = tile.getUnit();
		myTargetUnit.setStunned(true);
	}

	@Override
	public List<Tile> getTargetTilesToHighlight() {

		GameState gameState = GameState.getInstance();
		Unit humanAvatar = gameState.getHumanPlayer().getAvatar();

		// To highlight enemy tiles
		List<Tile> toHighlightTiles = TilesGenerator.getEnemyUnitTiles();
		
		// Except avatar all units can be stunned, hence removing the humanAvatarTile
		toHighlightTiles.removeAll(TilesGenerator.getAvatarTile(humanAvatar));
		gameState.setHighlightedEnemyTiles(toHighlightTiles);

		return toHighlightTiles;
	}

}
