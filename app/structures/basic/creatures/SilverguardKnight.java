package structures.basic.creatures;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.basic.Unit;

/*
 *  SilverguardKnight.java
 *  
 *  This class implements the Zeal and provoke interface, reacts when a unit's owning avatar
 *  is attacked (zeal).
 *  
 */
public class SilverguardKnight extends Unit implements Zeal, Provoke {

	final int updateAttack = 2;
	@Override
	public void applyZeal(ActorRef out) {
		// TODO Auto-generated method stub
		
		// Will be called when the owning player's (AI's) avatar
		// takes the damage
		// Should increase the unit's attack +2 permanently
		this.setAttack(this.getAttack() + updateAttack);
		
		// Update attack in front-end
		BasicCommands.setUnitAttack(out, this, this.getAttack());
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
	}
}
