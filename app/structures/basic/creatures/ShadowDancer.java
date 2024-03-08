package structures.basic.creatures;

import akka.actor.ActorRef;
import commands.BasicCommands;
import gamelogic.Actions;
import structures.GameState;
import structures.basic.Player;
import structures.basic.Unit;

/*
 *  ShadowDancer.java
 *  
 *  This class implements the Death interface, reacts when a unit dies on the board
 *  
 */

public class ShadowDancer extends Unit implements DeathWatch {

	/*
	 *  Reacts to death of any unit on the board
	 */
	@Override
	public void reactToDeath(ActorRef out) {
		// TODO Auto-generated method stub

		GameState gameState = GameState.getInstance();
		
		// Access the enemy avatar and deal one 1 damage to it and 
		// increase this unit's health to 1
		Unit avatar = null;
		Player player = null;
		if (gameState.isCurrentPlayerHuman()) {
			player = gameState.getAIPlayer();			
		} else {
			player = gameState.getHumanPlayer();
		}
		
		// Get the avatar
		avatar = player.getAvatar();
		int updatedHealth = avatar.getHealth()-1;

		// Damage opponent avatar
		avatar.setHealth(updatedHealth);
		BasicCommands.setUnitHealth(out, avatar, avatar.getHealth());
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		
		// Update player's health in front-end
		player.setHealth(updatedHealth);
		BasicCommands.setUnitHealth(out, avatar, avatar.getHealth());
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		//If avatar dies
    	if(avatar.getHealth() == 0){
    		Actions.unitDeathAction(out, avatar);
    	}

		// Heal myself
		this.setHealth(this.getHealth()+1);
		BasicCommands.setUnitHealth(out, this, this.getHealth());
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
	}

}
