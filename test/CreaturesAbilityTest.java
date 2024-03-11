import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.util.List;


import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import gamelogic.Actions;

import org.junit.Before;

import structures.GameState;
import structures.basic.*;
import structures.basic.creatures.*;
import utils.BasicObjectBuilders;
import utils.TilesGenerator;

public class CreaturesAbilityTest {

	private Player humanPlayer;
	private Player aiPlayer;
	private Grid board;
	private GameState gameState;

	@Before
	public void setUp() {
		// Setup players
		gameState = GameState.getInstance();
		board = BasicObjectBuilders.loadGrid();
		gameState.setGrid(board);
		humanPlayer = new Player();
		aiPlayer = new AIPlayer();
		gameState.setAIPlayer(aiPlayer);
		gameState.setHumanPlayer(humanPlayer);

		GameLogic.associateUnitWithTile(humanPlayer.getAvatar(), board.getTile(2, 3));
		GameLogic.associateUnitWithTile(aiPlayer.getAvatar(), board.getTile(8, 3));

		gameState.setCurrentPlayer(humanPlayer);

		// Override the alt tell variable so we can issue commands without a running front-end
		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used
	}

	@Test
	public void testBadOmen() {
		// Create an instance of BadOmen

		Unit badOmen = new BadOmen();
		badOmen.setHumanUnit(true);
		badOmen.setAttack(0);

		GameLogic.associateUnitWithTile(badOmen, board.getTile(4, 2));
		humanPlayer.addUnits(badOmen);

		// Check initial attack
		assertEquals("Initial attack", 0, badOmen.getAttack());

		// Death watchers notified
		GameLogic.notifyDeathWatchers(null);

		// Check attack increase
		assertEquals("Increase in attack", 1, badOmen.getAttack());
	}

	@Test
	public void testBloodMoonPriestess() {
		// Create an instance of BloodmoonPriestess
		Unit bloodmoonPriestess = new BloodmoonPriestess();
		bloodmoonPriestess.setHumanUnit(true);
		bloodmoonPriestess.setAttack(0);

		GameLogic.associateUnitWithTile(bloodmoonPriestess, board.getTile(4, 2));
		humanPlayer.addUnits(bloodmoonPriestess);

		// Death watchers notified
		GameLogic.notifyDeathWatchers(null);

		// Check attack increase
		assertTrue("Wraithling summoned on death watch notification", adjacentTileHasUnit(bloodmoonPriestess));
	}

	@Test
	public void testGloomChaser() {
		// Use GloomChaser card
		humanPlayer.getCardManager().drawCardFromDeck(3);
		gameState.setHandPosition(3);

		// Place opening gambit unit
		Actions.placeUnit(null,board.getTile(5, 2));

		// The behind tile should have a wraithling summoned 
		assertTrue("Wraithling summoned on placing Gloom chaser", 
				board.getTile(4, 2).getUnit() instanceof Wraithlings);


		// If the behind tile is occupies this ability has no effect
		Unit unit1 = GameLogic.getCreatureObject(humanPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(unit1, board.getTile(6, 3));
		humanPlayer.addUnits(unit1);

		gameState.setHandPosition(3);
		// Place opening gambit unit
		Actions.placeUnit(null, board.getTile(5, 3));

		// The behind tile should have a wraithling summoned 
		assertFalse("Wraithling not summoned on placing Gloom chaser", 
				board.getTile(6, 3).getUnit() instanceof Wraithlings);
	}


	@Test
	public void testNightsorrowAssassin() {
		// Use NightsorrowAssassin card
		humanPlayer.getCardManager().drawCardFromDeck(6);
		gameState.setHandPosition(6);

		// Place a enemy unit in adjacent tile
		Unit unit1 = GameLogic.getCreatureObject(aiPlayer.getCardManager().getMyDeck().get(0));
		unit1.setMaximumHealth(3); // Set a higher maximum health for the unit
		unit1.setHumanUnit(false);
		GameLogic.associateUnitWithTile(unit1, board.getTile(6, 3));
		aiPlayer.addUnits(unit1);

		// Check initial health before summoning nightsorrow assassin
		assertEquals("Initial Health", 2, unit1.getHealth());

		// Summon NightsorrowAssassin to an adjacent tile
		Actions.placeUnit(null,board.getTile(5, 3));

		// Check health after summoning nightsorrow assassin
		assertEquals("Health after nightsorrow assassin summon", 0, unit1.getHealth());
	}

	@Test
	public void testShadowDancer() {
		// Create an instance of ShadowDancer
		Unit shadowDancer = new ShadowDancer();
		shadowDancer.setHumanUnit(true);
		shadowDancer.setAttack(3);
		shadowDancer.setHealth(1);
		GameLogic.associateUnitWithTile(shadowDancer, board.getTile(4, 2));
		humanPlayer.addUnits(shadowDancer);

		// AI Avatar
		Unit aiavatar = aiPlayer.getAvatar();
		aiavatar.setHealth(16);
		GameLogic.associateUnitWithTile(aiavatar, board.getTile(6, 3));

		// Check initial health of the AI Avatar and the ShadowDancer
		assertEquals("AI Avatar Initial Health", 16, aiavatar.getHealth());
		assertEquals("ShadowDancer Initial Health", 1, shadowDancer.getHealth());

		// Death watchers notified
		GameLogic.notifyDeathWatchers(null);

		// Check health of the AI Avatar and the ShadowDancer
		assertEquals("AI Avatar Health after death watch", 15, aiavatar.getHealth());
		assertEquals("ShadowDancer Health after death watch", 2, shadowDancer.getHealth());
	}

	@Test
	public void testShadowWatcher() {
		// Create an instance of ShadowWatcher
		Unit shadowWatcher = new ShadowWatcher();
		shadowWatcher.setHumanUnit(true);
		shadowWatcher.setAttack(3);
		shadowWatcher.setHealth(2);
		shadowWatcher.setMaximumHealth(2);
		GameLogic.associateUnitWithTile(shadowWatcher, board.getTile(4, 2));
		humanPlayer.addUnits(shadowWatcher);

		// Check initial health and attack of the ShadowWatcher
		assertEquals("ShadowWatcher Initial Health", 2, shadowWatcher.getHealth());
		assertEquals("ShadowWatcher Initial Attack", 3, shadowWatcher.getAttack());

		// Death watchers notified
		GameLogic.notifyDeathWatchers(null);

		// Check initial health and attack of the ShadowWatcher
		assertEquals("ShadowWatcher Health after death watch", 2, shadowWatcher.getHealth());
		assertEquals("ShadowWatcher Attack after death watch", 4, shadowWatcher.getAttack());
	}

	@Test
	public void testSaberspineTiger() {
		// Create an instance of SaberspineTiger
		Unit saberSpineTiger = GameLogic.getCreatureObject(aiPlayer.getCardManager().getMyDeck().get(3));

		// This has Rush ability and hence can move and attack when summoned
		assertFalse("isExhausted should be false", saberSpineTiger.isExhausted());
		assertFalse("isMoved should be false", saberSpineTiger.isMoved());
	}

	@Test
	public void testSilverguardKnight() {
		// Create an instance of SilverguardKnight
		Unit silverguardKnight = GameLogic.getCreatureObject(aiPlayer.getCardManager().getMyDeck().get(2));
		GameLogic.associateUnitWithTile(silverguardKnight, board.getTile(4, 2));
		aiPlayer.addUnits(silverguardKnight);

		// Check initial attack
		assertEquals("Initial attack", 1, silverguardKnight.getAttack());

		// Simulate ai avatar attack
		Unit avatar = humanPlayer.getAvatar();
		avatar.setHealth(18);
		GameLogic.associateUnitWithTile(avatar, board.getTile(5, 3));

		Unit aiavatar = aiPlayer.getAvatar();
		aiavatar.setHealth(16);
		GameLogic.associateUnitWithTile(aiavatar, board.getTile(6, 3));

		// aiavatar attacked
		Actions.unitAdjacentAttack(null, avatar, aiavatar);

		// Check attack increase
		assertEquals("Increase in attack", 3, silverguardKnight.getAttack());
	}

	@Test
	public void testSilverguardSquire() {
		gameState.setCurrentPlayer(aiPlayer);
		// Place unit behind and front of AI avatar
		Unit unit1 = GameLogic.getCreatureObject(humanPlayer.getCardManager().getMyDeck().get(0));
		unit1.setHumanUnit(true);
		unit1.setAttack(4);
		GameLogic.associateUnitWithTile(unit1, board.getTile(7, 3));
		humanPlayer.addUnits(unit1);
		
		aiPlayer.getCardManager().getMyDeck().remove(0); // Discard a card
		Unit unit2 = GameLogic.getCreatureObject(aiPlayer.getCardManager().getMyDeck().remove(0));
		unit2.setHumanUnit(false);
		GameLogic.associateUnitWithTile(unit2, board.getTile(9, 3));
		aiPlayer.addUnits(unit2);
		
		// Check attack for the units before summoning silverguardsquire
		assertEquals("Unit 1 attack", 4, unit1.getAttack());
		assertEquals("Unit 2 attack", 0, unit2.getAttack());

		// Use SilverguardSquire card
		aiPlayer.getCardManager().drawCardFromDeck(6);
		gameState.setHandPosition(5);

		// Place opening gambit unit
		Actions.placeUnit(null,board.getTile(5, 2));

		// Check attack for the units after summoning silverguardsquire
		assertEquals("Unit 1 attack increased", 4, unit1.getAttack());
		assertEquals("Unit 2 attack increased", 1, unit2.getAttack());
		
	}

	// Helper method
	private boolean adjacentTileHasUnit(Unit unit) {
		List<Tile> adjacentTiles = TilesGenerator.getAdjacentTiles(unit);

		for (Tile tile: adjacentTiles) {
			if (tile.getUnit() != null) {
				if(tile.getUnit() instanceof Wraithlings) {
					return true;
				}
			}
		}
		return false;
	}
}
