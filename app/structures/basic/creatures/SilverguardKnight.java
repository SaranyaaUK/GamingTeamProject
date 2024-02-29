package structures.basic.creatures;

import structures.GameState;
import structures.basic.Unit;

public class SilverguardKnight extends Unit implements Zeal, Provoke {

	@Override
	public void applyZeal(GameState gameState) {
		// TODO Auto-generated method stub
		
		// Will be called when the owning player's (AI) avatar
		// takes the damage
		// Should increase the unit's attack +2 permanently
	}
}
