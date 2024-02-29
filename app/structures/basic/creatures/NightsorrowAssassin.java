package structures.basic.creatures;

import structures.GameState;
import structures.basic.Unit;

public class NightsorrowAssassin extends Unit implements OpeningGambit {

	@Override
	public void reactToUnitsSummon() {
		// TODO Auto-generated method stub
		
		GameState gameState = GameState.getInstance();
		
		// Access any enemy unit in an adjacent tile to this unit 
		// and if it is less than its maximum health, destroy it.

	}

}
