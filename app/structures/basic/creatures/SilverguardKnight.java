package structures.basic.creatures;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Unit;

public class SilverguardKnight extends Unit implements Zeal, Provoke {

	final int updateAttack = 2;
	@Override
	public void applyZeal(ActorRef out) {
		// TODO Auto-generated method stub
		
		// Will be called when the owning player's (AI) avatar
		// takes the damage
		// Should increase the unit's attack +2 permanently
		this.setAttack(this.getAttack() + updateAttack);
		
		BasicCommands.setUnitAttack(out, this, this.getAttack());
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
	}
}
