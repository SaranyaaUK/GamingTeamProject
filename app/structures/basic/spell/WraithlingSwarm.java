package structures.basic.spell;

import java.util.List;
import akka.actor.ActorRef;
import gamelogic.Actions;
import structures.GameState;
import structures.basic.Tile;
import utils.TilesGenerator;

/**
 *  WraithlingSwarm.java
 *  
 *  This class implements the Spell interface, gives the target tiles to highlight when
 *  the corresponding card is chosen and the effect to apply when the spell is casted.
 */

public class WraithlingSwarm implements Spell {

	private int numWraithlings = 0;
	public static final int maximumWraithlings = 3;

	/**
	 *  Get the number of warithlings summoned
	 *  
	 *  @return int
	 *  
	 */
	public int getNumWraithlings() {
		return this.numWraithlings;
	}

	/**
	 *  Set the number of wraithlings
	 *  
	 *  @param numWraithlings (int)
	 *  
	 */
	public void setNumWraithlings(int numWraithlings) {
		this.numWraithlings = numWraithlings;
	}

	/**
	 *  Spell cast actions
	 *  
	 *  @param out (ActorRef)
	 *  @param tile (Tile)
	 *  
	 */
	@Override
	public void applySpell(ActorRef out, Tile tile) {
		Actions.placeWraithling(out, tile);
		numWraithlings++;
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
		// To highlight tiles
		List<Tile> tilesToHighlight = TilesGenerator.getTilesToSummon();
		gameState.setHighlightedFriendlyTiles(tilesToHighlight);

		return tilesToHighlight;
	}

}
