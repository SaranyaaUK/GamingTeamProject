package structures.basic.spell;

import java.util.List;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.TilesGenerator;

public class HornOfForsaken implements Spell {

	final int avatarArtifact = 3;

	@Override
	public void applySpell(ActorRef out, Tile tile) {

		// We'll reach this point only if they have selected the avatar unit
		// So, it is okay to directly use the player's avatar.
		GameState gameState = GameState.getInstance();

		Player humanPlayer = gameState.getHumanPlayer();

		Unit humanAvatar = humanPlayer.getAvatar();
		// If the spell is casted when avatar is already having this attribute it is overriden
		humanAvatar.setArtifact(avatarArtifact);
	}

	@Override
	public List<Tile> getTargetTilesToHighlight() {

		GameState gameState = GameState.getInstance();
		// Horn of Forsaken, must know about the human avatar's position
		Unit humanAvatar = gameState.getHumanPlayer().getAvatar();

		// To highlight tiles
		List<Tile> toHighlightTiles = TilesGenerator.getAvatarTile(humanAvatar);
		gameState.setHighlightedFriendlyTiles(toHighlightTiles);

		return toHighlightTiles;
	}

}
