package structures.basic.spell;

import java.util.List;

import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.TilesGenerator;

public class HornOfForsaken implements Spell {

	@Override
	public void applySpell(Tile tile, GameState gameState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Tile> getTargetTilesToHighlight(GameState gameState) {
		// Horn of Forsaken, must know about the human avatar's position
		Unit humanAvatar = gameState.getHumanPlayer().getAvatar();
		return TilesGenerator.getAvatarTile(gameState, humanAvatar, 1);
	}

}
