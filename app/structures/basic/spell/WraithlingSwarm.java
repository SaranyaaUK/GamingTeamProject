package structures.basic.spell;

import java.util.List;
import akka.actor.ActorRef;
import commands.BasicCommands;
import gamelogic.Actions;
import structures.GameState;
import structures.basic.Tile;
import utils.TilesGenerator;

public class WraithlingSwarm implements Spell {

	private int numWraithlings = 0;
	public static final int maximumWraithlings = 3;


	public int getNumWraithlings() {
		return this.numWraithlings;
	}

	public void setNumWraithlings(int numWraithlings) {
		this.numWraithlings = numWraithlings;
	}

	@Override
	public void applySpell(ActorRef out, Tile tile) {
		GameState gameState = GameState.getInstance();
		Actions.placeUnit(out, tile);
		numWraithlings++;
	}

	@Override
	public List<Tile> getTargetTilesToHighlight() {

		GameState gameState = GameState.getInstance();
		// To highlight tiles
		List<Tile> tilesToHighlight = TilesGenerator.getTilesToSummon();
		gameState.setHighlightedFriendlyTiles(tilesToHighlight);

		return tilesToHighlight;
	}

}
