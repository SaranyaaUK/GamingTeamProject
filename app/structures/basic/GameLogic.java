package structures.basic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import akka.actor.ActorRef;
import commands.BasicCommands;
import gamelogic.Actions;
import structures.GameState;
import utils.*;
import structures.basic.creatures.*;
import structures.basic.spell.*;


/**
 *  GameLogic.java
 * 
 *  This class holds basic logics required for the game
 *
 */

public class GameLogic {

	/**
	 *  associateUnitWithTile
	 *  
	 *  Method to associate a unit with the tile, when a unit is created 
	 *  and we have it's tile position this method can be invoked 
	 *  It take the tile's position and also set the tile's unit attribute
	 *  accordingly.
	 *  
	 *   @param unit (Unit)
	 *   @param tile (Tile)
	 *  
	 */
	public static void associateUnitWithTile(Unit unit, Tile tile) {
		// Update the Unit's position to match the Tile's coordinates
		unit.setPositionByTile(tile);
		// Set the Unit on the Tile
		tile.setUnit(unit);
	}

	/**
	 *  HighlightAfterCardClick
	 *  
	 *  Events that takes place after a card is clicked
	 *  
	 *  @param ref (ActorRef)
	 *  
	 */
	public static void highlightAfterCardClick(ActorRef out) {

		// First clear any previous highlighting
		clearPreviousCardClickHighlights(out);

		// Now highlight the clicked card
		BasicCommands.highlightClickedCard(out);

		// Target Tiles Highlighting based on the clicked card
		cardClickTileHighlights(out);
	}


	/**
	 *  clearPreviousCardClickHighlights
	 *  
	 *  @param ref (ActorRef)
	 *  
	 */
	private static void clearPreviousCardClickHighlights(ActorRef out) {

		GameState gameState = GameState.getInstance();
		BasicCommands.dehighlightCards(out);
		BasicCommands.dehighlightTiles(out);
		// Update gameState after you de-highlight (can move to some reset method ?)
		gameState.getHighlightedFriendlyTiles().clear();
		gameState.getHighlightedEnemyTiles().clear();
	}

	/**
	 * 
	 *  cardClickTileHighlights
	 * 
	 *  @param ref (ActorRef)
	 * 
	 */
	public static void cardClickTileHighlights(ActorRef out) {

		GameState gameState = GameState.getInstance();
		Card clickedCard = gameState.getClickedCard();
		if (gameState.isCardClicked()) {
			if (clickedCard.isCreature()) {
				gameState.setSpellToCast(null);
				// Creature cards - We only summon here
				List<Tile> tilesToHighlight = TilesGenerator.getTilesToSummon();
				gameState.setHighlightedFriendlyTiles(tilesToHighlight);
			} else {
				// Spell cards

				// Instantiate the appropriate spell object
				// Now get the target tiles to highlight
				// Call drawTile with highlight mode.
				Spell mySpellInstance = getSpellObject(clickedCard.getId());
				gameState.setSpellToCast(mySpellInstance);

				mySpellInstance.getTargetTilesToHighlight();		
			}
		}
		BasicCommands.highlightTiles(out);
	}

	/**
	 *  notifyDeathWatchers
	 *  
	 *  Used to notify all death watchers regarding a unit's death
	 *  
	 *  @param ref (ActorRef)
	 * 
	 */
	public static void notifyDeathWatchers(ActorRef out) {
		// Only Human units have DeathWatch ability
		GameState gameState = GameState.getInstance();
		List<Unit> units = new ArrayList<>(gameState.getHumanPlayer().getMyUnits());

		Iterator<Unit> iterator = units.iterator();
		while (iterator.hasNext()) {
			Unit unit = iterator.next();
			if (unit instanceof DeathWatch) {
				((DeathWatch) unit).reactToDeath(out);
			}
		}
	}

	/**
	 *  reactToAvatarAttack
	 *  
	 *  Implement the reaction when avatars are attacked
	 *  
	 *  @param ref (ActorRef)
	 *  @param unit (Unit)
	 *  @param target (Unit)
	 *  
	 */
	public static void reactToAvatarAttack(ActorRef out, Unit avatar) {

		GameState gameState = GameState.getInstance();
		int aiAvatarID = gameState.getAIPlayer().getAvatarID();
		int humanAvatarID = gameState.getHumanPlayer().getAvatarID();

		if(!(avatar.getId() == aiAvatarID || avatar.getId() == humanAvatarID)) {
			return;
		}

		if (avatar.getId() == humanAvatarID) {
			// If Human avatar has the artifact (from Horn Of Forsaken Spell)
			// then decrement it
			Unit humanAvatar = gameState.getHumanPlayer().getAvatar();

			if (humanAvatar.getArtifact() > 0) {
				humanAvatar.setArtifact(humanAvatar.getArtifact() - 1);
			}

		} else {
			// If AI avatar takes attack, units with Zeal power reacts
			List<Unit> aiUnits = gameState.getAIPlayer().getMyUnits();
			for (Unit unit: aiUnits) {
				if (gameState.getAIPlayer() instanceof Zeal) {
					((Zeal) unit).applyZeal(out);
				}
			}

		}

	}

	/**
	 *  reactToAvatarHit
	 *  
	 *  @param ref (ActorRef)
	 *  @param unit (Unit)
	 *  @param target (Unit)
	 *  
	 */
	public static void reactToAvatarHit(ActorRef out, Unit unit, Unit target) {
		GameState gameState = GameState.getInstance();
		Unit avatar = gameState.getHumanPlayer().getAvatar();
		if (!(unit.getId() == gameState.getHumanPlayer().getAvatarID())) {
			return;
		}
		
		if (avatar.getArtifact() <= 0) {
			avatar.setArtifact(- 1);
			return;
		}

		// If it is human player's avatar that is attacking do the below 
		// (place wraithlings around the avatar)
		Tile targetTile = TilesGenerator.getUnitTile(avatar);

		List<Tile> adjacentTiles = TilesGenerator.getAdjacentTiles(targetTile);

		for (Tile tile: adjacentTiles) {
			// Can add some randomness - can try later <TO DO>
			// For now just find the first available tile
			if (tile.getUnit() == null) {
				Actions.placeWraithling(out, tile);
				break;
			}
		}
	}

	/**
	 *  updateHandCardsView
	 *  
	 *   @param out (ActorRef)
	 *  
	 */
	public static void updateHandCardsView(ActorRef out) {

		GameState gameState = GameState.getInstance();
		// Delete in front-end
		CardManager myCardManager = gameState.getCurrentPlayer().getCardManager();
		BasicCommands.deleteHandCards(myCardManager.getHandCards(), out);
		// Update gameState
		myCardManager.deleteHandCardAt(gameState.getHandPosition()-1);
		// Redraw Cards
		if (gameState.isCurrentPlayerHuman()) {
			BasicCommands.drawHandCards(gameState.getHumanPlayer().getMyHandCards(), out, 0);
		}
	}

	/**
	 *  getSpellObject
	 *  
	 *  @param id (int)
	 *  @return spell (Spell) 
	 *  
	 *  Given the card id - it returns the spell object associated with the card
	 *  
	 */
	private static Spell getSpellObject(int id) {
		Spell mySpell = null;

		switch (id) {
		case 2:
		case 12:
			mySpell = new HornOfForsaken();
			break;
		case 5:
		case 15:
			mySpell = new WraithlingSwarm();
			break;
		case 8:
		case 18:
			mySpell = new DarkTerminus();
			break;
		case 25:
		case 35:
			mySpell = new BeamShock();
			break;
		case 29:
		case 39:
			mySpell = new SundropElixir();
			break;
		case 30:
		case 40:
			mySpell = new TrueStrike();
			break;
		}

		return mySpell;
	}

	/**
	 *  getCreatureObject
	 *  
	 *  @param clickedCard (Card)
	 *  @return unit (Unit)
	 *  
	 *  Given the card it returns the creature object associated with the card
	 * 
	 */
	public static Unit getCreatureObject(Card clickedCard) {
		Unit creature = null;
		int id = clickedCard.getId();
		String conf = clickedCard.getUnitConfig();
		boolean isHumanUnit = false;

		switch (id) {
		case 1:
		case 11:
			creature = BasicObjectBuilders.loadUnit(conf, id, BadOmen.class);
			isHumanUnit = true;
			break;
		case 3:
		case 13:
			creature = BasicObjectBuilders.loadUnit(conf, id, GloomChaser.class);
			isHumanUnit = true;
			break;
		case 4:
		case 14:
			creature = BasicObjectBuilders.loadUnit(conf, id, ShadowWatcher.class);
			isHumanUnit = true;
			break;
		case 6:
		case 16:
			creature = BasicObjectBuilders.loadUnit(conf, id, NightsorrowAssassin.class);
			isHumanUnit = true;
			break;
		case 7:
		case 17:
			creature = BasicObjectBuilders.loadUnit(conf, id, RockPulveriser.class);
			isHumanUnit = true;
			break;
		case 9:
		case 19:
			creature = BasicObjectBuilders.loadUnit(conf, id, BloodmoonPriestess.class);
			isHumanUnit = true;
			break;
		case 10:
		case 20:
			creature = BasicObjectBuilders.loadUnit(conf, id, ShadowDancer.class);
			isHumanUnit = true;
			break;
		case 21:
		case 31:
			creature = BasicObjectBuilders.loadUnit(conf, id, SkyrockGolem.class);
			break;
		case 22:
		case 32:
			creature = BasicObjectBuilders.loadUnit(conf, id, SwampEntangler.class);
			break;
		case 23:
		case 33:
			creature = BasicObjectBuilders.loadUnit(conf, id, SilverguardKnight.class);
			break;
		case 24:
		case 34:
			creature = BasicObjectBuilders.loadUnit(conf, id, SaberspineTiger.class);
			break;
		case 26:
		case 36:
			creature = BasicObjectBuilders.loadUnit(conf, id, YoungFlamewing.class);
			break;
		case 27:
		case 37:
			creature = BasicObjectBuilders.loadUnit(conf, id, SilverguardSquire.class);
			break;
		case 28:
		case 38:
			creature = BasicObjectBuilders.loadUnit(conf, id, IronCliffeGuardian.class);
		}


		// Other configuration for the units created	
		creature.setHealth(clickedCard.getBigCard().getHealth());
		creature.setAttack(clickedCard.getBigCard().getAttack());
		creature.setMaximumHealth(creature.getHealth());
		creature.setHumanUnit(isHumanUnit);

		return creature;
	}
	
	/**
	 *   isEnemyUnit 
	 *   
	 *   @param unit (Unit)
	 *   @return boolean, true if the given unit is an
	 *   enemy unit to the current player
	 *  
	 */
	public static boolean isEnemyUnit(Unit unit) {		
		GameState gameState = GameState.getInstance();
		
		if (gameState.isCurrentPlayerHuman()) {
			return gameState.getAIPlayer().getMyUnits().contains(unit);
		} else {
			return gameState.getHumanPlayer().getMyUnits().contains(unit);
		}
	}
	
	/**
	 *   wraithlingSummonStatus 
	 *   
	 *   @return boolean, true if the game is in progress of 
	 *   summoning a wraithling
	 *  
	 */
    public static boolean wraithlingSummonStatus() {
    	GameState gameState = GameState.getInstance();
    	Spell spellToCast = gameState.getSpellToCast();
		if (! (spellToCast == null)) {
			if (gameState.isSpellWraithlingSwarm()) {
				int numWraithlingSummoned = ((WraithlingSwarm) spellToCast).getNumWraithlings();
				if (numWraithlingSummoned == 0) {
					return false;
				}
				if (numWraithlingSummoned < WraithlingSwarm.maximumWraithlings) {
					return true;
				}
			}
		}
		return false;
    }
    
	/**
	 *  UpdateCurrentPlayerMana
	 *  
	 *  @param out (ActorRef)
	 *  @param mana (int)
	 */
	public static void updateCurrentPlayerMana(ActorRef out, int mana) {
		GameState gameState = GameState.getInstance();
		gameState.getCurrentPlayer().setMana(mana);

		if (gameState.isCurrentPlayerHuman()) {
			BasicCommands.setPlayer1Mana(out, gameState.getCurrentPlayer());
		} else {
			BasicCommands.setPlayer2Mana(out, gameState.getCurrentPlayer());	
		}
	}

    /**
     *  updatePlayerHealth
     *  
     *  @param out (ActorRef)
     *  @param target (Unit)
     *  
     */
    public static void updatePlayerHealth(ActorRef out, Unit target) {

    	GameState gameState = GameState.getInstance();
    	int aiAvatarID = gameState.getAIPlayer().getAvatarID();
    	int humanAvatarID = gameState.getHumanPlayer().getAvatarID();

    	// Update the Player's health if the avatar was the target
    	if (target.getId() == humanAvatarID) {
    		gameState.getHumanPlayer().setHealth(target.getHealth());
    		// Communicate the updated health and mana values to the front-end
    		BasicCommands.setPlayer1Health(out, gameState.getHumanPlayer());
    		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
    	} else if (target.getId() == aiAvatarID) {
    		gameState.getAIPlayer().setHealth(target.getHealth());
    		BasicCommands.setPlayer2Health(out, gameState.getAIPlayer());
    		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
    	}
    }
}
