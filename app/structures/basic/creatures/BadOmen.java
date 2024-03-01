package structures.basic.creatures;

import structures.GameState;
import structures.basic.Unit;

public class BadOmen extends Unit implements DeathWatch {

	@Override
	public void reactToDeath() {
		// TODO Auto-generated method stub
		
		GameState gameState = GameState.getInstance();
		
		// The unit gains +1 attack permanently
	}

}
