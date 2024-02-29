package structures.basic.spell;

import java.util.List;

import structures.GameState;
import structures.basic.Tile;

public class SundropElixir implements Spell {

	@Override
	public void applySpell(Tile tile) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Tile> getTargetTilesToHighlight() {
		// TODO Auto-generated method stub

		GameState gameState = GameState.getInstance();
		// Should choose a friendly units - must use the TilesGenerator.getFriendlyUnitTiles();

		// Need to set the appropriate highlighted tiles list here
		//			gameState.setHighlightedFriendlyTiles(<toHighlightTiles>);
		return null;
	}

}
