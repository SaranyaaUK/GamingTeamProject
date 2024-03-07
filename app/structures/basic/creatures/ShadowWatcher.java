package structures.basic.creatures;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Unit;

public class ShadowWatcher extends Unit implements DeathWatch {

	@Override
	public void reactToDeath(ActorRef out) {
		// TODO Auto-generated method stub
		
		GameState gameState = GameState.getInstance();
		
		// The unit gains +1 attack and +1 health permanently
		// (Cannot increase the creature's maximum health)
		// Add any method that is needed
		this.setAttack(this.getAttack() + 1);
		this.setHealth(Math.min(this.getMaximumHealth(), this.getHealth() + 1));
		
		// Update in front-end
		BasicCommands.setUnitAttack(out, this, this.getAttack());
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		
		BasicCommands.setUnitHealth(out, this, this.getHealth());
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
	}

}
