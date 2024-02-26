package structures.basic.spell;

import java.util.List;

import structures.GameState;
import structures.basic.Tile;

public class SundropElixir implements Spell {

	@Override
	public void applySpell(Tile tile, GameState gameState) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Tile> getTargetTilesToHighlight(GameState gameState) {
		// TODO Auto-generated method stub
		
		// Should choose a friendly units - must use the TilesGenerator.getFriendlyUnitTiles();
		return null;
	}

}
