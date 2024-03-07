package structures.basic.creatures;

import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class Wraithlings extends Unit {
	
	final static int DEFAULT_HEALTH = 1;
	final static int DEFAULT_ATTACK = 1;
	
	Wraithlings(){
		super();
		this.setHealth(DEFAULT_HEALTH);
		this.setAttack(DEFAULT_ATTACK);
		this.setHumanUnit(true);
	}
	
	public static Unit createWraithling() {
		// For wraithlings setting the id as -1 (I guess we might not need any tracking with the id)
		return BasicObjectBuilders.loadUnit(StaticConfFiles.wraithling, -1, Wraithlings.class);
	}
}
