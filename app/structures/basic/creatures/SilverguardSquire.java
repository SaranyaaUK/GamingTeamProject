package structures.basic.creatures;

import structures.GameState;
import structures.basic.Unit;

public class SilverguardSquire extends Unit implements OpeningGambit {

	@Override
	public void reactToUnitsSummon() {
		// TODO Auto-generated method stub
		
		// Give any adjacent allied unit that is directly infront
		// or behind the owning player's (AI) avatar +1 attack and
		// +1 health permanently (can increase the creatures maximum health)

	}

}
