package structures.basic.creatures;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Unit;

public class ShadowDancer extends Unit implements DeathWatch {

	@Override
	public void reactToDeath(ActorRef out) {
		// TODO Auto-generated method stub

		GameState gameState = GameState.getInstance();
		
		// Access the enemy avatar and deal one 1 damage to it and 
		// increase this unit's health to 1
		Unit avatar = null;
		if (gameState.isCurrentPlayerHuman()) {
			avatar = gameState.getAIPlayer().getAvatar();
		} else {
			avatar = gameState.getHumanPlayer().getAvatar(); 
		}
		
		// Damage opponent avatar
		avatar.setHealth(avatar.getHealth()-1);
		BasicCommands.setUnitHealth(out, avatar, avatar.getHealth());
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		// Heal myself
		this.setHealth(this.getHealth()+1);
		BasicCommands.setUnitHealth(out, this, this.getHealth());
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
	}

}
