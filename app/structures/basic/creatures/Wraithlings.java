package structures.basic.creatures;

import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/*
 *  Wraithlings.java
 *    
 *  This class extends Unit. Represents a token that
 *  is not associated with a card.
 */

public class Wraithlings extends Unit {
	
	final static int DEFAULT_HEALTH = 1;
	final static int DEFAULT_ATTACK = 1;
	static int wraithlingsID = 43;
	
	Wraithlings(){
		super();
		this.setHealth(DEFAULT_HEALTH);
		this.setAttack(DEFAULT_ATTACK);
		this.setHumanUnit(true);
	}
	
	/*
	 *  Create a wraithling and update its other attributes
	 *  
	 */
	public static Unit createWraithling() {
		// Load the unit
		Unit unit =  BasicObjectBuilders.loadUnit(StaticConfFiles.wraithling, wraithlingsID++, Wraithlings.class);
		unit.setMaximumHealth(unit.getHealth());
		unit.setHumanUnit(true);

		return unit;
	}
}
