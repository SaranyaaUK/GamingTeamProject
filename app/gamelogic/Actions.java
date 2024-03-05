package gamelogic;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Card;
import structures.basic.CardManager;
import structures.basic.EffectAnimation;
import structures.basic.GameLogic;
import structures.basic.Grid;
import structures.basic.Player;
import structures.basic.Position;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import structures.basic.creatures.OpeningGambit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;
import utils.TilesGenerator;

import java.util.Collection;
import java.util.List;

public class Actions {
	public static void resetWithinOneTurn(ActorRef out) {
		GameState gameState = GameState.getInstance();
		if (!gameState.endTurnClicked && GameState.wraithlingSummonStatus()) {
			return;
		}
		BasicCommands.dehighlightTiles(out);

		gameState.setHandPosition(-1);
		gameState.setCurrentUnit(null);
		gameState.setSpellToCast(null);
		gameState.getHighlightedFriendlyTiles().clear();
		gameState.getHighlightedEnemyTiles().clear();
	}

	//reset all the unit's isExhausted/isMoved
	//remove all the highlight
	//reset handPosition
	//reset current player's Mana to 0
	public static void resetAfterOneTurn(ActorRef out) {

		GameState gameState = GameState.getInstance();
		
		if (GameState.wraithlingSummonStatus()) {
			GameLogic.updateHandCardsView(out);
		}
		// Determine current player and next player
		Player currentPlayer = gameState.getCurrentPlayer();
		Player nextPlayer;

		// Clear current player's mana
		currentPlayer.setMana(0);
		Actions.resetWithinOneTurn(out);

		// CurrentPlayer's units need reset
		for(Unit unit: currentPlayer.getMyUnits()) {
			resetUnitStatus(unit);
		}

		// Draw a card from deck
		if (currentPlayer.isMyDeckEmpty()) {
			BasicCommands.addPlayer1Notification(out, "Game Over", 100);
		} else {
			currentPlayer.getCardManager().drawCardFromDeck(1);
		}

		if (gameState.isCurrentPlayerHuman()) {
			BasicCommands.drawHandCards(gameState.getHumanPlayer().getMyHandCards(), out, 0); // pass the mode
		}

		// Now based on the current player, get the next player and also set
		// their mana in front-end
		if (gameState.isCurrentPlayerHuman()) {
			BasicCommands.setPlayer1Mana(out, currentPlayer);
			nextPlayer = gameState.getAIPlayer();
		} else {
			BasicCommands.setPlayer2Mana(out, currentPlayer);
			nextPlayer = gameState.getHumanPlayer();
			// Turn will be increased if the next player is a Human player
			gameState.nextTurn();
		}

		// Now switch players
		// looks similar to one in Initialise can have a common method (re check)
		gameState.setCurrentPlayer(nextPlayer);
		updateCurrentPlayerMana(out, gameState.getTurn() + 1);
	}

	public static void updateCurrentPlayerMana(ActorRef out, int mana) {
		GameState gameState = GameState.getInstance();
		gameState.getCurrentPlayer().setMana(mana);

		if (gameState.isCurrentPlayerHuman()) {
			BasicCommands.setPlayer1Mana(out, gameState.getCurrentPlayer());
		} else {
			BasicCommands.setPlayer2Mana(out, gameState.getCurrentPlayer());	
		}
	}

	private static void resetUnitStatus(Unit unit) {
		// All unit's isMoved and isAttacked need to be set to false,
		// except for stunned unit.
		if (unit.isStunned()) {
			unit.setExhausted(true);
			unit.setStunned(false);
		} else {
			unit.setExhausted(false);
			unit.setMoved(false);
		}
	}

	//modify the state of unit
	//modify the tile
	//move animation
	//gamelogic.associateWithTile
	public static void unitMove(ActorRef out, Unit unit, Tile tile) {
		// Check if Vertical movement is necessary
		BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.move);
		try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
		if (checkDiagonalMovement(unit, tile)) {
			BasicCommands.moveUnitToTile(out, unit, tile, true);
			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
		}
		else {
			BasicCommands.moveUnitToTile(out, unit, tile); //move unit to chosen tiles
			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
		}
		BasicCommands.dehighlightTiles(out);
		unit.setMoved(true);
		GameLogic.associateUnitWithTile(unit, tile);
	}

	/*
	 *  CheckVerticalMovement
	 */
	public static Boolean checkVerticalMovement(Unit unit) {
		GameState gameState = GameState.getInstance();
		Grid myGrid = gameState.getGrid();
		
		int unitx = unit.getPosition().getTilex();
		int unity = unit.getPosition().getTiley();
		if (TilesGenerator.isValidTile(unitx + 1, unity) || TilesGenerator.isValidTile(unitx - 1, unity))
			return false;
		
		Player opponentPlayer = null;
		if (gameState.isCurrentPlayerHuman()) {
			opponentPlayer = gameState.getAIPlayer();
		} else {
			opponentPlayer = gameState.getHumanPlayer();
		}
		if (myGrid.getTile(unitx+1, unity).getUnit() != null && opponentPlayer.getMyUnits().contains(myGrid.getTile(unitx+1, unity).getUnit())) {
			return true;
		}
		if (myGrid.getTile(unitx-1, unity).getUnit() != null && opponentPlayer.getMyUnits().contains(myGrid.getTile(unitx-1, unity).getUnit())) {
			return true;
		}
		return false;

	}
	
	/*
	 *  CheckDiagonalMovement
	 */
	public static boolean checkDiagonalMovement(Unit unit, Tile tile) {	    	
		int unitx = unit.getPosition().getTilex();
		int unity = unit.getPosition().getTiley();

		// Check if we are moving to diagonal tiles
		if (Math.abs(unitx - tile.getTilex())==1 && Math.abs(unity - tile.getTiley())==1 ) {
			if(checkVerticalMovement(unit))
				return true;
			else 
				return false;
		}
		return false;
	}

	//check if the target if within the attack distance,if not, move first.
	//then calculate and update the health of target
	// do the counter-attack
    // check if avatar dies, is so, end game
    // check if any unit dies, if so, invoke DeathWatch
    // unit attack animation
    // target hit animation
	public static void unitAttack(ActorRef ref, Unit unit, Unit target) {
		if (isAdjacent(unit,target)) {
			// If target is just adjacent, just attack
			unitAdjacentAttack(ref,unit,target);
		}else if(!isAdjacent(unit,target)){
			// Else move and then attack
			unitMoveAttack(ref,unit,target);
		}
	}

    // Helper method to check if two units are adjacent to each other
    private static boolean isAdjacent(Unit unit1, Unit unit2) {
        // Assuming units are adjacent if they share the same X or Y coordinate
        return Math.abs(unit1.getPosition().getTilex() - unit2.getPosition().getTilex()) == 1 ||
                Math.abs(unit1.getPosition().getTiley() - unit2.getPosition().getTiley()) == 1;
    }

    /*
     *  UnitAdjacentAttack
     */
    public static void unitAdjacentAttack(ActorRef ref, Unit unit, Unit target){
    	//enemy unit get damage
    	int damageToTarget = unit.getAttack();
    	int updatedTargetHealth =  Math.max(target.getHealth() - damageToTarget, 0);
    	target.setHealth(updatedTargetHealth);

    	// Unit attack actions
    	unitAttackAction(ref, unit, target);

    	//if target is still alive then counter-attack
    	if (updatedTargetHealth > 0){
    		counterAttack(ref, target, unit);
    	}

    	if (target.getHealth() == 0) {
    		unitDeathAction(ref, target);
    	}
    	//set status
    	unit.setExhausted(true);
    }

    /*
     *  CounterAttack
     */
    public static void counterAttack(ActorRef ref, Unit unit, Unit target){
    	int damageToTarget = unit.getAttack();
    	int updatedTargetHealth = Math.max(target.getHealth() - damageToTarget, 0);
    	target.setHealth(updatedTargetHealth);
    	
    	// Unit attack actions
    	unitAttackAction(ref, unit, target);

    	//If died after counter-attack
    	if(target.getHealth() == 0){
    		unitDeathAction(ref, target);
    	}
    }

    /*
     *  UnitAttackAction
     */
    public static void unitAttackAction(ActorRef ref, Unit unit, Unit target) {
    	//attack animation
        BasicCommands.playUnitAnimation(ref, unit, UnitAnimationType.attack);
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}

        // Update target's health
        BasicCommands.setUnitHealth(ref, target, target.getHealth());
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        
        // Target plays the hit animation
        BasicCommands.playUnitAnimation(ref,target,UnitAnimationType.hit);
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        
        // Check if avatar is attacked to trigger respective actions
        GameLogic.reactToAvatarAttack(ref, target);
    }

    /*
     *  UnitDeathAction
     */
    public static void unitDeathAction(ActorRef ref, Unit target) {
    	// Target plays the death animation
    	BasicCommands.playUnitAnimation(ref,target,UnitAnimationType.death);
    	try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}

    	// Remove Unit 
    	if(!target.isHumanUnit()) {
    		Tile tile = GameState.getInstance().getGrid().getTile(target.getPosition().getTilex(), target.getPosition().getTiley());
    		tile.setUnit(null);
    		GameState.getInstance().getAIPlayer().getMyUnits().remove(target);
    	} else {
    		Tile tile = GameState.getInstance().getGrid().getTile(target.getPosition().getTilex(), target.getPosition().getTiley());
    		tile.setUnit(null);
    		GameState.getInstance().getHumanPlayer().getMyUnits().remove(target);
    	}

    	BasicCommands.deleteUnit(ref,target);
    	try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}

    	// Check for End game - avatars dying
    	int aiAvatarID = GameState.getInstance().getAIPlayer().getAvatarID();
		int humanAvatarID = GameState.getInstance().getHumanPlayer().getAvatarID();
    	if (target.getId() == aiAvatarID || target.getId() == humanAvatarID) {
    		// We should maintain some state in GameState--- to not allow other actions after end turn
    		// <TO DO>
    		BasicCommands.addPlayer1Notification(ref, "Game Over", 100);
		}

    	// Notify DeathWatchers
    	GameLogic.notifyDeathWatchers(ref);
    }

    /*
     *  UnitMoveAction
     */
    public static void unitMoveAttack(ActorRef ref, Unit unit, Unit target){
        // Move unit to a position adjacent to the target unit
        Actions.moveToAdjacentPosition(ref, unit, target.getPosition());
        //wait for move
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        // Perform attack after moving
        unitAdjacentAttack(ref, unit, target);
    }

    // Move the unit to the adjacent position
    public static void moveToAdjacentPosition(ActorRef ref,Unit unit, Position targetPosition){
        Tile adjacentPositionTile = findEmptyAdjacentTile(unit, targetPosition);
        unitMove(ref,unit,adjacentPositionTile);
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
    }

    /*
     *  FindEmptyAdjacentTile for move and attack
     */
    public static Tile findEmptyAdjacentTile(Unit unit, Position target){
    	
    	GameState gameState = GameState.getInstance();
    	// If the the target is forward or behind the unit
    	int unitx = unit.getPosition().getTilex();
    	int targetx = target.getTilex();
    	
    	Integer[][] offsets = new Integer[4][4];
    	if ((unitx-targetx <  1)) {
    		if (gameState.isCurrentPlayerHuman()) {
    			// Target is in front
    			// Define the relative offsets for adjacent positions
    	        Integer[][] humanoffsets = {{-1, 0}, {-1, -1},{-1, 1}}; // adjacent tiles
    	        offsets = humanoffsets;
    		} else {
    			// Define the relative offsets for adjacent positions
    	        Integer[][] aioffsets = {{1, 0}, {1, -1}, {1, 1}}; // adjacent tiles
    	        offsets = aioffsets;
    		}
    	} else {
    		if (gameState.isCurrentPlayerHuman()) {
    			// Define the relative offsets for adjacent positions
    	        Integer[][] humanoffsets = {{1, 0}, {1, -1}, {1, 1}}; // adjacent tiles
    	        offsets = humanoffsets;
    		} else {
    			// Define the relative offsets for adjacent positions
    	        Integer[][] aioffsets = {{-1, 0}, {-1, -1},{-1, 1}}; // adjacent tiles
    	        offsets = aioffsets;
    		}
    	}

        // Iterate through the offsets to check adjacent positions
        for (Integer[] offset : offsets) {
            int adjacentX = target.getTilex() + offset[0];
            int adjacentY = target.getTiley() + offset[1];
            if (TilesGenerator.isValidTile(adjacentX, adjacentY)) {
                // Get the tile corresponding to the adjacent position
                Tile adjacentTile = GameState.getInstance().getGrid().getTile(adjacentX, adjacentY);
                // Check if the tile is empty (not occupied by any unit)
                if (adjacentTile != null && adjacentTile.getUnit() == null) {
                    return adjacentTile;
                }
            }
        }
        return null; // No empty adjacent tile found
    }
    
	//create the unit first
	//modify the tile and unit
	//check if it is the OpeningGambits
	//call repositionHandCards
	//delete one card in hand
	public static void placeUnit(ActorRef ref, Tile tile) {
		
		GameState gameState = GameState.getInstance();
		// Get clicked card
		Card card = gameState.getClickedCard();
		
		// Create Unit and associate it with the tile
		Unit unit = GameLogic.getCreatureObject(card);
		gameState.getCurrentPlayer().addUnits(unit);
		GameLogic.associateUnitWithTile(unit, tile);
		
		if (unit instanceof OpeningGambit) {
			// OpeningGambits when they summoned they trigger some effects
			((OpeningGambit) unit).reactToUnitsSummon();
		}

		// Do the front-end communication
		BasicCommands.drawUnit(ref, unit, tile);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		
		EffectAnimation summonEffect = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon);
		BasicCommands.playEffectAnimation(ref, summonEffect, tile);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		
		BasicCommands.setUnitAttack(ref, unit, unit.getAttack());
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		BasicCommands.setUnitHealth(ref, unit, unit.getHealth());
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
	}


}
