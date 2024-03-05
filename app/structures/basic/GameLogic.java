package structures.basic;

import java.util.List;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import utils.BasicObjectBuilders;
import utils.TilesGenerator;
import structures.basic.creatures.*;
import structures.basic.spell.*;

// Import any packages as needed

/**
 * Help of the GameLogic class goes here
 *
 */

public class GameLogic {

	/*
	 *  Method to associate a unit with the tile, when a unit is created 
	 *  and we have it's tile position this method can be invoked 
	 *  It take the tile's position and also set the tile's unit attribute
	 *  accordingly.
	 */
	public static void associateUnitWithTile(Unit unit, Tile tile) {
		// Update the Unit's position to match the Tile's coordinates
		unit.setPositionByTile(tile);
		// Set the Unit on the Tile
		tile.setUnit(unit);
	}

	/*
	 *  Highlights after a card click
	 *  
	 *  Events that takes place after a card is clicked
	 */
	public static void highlightAfterCardClick(ActorRef out) {

		// First clear any previous highlighting
		clearPreviousCardClickHighlights(out);

		// Now highlight the clicked card
		BasicCommands.highlightClickedCard(out);

		// Target Tiles Highlighting based on the clicked card
		cardClickTileHighlights(out);
	}


	/*
	 *  Clear cardClick highlights
	 */
	private static void clearPreviousCardClickHighlights(ActorRef out) {

		GameState gameState = GameState.getInstance();
		BasicCommands.dehighlightCards(out);
		BasicCommands.dehighlightTiles(out);
		// Update gameState after you de-highlight (can move to some reset method ?)
		gameState.getHighlightedFriendlyTiles().clear();
		gameState.getHighlightedEnemyTiles().clear();
	}

	/*
	 * Card Click Highlight 
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
		for(Tile tile: gameState.getHighlightedFriendlyTiles()) {
			BasicCommands.addPlayer1Notification(out, ""+tile.getTilex()+", "+tile.getTiley(), 100);
		}
		BasicCommands.highlightTiles(out);
	}

	/*
	 *  NotifyDeathWatchers
	 *  
	 *  Used in setHealth method of the Unit class
	 */
	public static void notifyDeathWatchers(ActorRef out) {
		// Only Human units have DeathWatch ability

		GameState gameState = GameState.getInstance();

		for (Unit unit : gameState.getHumanPlayer().getMyUnits()) {
			if(unit instanceof DeathWatch) {
				((DeathWatch) unit).reactToDeath(out);
			}
		}

	}

	/*
	 *  ReactToAvatarAttack
	 *  
	 *  Implement the reaction when avatars are attacked
	 */
	public static void reactToAvatarAttack(ActorRef out, Unit avatar) {

		GameState gameState = GameState.getInstance();
		int aiAvatarID = gameState.getAIPlayer().getAvatarID();
		int humanAvatarID = gameState.getHumanPlayer().getAvatarID();

		if(!(avatar.getId() == aiAvatarID || avatar.getId() == humanAvatarID)) {
			return;
		}
		
		if (gameState.isCurrentPlayerHuman()) {
			// If Human avatar has the artifact (from Horn Of Forsaken Spell)
			// then decrement it
			Unit humanAvatar = gameState.getHumanPlayer().getAvatar();
			if(humanAvatar.getArtifact() > 0) {
				humanAvatar.decrementArtifact();
			}
		} else {
			// If AI avatar takes attack units with Zeal power reacts
			List<Unit> aiUnits = gameState.getAIPlayer().getMyUnits();
			for (Unit unit: aiUnits) {
				if (gameState.getAIPlayer() instanceof Zeal) {
					((Zeal) unit).applyZeal(out);
				}
			}	
		}

	}
	
	/*
	 *  UpdateHandCardsView 
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
	/*
	 *  Give the card id - it returns the spell object associated to the card
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

	/*
	 *  Give the card it returns the creature object associated to the card
	 *  Also used to create Wraithlings.
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
		default:
			creature = Wraithlings.createWraithling();
			isHumanUnit = true;
		}


		// Other configuration for the units created
		if (!(creature instanceof Wraithlings)) {
			creature.setHealth(clickedCard.getBigCard().getHealth());
			creature.setAttack(clickedCard.getBigCard().getAttack());
		}
		creature.setMaximumHealth(creature.getHealth());
		creature.setHumanUnit(isHumanUnit);

		return creature;
	}
}
