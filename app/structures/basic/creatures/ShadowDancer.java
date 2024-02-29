package structures.basic.creatures;

import structures.GameState;
import structures.basic.Unit;

public class ShadowDancer extends Unit implements DeathWatch {

	@Override
	public void reactToDeath() {
		// TODO Auto-generated method stub

		GameState gameState = GameState.getInstance();
		
		// Access the enemy avatar and deal one 1 damage to it and 
		// increase this unit's health to 1
	}

}
