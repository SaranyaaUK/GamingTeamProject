package commands;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import akka.actor.ActorRef;
import play.libs.Json;
import structures.GameState;
import structures.basic.Card;
import structures.basic.EffectAnimation;
import structures.basic.GameLogic;
import structures.basic.Grid;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimation;
import structures.basic.UnitAnimationType;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;
import utils.TilesGenerator;


/**
 * This is a utility class that simply provides short-cut methods for
 * running the basic command set for the game.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class BasicCommands {

	private static ObjectMapper mapper = new ObjectMapper(); // Jackson Java Object Serializer, is used to turn java objects to Strings
	
	// An alternative class with a 'tell' implementation can be given if writing unit tests
	// and need to have a null ActorRef. This should be null during normal operation.
	public static DummyTell altTell = null;

	/**
	 * You can consider the contents of the user’s browser window a canvas that can be drawn upon. drawTile will draw 
	 * the image of a board tile on the board. This command takes as input a Tile object and a visualisation mode (an 
	 * integer) that specifies which version of the tile to render (each tile has multiple versions, e.g. normal vs. 
	 * highlighted). This command can be used multiple times to change the visualisation mode for a tile.
	 * @param out
	 * @param tile
	 * @param mode
	 */
	@SuppressWarnings({"deprecation"})
	public static void drawTile(ActorRef out, Tile tile, int mode) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "drawTile");
			returnMessage.put("tile", mapper.readTree(mapper.writeValueAsString(tile)));
			returnMessage.put("mode", mode);
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * drawUnit will draw the sprite for a unit (a picture of that unit with its attack and health values) on the board. 
	 * This command takes as input a target Tile (a ‘square’ of the main game grid) to place the unit’s sprite upon, 
	 * and the instance of the Unit (which holds the needed information about how to draw that unit).
	 * @param out
	 * @param unit
	 * @param tile
	 */
	@SuppressWarnings({"deprecation"})
	public static void drawUnit(ActorRef out, Unit unit, Tile tile) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "drawUnit");
			returnMessage.put("tile", mapper.readTree(mapper.writeValueAsString(tile)));
			returnMessage.put("unit", mapper.readTree(mapper.writeValueAsString(unit)));
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command changes the visualised attack value just under a unit’s sprite to a value between 0 
	 * and 20. The command takes in a unit instance. The associated values are read from the unit object.
	 * @param out
	 * @param unit
	 * @param attack
	 */
	@SuppressWarnings({"deprecation"})
	public static void setUnitAttack(ActorRef out, Unit unit, int attack) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "setUnitAttack");
			returnMessage.put("unit", mapper.readTree(mapper.writeValueAsString(unit)));
			returnMessage.put("attack", attack);
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command changes the visualised health value just under a unit’s sprite to a value between 0 
	 * and 20. The command takes in a unit instance. The associated values are read from the unit object.
	 * @param out
	 * @param unit
	 * @param health
	 */
	@SuppressWarnings({"deprecation"})
	public static void setUnitHealth(ActorRef out, Unit unit, int health) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "setUnitHealth");
			returnMessage.put("unit", mapper.readTree(mapper.writeValueAsString(unit)));
			returnMessage.put("health", health);
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command moves a unit sprite from one tile to another. It takes in the unit’s object and the target Tile. 
	 * Note that this command will start the movement, it may take multiple seconds for the movement to complete.
	 * @param out
	 * @param unit
	 * @param tile
	 */
	@SuppressWarnings({"deprecation"})
	public static void moveUnitToTile(ActorRef out, Unit unit, Tile tile) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "moveUnitToTile");
			returnMessage.put("unit", mapper.readTree(mapper.writeValueAsString(unit)));
			returnMessage.put("tile", mapper.readTree(mapper.writeValueAsString(tile)));
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command moves a unit sprite from one tile to another. It takes in the unit’s object and the target Tile. 
	 * Note that this command will start the movement, it may take multiple seconds for the movement to complete.
	 * yfirst sets whether the move should move the unit vertically first before moving horizontally
	 * @param out
	 * @param yfirst
	 * @param unit
	 * @param tile
	 */
	@SuppressWarnings({"deprecation"})
	public static void moveUnitToTile(ActorRef out, Unit unit, Tile tile, boolean yfirst) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "moveUnitToTile");
			returnMessage.put("yfirst", yfirst);
			returnMessage.put("unit", mapper.readTree(mapper.writeValueAsString(unit)));
			returnMessage.put("tile", mapper.readTree(mapper.writeValueAsString(tile)));
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command makes a unit play a specified animation. It takes in the unit object which
	 * contains all of the data needed to play the animations, and a UnitAnimation that specifies
	 * which animation to switch to.
	 * 
	 * This method now returns an estimate for the number of milliseconds until the animation completes
	 * playing in the browser. Ignore this if it is a looping animation.
	 * @param out
	 * @param unit
	 * @param animation
	 */
	@SuppressWarnings({"deprecation"})
	public static int playUnitAnimation(ActorRef out, Unit unit, UnitAnimationType animationToPlay) {
		try {
			
			unit.setAnimation(animationToPlay);
			
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "playUnitAnimation");
			returnMessage.put("unit", mapper.readTree(mapper.writeValueAsString(unit)));
			returnMessage.put("animation", animationToPlay.toString());
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
			
			// estimate the time needed for the animation to play
			UnitAnimation animation = null;
			if (animationToPlay.equals(UnitAnimationType.idle)) animation = unit.getAnimations().getIdle();
			if (animationToPlay.equals(UnitAnimationType.attack)) animation = unit.getAnimations().getAttack();
			if (animationToPlay.equals(UnitAnimationType.channel)) animation = unit.getAnimations().getChannel();
			if (animationToPlay.equals(UnitAnimationType.death)) animation = unit.getAnimations().getDeath();
			if (animationToPlay.equals(UnitAnimationType.hit)) animation = unit.getAnimations().getHit();
			if (animationToPlay.equals(UnitAnimationType.move)) animation = unit.getAnimations().getMove();
			
			if (animation==null) return 0;
			
			return ((1000*(animation.getFrameStartEndIndices()[1]-animation.getFrameStartEndIndices()[0]))/animation.getFps())+50;
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	
	/**
	 * This will delete a unit instance from the board. It takes as input the unit object of the unit.
	 * @param out
	 * @param unit
	 */
	@SuppressWarnings({"deprecation"})
	public static void deleteUnit(ActorRef out, Unit unit) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "deleteUnit");
			returnMessage.put("unit", mapper.readTree(mapper.writeValueAsString(unit)));
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command changes the visualised health value in the player’s information card to a value between 0 
	 * and 20. The command takes in a basic player instance. The associated values are read from the basic player 
	 * object.
	 * @param out
	 * @param player
	 */
	@SuppressWarnings({"deprecation"})
	public static void setPlayer1Health(ActorRef out, Player player) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "setPlayer1Health");
			returnMessage.put("player", mapper.readTree(mapper.writeValueAsString(player)));
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command changes the visualised health value in the player’s information card to a value between 0 
	 * and 20. The command takes in a basic player instance. The associated values are read from the basic player 
	 * object.
	 * @param out
	 * @param player
	 */
	@SuppressWarnings({"deprecation"})
	public static void setPlayer2Health(ActorRef out, Player player) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "setPlayer2Health");
			returnMessage.put("player", mapper.readTree(mapper.writeValueAsString(player)));
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command changes the visualised mana value in the player’s information card to a value between 0 
	 * and 9. The command takes in a basic player instance. The associated values are read from the basic player 
	 * object.
	 * @param out
	 * @param player
	 */
	@SuppressWarnings({"deprecation"})
	public static void setPlayer1Mana(ActorRef out, Player player) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "setPlayer1Mana");
			returnMessage.put("player", mapper.readTree(mapper.writeValueAsString(player)));
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command changes the visualised mana value in the player’s information card to a value between 0 
	 * and 9. The command takes in a basic player instance. The associated values are read from the basic player 
	 * object.
	 * @param out
	 * @param player
	 */
	@SuppressWarnings({"deprecation"})
	public static void setPlayer2Mana(ActorRef out, Player player) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "setPlayer2Mana");
			returnMessage.put("player", mapper.readTree(mapper.writeValueAsString(player)));
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command renders a card in the player’s hand. It takes as input a hand position (a value between 1-6), a 
	 * Card (which is an object containing basic information needed to visualise that card) and a visualisation mode 
	 * (similarly to a tile). This command can be issued multiple times to change the visualisation mode of a card.
	 * @param out
	 * @param card
	 * @param position
	 * @param mode
	 */
	@SuppressWarnings({"deprecation"})
	public static void drawCard(ActorRef out, Card card, int position, int mode) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "drawCard");
			returnMessage.put("card", mapper.readTree(mapper.writeValueAsString(card)));
			returnMessage.put("position", position);
			returnMessage.put("mode", mode);
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This command deletes a card in the player’s hand. It takes as input a hand position (a value between 1-6).
	 * @param out
	 * @param position
	 */
	public static void deleteCard(ActorRef out, int position) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "deleteCard");
			returnMessage.put("position", position);
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Plays a specified EffectAnimation (such as an explosion) centred on a particular Tile. It takes as input an 
	 * EffectAnimation (an object with information about rendering the effect) and a target Tile.
	 * 
	 * This method has been updated to provide an estimate of the time until the animation will finish playing
	 * @param out
	 * @param effect
	 * @param tile
	 */
	@SuppressWarnings({"deprecation"})
	public static int playEffectAnimation(ActorRef out, EffectAnimation effect, Tile tile) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "playEffectAnimation");
			returnMessage.put("effect", mapper.readTree(mapper.writeValueAsString(effect)));
			returnMessage.put("tile", mapper.readTree(mapper.writeValueAsString(tile)));
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
			
			return ((1000*effect.getAnimationTextures().size())/effect.getFps())+50;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * This command creates a notification box next to the portrait for the player 1 which contains
	 * the specified text. It will be displayed for a number of seconds before being removed.
	 * object.
	 * @param out
	 * @param text
	 * @param displayTimeSeconds
	 */
	public static void addPlayer1Notification(ActorRef out, String text, int displayTimeSeconds) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "addPlayer1Notification");
			returnMessage.put("text", text);
			returnMessage.put("seconds", displayTimeSeconds);
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Plays a projectile fire animation between two tiles
	 * @param out
	 * @param effect
	 * @param tile
	 */
	@SuppressWarnings({"deprecation"})
	public static void playProjectileAnimation(ActorRef out, EffectAnimation effect, int mode, Tile startTile, Tile targetTile) {
		try {
			ObjectNode returnMessage = Json.newObject();
			returnMessage.put("messagetype", "drawProjectile");
			returnMessage.put("effect", mapper.readTree(mapper.writeValueAsString(effect)));
			returnMessage.put("tile", mapper.readTree(mapper.writeValueAsString(startTile)));
			returnMessage.put("targetTile", mapper.readTree(mapper.writeValueAsString(targetTile)));
			returnMessage.put("mode", mapper.readTree(mapper.writeValueAsString(mode)));
			if (altTell!=null) altTell.tell(returnMessage);
			else out.tell(returnMessage, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Draws the entire game grid on the front-end.
	 * Iterates through each tile in the grid and sends a command to draw it.
	 * The visualisation mode can be adjusted based on game logic (e.g., normal, highlighted).
	 *
	 * @param out The ActorRef for sending messages to the front-end.
	 * @param grid The Grid object containing the tiles to be drawn.
	 */
	public static void drawGrid(ActorRef out, Grid grid) {
		// Assuming default visualisation mode is 0 for all tiles initially
		int defaultMode = 0;

		// Get the 2D array of tiles from the Grid object
		Tile[][] tiles = grid.getBoardTiles();

		// Iterate through each tile in the grid
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				Tile tile = grid.getTile(x+1,y+1);
				// Use the drawTile method to draw each tile on the front-end
				BasicCommands.drawTile(out, tile, defaultMode);
			}
		}
	}

	public static void drawHandCards(List<Card> handCards, ActorRef out, int mode) {
		int position = 1; // Start position from 1
		for (Card card : handCards) {
				card.getMiniCard().setIndex(mode);
				BasicCommands.drawCard(out, card, position, mode);
				position++; // Increment position for next card
		}
	}
	
	public static void deleteHandCards(List<Card> handCards, ActorRef out) {
		int position = 1; // Start position from 1
		for (Card card : handCards) {
				BasicCommands.deleteCard(out, position);
				position++; // Increment position for next card
		}
	}

	// Helper methods for other front-end communication
	
	/*
	 *  Used by the Initialise event to draw the player's avatar and its statistics
	 */
	public static void drawUnitsAndSetAttributes(ActorRef out) {
		
		GameState gameState = GameState.getInstance();
		Player humanPlayer = gameState.getHumanPlayer();
		Player aiPlayer = gameState.getAIPlayer();
		Player currentPlayer = gameState.getCurrentPlayer();
		Unit humanAvatar = humanPlayer.getAvatar();
		Unit aiAvatar = aiPlayer.getAvatar();

		// Place human avatar
		EffectAnimation summonEffect = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_summon);
		BasicCommands.placeUnit(out, humanAvatar, gameState.getGrid().getTile(2, 3), summonEffect);
		
		// Place AI avatar
		BasicCommands.placeUnit(out, aiAvatar, gameState.getGrid().getTile(8, 3), summonEffect);

		// Communicate the updated health and mana values to the front-end for the player 
		BasicCommands.setPlayer1Health(out, humanPlayer);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		BasicCommands.setPlayer2Health(out, aiPlayer);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		BasicCommands.setPlayer1Mana(out, currentPlayer);
	}
	
	/**
	 *  Dehighlight any highlighted card
	 *  Used in highlightsAfterCardClick method
	 *  
	 *  @param out (ActorRef)
	 *  
	 */
    public static void dehighlightCards(ActorRef out) {
    	GameState gameState = GameState.getInstance();
    	BasicCommands.drawHandCards(gameState.getCurrentPlayer().getMyHandCards(), out, 0);
    }
    
    /**
     *  Highlight clicked cards
     *  
     *  @param out (ActorRef)
     */
    public static void highlightClickedCard(ActorRef out) {
    	
    	GameState gameState = GameState.getInstance();
    	Card clickedCard = gameState.getClickedCard();
		clickedCard.getMiniCard().setIndex(1); // Sets the animations to play in the minicard
		BasicCommands.drawCard(out, clickedCard, gameState.getHandPosition(), 1);
		try {Thread.sleep(2);} catch (InterruptedException e) {e.printStackTrace();}
    }

    /**
     * 	Highlight of tiles
     * 
     *  @param out (ActorRef)
     */
    public static void highlightTiles(ActorRef out) {
    	
    	GameState gameState = GameState.getInstance();
    	
    	// Iterate over the tiles to be Highlighted
    	for (Tile tile: gameState.getHighlightedFriendlyTiles()) {
    		BasicCommands.drawTile(out, tile, 1);
    		try {Thread.sleep(2);} catch (InterruptedException e) {e.printStackTrace();}
    	}
    	
    	for (Tile tile: gameState.getHighlightedEnemyTiles()) {
    		BasicCommands.drawTile(out, tile, 2);
    		try {Thread.sleep(2);} catch (InterruptedException e) {e.printStackTrace();}
    	}
    }
    
    /**
     *  Dehighlight tiles
     *  
     *  @param out (ActorRef)
     *  
     */
    public static void dehighlightTiles(ActorRef out) {
    	
    	GameState gameState = GameState.getInstance();
 
    	// Iterate over the highlighted tiles to dehighlight it.
    	for (Tile tile: gameState.getHighlightedFriendlyTiles()) {
    		BasicCommands.drawTile(out, tile, 0);
    		try {Thread.sleep(2);} catch (InterruptedException e) {e.printStackTrace();}
    	}
    	
    	for (Tile tile: gameState.getHighlightedEnemyTiles()) {
    		BasicCommands.drawTile(out, tile, 0);
    		try {Thread.sleep(2);} catch (InterruptedException e) {e.printStackTrace();}
    	}
    }
    
    /**
     *  placeUnit
     *  
     *  Unit placement actions
     *  
     *  @param out (ActorRef)
     *  @param unit (Unit)
     *  @param tile (Tile)
     *  @param summonEffect (EffectAnimation)
     *  
     */
	public static void placeUnit(ActorRef out, Unit unit, Tile tile, EffectAnimation summonEffect) {
		// Do the front-end communication
		BasicCommands.drawUnit(out, unit, tile);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		BasicCommands.playEffectAnimation(out, summonEffect, tile);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		BasicCommands.setUnitAttack(out, unit, unit.getAttack());
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		BasicCommands.setUnitHealth(out, unit, unit.getHealth());
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	/**
	 *  attackUnit
	 *  
	 *  Takes care of unit attack actions 
	 *  
	 *  @param ref (ActorRef)
	 *  @param unit (Unit)
	 *  @param target (Unit)
	 *
	 */
	
	public static void attackUnit(ActorRef ref, Unit unit, Unit target) {
		// Update the Player's health if the avatar was target
		GameLogic.updatePlayerHealth(ref, target);

		//attack animation
		BasicCommands.playUnitAnimation(ref, unit, UnitAnimationType.attack);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		// Update target's health
		BasicCommands.setUnitHealth(ref, target, target.getHealth());
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

		// Target plays the hit animation
		BasicCommands.playUnitAnimation(ref,target,UnitAnimationType.hit);
		try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
	}
}
