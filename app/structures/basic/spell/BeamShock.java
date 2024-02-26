package structures.basic.spell;

import java.util.List;

import structures.GameState;
import structures.basic.Tile;

public class BeamShock implements Spell {

	@Override
	public void applySpell(Tile tile, GameState gameState) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Tile> getTargetTilesToHighlight(GameState gameState) {
		// TODO Auto-generated method stub
		
		// Should choose an enemy unit - must use the TilesGenerator.getEnemyUnitTiles(); but not avatar
		return null;
	}

}
