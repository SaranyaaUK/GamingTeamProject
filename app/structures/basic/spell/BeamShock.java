package structures.basic.spell;

import java.util.List;

import structures.GameState;
import structures.basic.Tile;
import utils.TilesGenerator;

public class BeamShock implements Spell {

	@Override
	public void applySpell(Tile tile) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Tile> getTargetTilesToHighlight() {
		// TODO Auto-generated method stub

		GameState gameState = GameState.getInstance();
		// Should choose an enemy unit - must use the TilesGenerator.getEnemyUnitTiles(); but not avatar

		// Need to set the appropriate highlighted tiles list here
		//		gameState.setHighlightedEnemyTiles(<toHighlightTiles>);

		return null;
	}

}
