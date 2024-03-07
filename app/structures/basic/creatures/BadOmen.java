package structures.basic.creatures;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Unit;

public class BadOmen extends Unit implements DeathWatch {

	@Override
	public void reactToDeath(ActorRef out) {
		// The unit gains +1 attack permanently
		this.setAttack(this.getAttack()+1);
		BasicCommands.setUnitAttack(out, this, this.getAttack());
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
	}

}
