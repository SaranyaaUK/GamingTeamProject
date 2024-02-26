package structures.basic;

import java.util.List;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import utils.TilesGenerator;
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
    public static void highlightAfterCardClick(GameState gameState, ActorRef out) {
    	// First clear any previous highlighting
    	clearPreviousCardClickHighlights(gameState, out);

    	// Card Highlighting 
    	Card clickedCard = gameState.getClickedCard();
    	// Now highlight the clicked card
    	BasicCommands.highlightClickedCard(gameState,out);
		
		// Target Tiles Highlighting based on the clicked card
    	if (gameState.isCardClicked()) {
    		if (clickedCard.isCreature()) {
    			gameState.setSpellToCast(null);
    			// Creature cards - We only summoning here
    			// We should have friendly and enemy unit's tiles
    			
    			BasicCommands.highlightTiles(gameState, out, TilesGenerator.getTilesToSummon(gameState));
    		} else {
    			// Spell cards

    			// Instantiate the appropriate spell object
    			// Now get the target tiles to highlight
    			// Call drawTile with highlight mode.
    			Spell mySpellInstance = getSpellObject(clickedCard.getId());
    			gameState.setSpellToCast(mySpellInstance);

    			BasicCommands.highlightTiles(gameState, out, mySpellInstance.getTargetTilesToHighlight(gameState));
    		}
    	}
    	
    	// After the card is used (Probably after a tile is clicked) we need 
    	// resetHandPosition in gameState
    	// Clear the SpellToCast attribute in gameState
    }
    

    /*
     *  Clear cardClick highlights
     */
    private static void clearPreviousCardClickHighlights(GameState gameState, ActorRef out) {
    	BasicCommands.dehighlightCards(gameState, out);
    	BasicCommands.dehighlightTiles(gameState, out);
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

}
