package structures.basic.spell;

import java.util.List;

import structures.GameState;
import structures.basic.Tile;

public class WraithlingSwarm implements Spell {

	private int NumWraithlings = 0;
	
	public int getNumWraithlings() {
		return NumWraithlings;
	}

	public void setNumWraithlings(int numWraithlings) {
		NumWraithlings = numWraithlings;
	}

	@Override
	public void applySpell(Tile tile) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Tile> getTargetTilesToHighlight() {
		// TODO Auto-generated method stub
		
		GameState gameState = GameState.getInstance();
		// Should use the Utils.TilesGenerator getTilesToSummon method
		
		// Set the appropriate Highlight tile lsit
//		gameState.setHighlightedFriendlyTiles(<toHighlightTiles>);
		
		return null;
	}

}
