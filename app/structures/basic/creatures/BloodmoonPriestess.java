package structures.basic.creatures;

import structures.GameState;
import structures.basic.Unit;

public class BloodmoonPriestess extends Unit implements DeathWatch {

	@Override
	public void reactToDeath() {
		// TODO Auto-generated method stub
		
		GameState gameState = GameState.getInstance();
		
		// Summon a wraithling to a randomly selected unoccupied adjacent tile, 
		// if there are no unoccupied tiles, then this ability has no effect
	}

}
