package structures.basic.spell;

import java.util.List;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.TilesGenerator;

/*
 *  HornOfForsaken.java
 *  
 *  This class implements the Spell interface, gives the target tiles to highlight when
 *  the corresponding card is chosen and the effect to apply when the spell is casted.
 */

public class HornOfForsaken implements Spell {

	final int avatarArtifact = 3;

	/**
	 *  Spell cast actions
	 *  
	 *  @param out (ActorRef)
	 *  @param tile (Tile)
	 *  
	 */
	@Override
	public void applySpell(ActorRef out, Tile tile) {

		// We'll reach this point only if they have selected the avatar unit
		// So, it is okay to directly use the player's avatar.
		GameState gameState = GameState.getInstance();

		Player humanPlayer = gameState.getHumanPlayer();

		Unit humanAvatar = humanPlayer.getAvatar();
		
		// If the spell is casted again when avatar is already having 
		// positive artifact value, then it is overriden
		humanAvatar.setArtifact(avatarArtifact);
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
		// Horn of Forsaken, must know about the human avatar's position
		Unit humanAvatar = gameState.getHumanPlayer().getAvatar();

		// To highlight tiles
		List<Tile> toHighlightTiles = TilesGenerator.getAvatarTile(humanAvatar);
		gameState.setHighlightedFriendlyTiles(toHighlightTiles);

		return toHighlightTiles;
	}
}
