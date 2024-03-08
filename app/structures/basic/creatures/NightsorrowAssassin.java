package structures.basic.creatures;

import java.util.List;

import akka.actor.ActorRef;
import commands.BasicCommands;
import gamelogic.Actions;
import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.TilesGenerator;


/*
 *  NightsorrowAssassins.java
 *  
 *  This class implements the OpeningGambit interface, triggers an effect when this unit
 *  is summoned on to the board.
 *  
 */

public class NightsorrowAssassin extends Unit implements OpeningGambit {

	/*
	 *  Effects to be triggered when this unit is summoned
	 */
	@Override
	public void reactToUnitsSummon(ActorRef out, Tile tile) {
		// TODO Auto-generated method stub
		Unit target = tile.getUnit();
		GameState gameState = GameState.getInstance();
		
		// Access any enemy unit in an adjacent tile to this unit 
		// and if it is less than its maximum health, destroy it.
		List<Tile> enemyUnitsInAdjacentTiles = TilesGenerator.getAdjacentEnemyTiles(target);

		// Eliminate enemy avatar
		Unit avatar = null;
		if (gameState.isCurrentPlayerHuman()) {
			avatar = gameState.getAIPlayer().getAvatar();
		} else {
			avatar = gameState.getHumanPlayer().getAvatar();
		}
		// Except avatar other units can be destroyed when it is summoned
		enemyUnitsInAdjacentTiles.removeAll(TilesGenerator.getAvatarTile(avatar));
		
		// Actual effect
		if (!enemyUnitsInAdjacentTiles.isEmpty()) {
			for(Tile myTile: enemyUnitsInAdjacentTiles) {
				if (myTile.getUnit().getHealth() < myTile.getUnit().getMaximumHealth()) {
					// Can also have some logic.
					// If a provoke unit or any other lethal unit kill it <TO DO>
					
					// For now just choose the first one
					myTile.getUnit().setHealth(0);
					
					// Front-end communication
					// Update target's health
			        BasicCommands.setUnitHealth(out, myTile.getUnit(), Math.max(myTile.getUnit().getHealth(),0));
			        try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
					
					Actions.unitDeathAction(out, myTile.getUnit());
					break;
				}
			}
		}
	}

}
