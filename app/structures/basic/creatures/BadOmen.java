package structures.basic.creatures;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.basic.Unit;

/*
 *  BadOmen.java
 *  
 *  This class implements the Death interface, reacts when a unit dies on the board
 *  
 */
public class BadOmen extends Unit implements DeathWatch {

	/*
	 *  Reacts to death of any unit on the board
	 */
	@Override
	public void reactToDeath(ActorRef out) {
		// The unit gains +1 attack permanently
		this.setAttack(this.getAttack()+1);
		
		// Increase the unit's attack in front-end
		BasicCommands.setUnitAttack(out, this, this.getAttack());
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
	}

}
