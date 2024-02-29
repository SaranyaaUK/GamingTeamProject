package structures.basic.spell;

import java.util.List;

import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.TilesGenerator;

public class HornOfForsaken implements Spell {

	final int avatarArtefact = 3;

	@Override
	public void applySpell(Tile tile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Tile> getTargetTilesToHighlight() {
		
		GameState gameState = GameState.getInstance();
		// Horn of Forsaken, must know about the human avatar's position
		Unit humanAvatar = gameState.getHumanPlayer().getAvatar();
		
		// Get the tile to highlight
		List<Tile> tiles = TilesGenerator.getAvatarTile(humanAvatar);

		gameState.setHighlightedFriendlyTiles(tiles);

		return tiles;
	}

}
