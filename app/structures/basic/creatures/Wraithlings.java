package structures.basic.creatures;

import structures.basic.Unit;

public class Wraithlings extends Unit {
	
	final static int DEFAULT_HEALTH = 1;
	final static int DEFAULT_ATTACK = 1;
	
	Wraithlings(){
		super();
		this.setHealth(DEFAULT_HEALTH);
		this.setAttack(DEFAULT_ATTACK);
		this.setHumanUnit(true);
	}
}
