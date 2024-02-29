package structures.basic.creatures;

import structures.GameState;
import structures.basic.Unit;

public class GloomChaser extends Unit implements OpeningGambit {

	@Override
	public void reactToUnitsSummon() {
		// TODO Auto-generated method stub
		
		GameState gameState = GameState.getInstance();
		// Should summon wraithlings to a tile behind this unit if that tile is empty
		// If the tile is occupied do nothing.
		
		// If wraithling is added make sure to add to the Player's unit list
	}

}
