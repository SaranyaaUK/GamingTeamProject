package structures.basic.creatures;

import structures.GameState;
import structures.basic.Unit;

public class ShadowWatcher extends Unit implements DeathWatch {

	@Override
	public void reactToDeath() {
		// TODO Auto-generated method stub
		
		GameState gameState = GameState.getInstance();
		
		// The unit gains +1 attack and +1 health permanently
		// (Cannot increase the creature's maximum health)
		// Add any method that is needed

	}

}
