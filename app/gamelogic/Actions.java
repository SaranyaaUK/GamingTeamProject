package gamelogic;

import java.util.List;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.*;
import structures.basic.creatures.*;
import utils.*;

/**
 * Actions.java
 * 
 * This class implements actions that follows after different events
 * 
 */
public class Actions {

	/**
	 * ResetWithinOneTurn
	 * 
	 * Reset actions within one turn
	 * 
	 * @param out(ActorRef)
	 */
	public static void resetWithinOneTurn(ActorRef out) {
		GameState gameState = GameState.getInstance();
		if (!gameState.isEndTurnClicked() && GameLogic.wraithlingSummonStatus()) {
			return;
		}
		BasicCommands.dehighlightTiles(out);
		gameState.setHandPosition(-1);
		gameState.setCurrentUnit(null);
		gameState.setSpellToCast(null);
		gameState.getHighlightedFriendlyTiles().clear();
		gameState.getHighlightedEnemyTiles().clear();
	}

	/**
	 * ResetAfterOneTurn
	 * 
	 * Reset actions within one turn
	 *
	 * reset all the unit's isExhausted/isMoved
	 * remove all the highlight
	 * reset handPosition
	 * reset current player's Mana to 0
	 * 
	 * @param out (ActorRef)
	 * 
	 */
	public static void resetAfterOneTurn(ActorRef out) {

		GameState gameState = GameState.getInstance();

		if (GameLogic.wraithlingSummonStatus()) {
			GameLogic.updateHandCardsView(out);
		}
		// Determine current player and next player
		Player currentPlayer = gameState.getCurrentPlayer();
		Player nextPlayer;

		// Clear current player's mana
		currentPlayer.setMana(0);

		// Do other resets - Similar to resetting within one turn
		Actions.resetWithinOneTurn(out);

		// CurrentPlayer's units need reset
		for (Unit unit : currentPlayer.getMyUnits()) {
			resetUnitStatus(unit);
		}

		// Draw a card from deck
		if (currentPlayer.isMyDeckEmpty()) {
			gameState.setGameEnded(true);
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
		GameLogic.updateCurrentPlayerMana(out, Math.min(gameState.getTurn() + 1, GameState.MAXIMUM_MANA));
	}

	/**
	 * ResetUnitStatus
	 * 
	 * After one turn set all units isMoved and isExhausted attributes
	 * if it is not stunned.
	 * 
	 * @param unit (Unit)
	 * 
	 */
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

	/**
	 * UnitMove
	 *
	 * modify the state of unit
	 * modify the tile
	 * move animation
	 * gamelogic.associateWithTile
	 *
	 * @param out  (ActorRef)
	 * @param unit (Unit)
	 * @param tile (Tile)
	 * 
	 */
	public static void unitMove(ActorRef out, Unit unit, Tile tile) {
		// Check if Vertical movement is necessary
		BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.move);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		if (checkDiagonalMovement(unit, tile)) {
			BasicCommands.moveUnitToTile(out, unit, tile, true);
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		} else {
			BasicCommands.moveUnitToTile(out, unit, tile); // move unit to chosen tiles
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
		}
		BasicCommands.dehighlightTiles(out);
		unit.setMoved(true);
		GameLogic.associateUnitWithTile(unit, tile);

		// Set the animation back to idle after move is done
		BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.idle);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
	}

	/**
	 * CheckVerticalMovement
	 * 
	 * @param unit (Unit)
	 * 
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
		if (myGrid.getTile(unitx + 1, unity).getUnit() != null
				&& opponentPlayer.getMyUnits().contains(myGrid.getTile(unitx + 1, unity).getUnit())) {
			return true;
		}
		if (myGrid.getTile(unitx - 1, unity).getUnit() != null
				&& opponentPlayer.getMyUnits().contains(myGrid.getTile(unitx - 1, unity).getUnit())) {
			return true;
		}
		return false;

	}

	/**
	 * CheckDiagonalMovement
	 * 
	 * Check if we have to move to a diagonal tile
	 * 
	 * @param unit (Unit)
	 * @param tile (Tile)
	 * 
	 */
	public static boolean checkDiagonalMovement(Unit unit, Tile tile) {
		int unitx = unit.getPosition().getTilex();
		int unity = unit.getPosition().getTiley();
		// Check if we are moving to diagonal tiles
		if (Math.abs(unitx - tile.getTilex()) == 1 && Math.abs(unity - tile.getTiley()) == 1) {
			// Check if vertical movement is needed first
			if (checkVerticalMovement(unit)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * UnitAttack
	 * 
	 * check if the target is within the attack distance, if not, move first.
	 * then calculate and update the health of target
	 * do the counter-attack, if the unit survives
	 * check for avatar attack actions
	 * check if avatar dies, if so, end game
	 * check if any unit dies, if so, notify DeathWatchers
	 * unit attack animation
	 * target hit animation, repeat for counter attack
	 * idle animation after we hit/attack
	 * 
	 * @param ref    (ActorRef)
	 * @param unit   (Unit)
	 * @param target (Unit)
	 * 
	 */

	public static void unitAttack(ActorRef ref, Unit unit, Unit target) {
		if (isAdjacent(unit, target)) {
			System.out.println("Unit Adjacent attack");
			// If target is just adjacent, just attack
			unitAdjacentAttack(ref, unit, target);
		} else {
			System.out.println("Unit move and attack");
			// Else move and then attack
			unitMoveAttack(ref, unit, target);
		}

		// Set the units animation to idle after attack
		BasicCommands.playUnitAnimation(ref, unit, UnitAnimationType.idle);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		BasicCommands.playUnitAnimation(ref, target, UnitAnimationType.idle);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * isAdjacent
	 * 
	 * @param unit1 (Unit)
	 * @param unit2 (Unit)
	 * @return true (Boolean) if two units are adjacent to each other
	 * 
	 */
	private static boolean isAdjacent(Unit unit1, Unit unit2) {
		// Check the distance between the unit's tiles
		return ((Math.abs(unit1.getPosition().getTilex() - unit2.getPosition().getTilex()) == 1
				&& unit1.getPosition().getTiley() == unit1.getPosition().getTiley()) ||
				(Math.abs(unit1.getPosition().getTiley() - unit2.getPosition().getTiley()) == 1
						&& unit1.getPosition().getTilex() == unit2.getPosition().getTilex()));

	}

	/**
	 * UnitAdjacentAttack
	 * 
	 * @param ref    (ActorRef)
	 * @param unit   (Unit)
	 * @param target (Unit)
	 * 
	 */
	public static void unitAdjacentAttack(ActorRef ref, Unit unit, Unit target) {

		// enemy unit get damage
		int damageToTarget = unit.getAttack();

		int updatedTargetHealth = Math.max(target.getHealth() - damageToTarget, 0);
		target.setHealth(updatedTargetHealth);

		// Unit attack actions
		unitAttackAction(ref, unit, target);

		// if target is still alive then counter-attack
		if (target.getHealth() > 0) {
			// Should check if the unit (the new defender)
			// is in adjacent position to the target (new attacker)
			// if not the counter attack does not take place
			if (isAdjacent(target, unit)) {
				counterAttack(ref, target, unit);
			}
		} else if (target.getHealth() == 0) {
			unitDeathAction(ref, target);
		}
		// Update exhaust status
		unit.setExhausted(true);
	}

	/**
	 * CounterAttack
	 * 
	 * @param ref    (ActorRef)
	 * @param unit   (Unit)
	 * @param target (Unit)
	 * 
	 */
	public static void counterAttack(ActorRef ref, Unit unit, Unit target) {
		int damageToTarget = unit.getAttack();
		int updatedTargetHealth = Math.max(target.getHealth() - damageToTarget, 0);
		target.setHealth(updatedTargetHealth);

		// Unit attack actions
		unitAttackAction(ref, unit, target);

		// If died after counter-attack
		if (target.getHealth() == 0) {
			unitDeathAction(ref, target);
		}
	}

	/**
	 * UnitAttackAction
	 * 
	 * @param ref    (ActorRef)
	 * @param unit   (Unit)
	 * @param target (Unit)
	 * 
	 */
	public static void unitAttackAction(ActorRef ref, Unit unit, Unit target) {
		// Front-end communication for unit attack
		BasicCommands.attackUnit(ref, unit, target);

		// Check if avatar is attacked to trigger respective actions
		GameLogic.reactToAvatarAttack(ref, target);

		// Check if the avatar attacking
		// Check the attacking unit (if it is avatar) - To trigger horn of Forsaken hit
		// effect
		GameLogic.reactToAvatarHit(ref, unit, target);
	}

	/**
	 * UnitDeathAction
	 * 
	 * @param ref    (ActorRef)
	 * @param target (Unit)
	 * 
	 */
	public static void unitDeathAction(ActorRef ref, Unit target) {
		// Target plays the death animation
		BasicCommands.playUnitAnimation(ref, target, UnitAnimationType.death);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		// Remove Unit
		if (!target.isHumanUnit()) {
			Tile tile = GameState.getInstance().getGrid().getTile(target.getPosition().getTilex(),
					target.getPosition().getTiley());
			tile.setUnit(null);
			GameState.getInstance().getAIPlayer().getMyUnits().remove(target);
		} else {
			Tile tile = GameState.getInstance().getGrid().getTile(target.getPosition().getTilex(),
					target.getPosition().getTiley());
			tile.setUnit(null);
			GameState.getInstance().getHumanPlayer().getMyUnits().remove(target);
		}

		BasicCommands.deleteUnit(ref, target);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		// Check for End game - avatars dying
		int aiAvatarID = GameState.getInstance().getAIPlayer().getAvatarID();
		int humanAvatarID = GameState.getInstance().getHumanPlayer().getAvatarID();
		if (target.getId() == aiAvatarID || target.getId() == humanAvatarID) {
			GameState.getInstance().setGameEnded(true);
			BasicCommands.addPlayer1Notification(ref, "Game Over", 100);
			return;
		}

		// Notify DeathWatchers
		GameLogic.notifyDeathWatchers(ref);
	}

	/**
	 * UnitMoveAction
	 * 
	 * @param ref    (ActorRef)
	 * @param unit   (Unit)
	 * @param target (Unit)
	 * 
	 */
	public static void unitMoveAttack(ActorRef ref, Unit unit, Unit target) {
		// Move unit to a position adjacent to the target unit
		Actions.moveToAdjacentPosition(ref, unit, TilesGenerator.getUnitTile(target));
		// Perform attack after moving
		unitAdjacentAttack(ref, unit, target);
	}

	/**
	 * MoveToAdjacentPosition
	 * 
	 * @param ref          (ActorRef)
	 * @param unit         (Unit)
	 * @param toAttackTile (Tile)
	 * 
	 */
	public static void moveToAdjacentPosition(ActorRef ref, Unit unit, Tile toAttackTile) {
		System.out.println("moveToAdjacentPosition");
		List<Tile> myMovableTiles = TilesGenerator.getMovableTiles(unit);
		List<Tile> getAdjacentTiles = TilesGenerator.getAdjacentTiles(toAttackTile);
		// Find possible tile to move to attack
		myMovableTiles.retainAll(getAdjacentTiles);

		// Move to one of the possible tiles
		for (Tile tile : myMovableTiles) {
			System.out.println("move to Tile");
			unitMove(ref, unit, tile);
			try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
			break;
		}
	}

	/**
	 * PlaceUnit
	 * 
	 * create the unit first
	 * modify the tile and unit
	 * check if it is the OpeningGambits
	 * 
	 * @param ref  (ActorRef)
	 * @param tile (Tile)
	 * 
	 */
	public static void placeUnit(ActorRef ref, Tile tile) {
		GameState gameState = GameState.getInstance();
		// Get clicked card
		Card card = gameState.getClickedCard();

		// Create Unit and associate it with the tile
		Unit unit = GameLogic.getCreatureObject(card);

		// Add unit to the player's allied units
		gameState.getCurrentPlayer().addUnits(unit);
		GameLogic.associateUnitWithTile(unit, tile);

		// Do the front-end communication
		EffectAnimation summonEffect = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon);
		BasicCommands.placeUnit(ref, unit, tile, summonEffect);

		if (unit instanceof OpeningGambit) {
			// OpeningGambits when they summoned they trigger some effects
			((OpeningGambit) unit).reactToUnitsSummon(ref, tile);
		}
	}

	/**
	 * PlaceWraithlings
	 * 
	 * @param ref  (ActorRef)
	 * @param tile (Tile)
	 * 
	 */
	public static void placeWraithling(ActorRef ref, Tile tile) {
		// Create wraithlings
		Unit unit = Wraithlings.createWraithling();

		// Add wraithling to the player's allied units
		GameState.getInstance().getCurrentPlayer().addUnits(unit);
		GameLogic.associateUnitWithTile(unit, tile);

		// Do the front-end communication
		EffectAnimation summonEffect = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_warithsummon);
		BasicCommands.placeUnit(ref, unit, tile, summonEffect);
	}

}
