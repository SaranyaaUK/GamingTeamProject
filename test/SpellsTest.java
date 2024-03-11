import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import akka.actor.ActorRef;
import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import gamelogic.Actions;

import org.junit.Before;

import structures.GameState;
import structures.basic.*;
import structures.basic.creatures.Wraithlings;
import structures.basic.spell.*;
import utils.BasicObjectBuilders;
import utils.TilesGenerator;

public class SpellsTest {
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
	public void testBeamShockApplySpell() {
		Unit unit1 = GameLogic.getCreatureObject(humanPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(unit1, board.getTile(3, 4));

		// Creating instance of BeamShock
		BeamShock beamShock = new BeamShock();

		// Apply Spell
		beamShock.applySpell(null, board.getTile(3, 4));

		// Verify
		assertTrue("Unit is stunned", unit1.isStunned());
	}

	@Test 
	public void testDarkTerminusApplySpell() {
		Unit unit1 = GameLogic.getCreatureObject(aiPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(unit1, board.getTile(3, 4));

		// Creating instance of Dark Terminus
		DarkTerminus darkTerminus = new DarkTerminus();

		// Apply spell
		darkTerminus.applySpell(null, board.getTile(3, 4));

		// Verify
		assertEquals("Unit died", unit1.getHealth(), 0);

		// Test if a new wraithling is added to the tile
		assertTrue("Wraithling summoned", board.getTile(3, 4).getUnit() instanceof Wraithlings);
	}

	@Test
	public void testHornOfForsakenApplySpell() {
		Unit avatar = humanPlayer.getAvatar();

		// Creating instance of HornOfForsaken
		HornOfForsaken hornOfForsaken = new HornOfForsaken();

		// Verify artifact before spell cast
		assertEquals("Artifact is -1", avatar.getArtifact(), -1);

		// Apply spell
		hornOfForsaken.applySpell(null, board.getTile(avatar.getPosition().getTilex(), avatar.getPosition().getTiley()));

		// Verify artifact
		assertEquals("Artifact is 3", avatar.getArtifact(), 3);

		// Check if the artifact decreases when the avatar takes attack
		Unit attacker = aiPlayer.getAvatar();
		GameLogic.associateUnitWithTile(attacker, board.getTile(3, 3));

		Actions.unitAdjacentAttack(null, attacker, avatar);

		// Verify artifact and wraithling summoned
		assertEquals("Artifact is 2", avatar.getArtifact(), 2);

		Actions.unitAdjacentAttack(null, attacker, avatar);
		Actions.unitAdjacentAttack(null, attacker, avatar);
		Actions.unitAdjacentAttack(null, attacker, avatar);

		assertEquals("Artifact is -1", avatar.getArtifact(), -1);
	}

	@Test
	public void testSundropElixirApplySpell() {
		Unit aiavatar = aiPlayer.getAvatar();
		// Reduce the avatar's health
		aiavatar.setHealth(15);

		// Creating instance of SundropElixir
		SundropElixir sundropElixir = new SundropElixir();

		// Check unit's health before healing
		assertEquals("The unit's health before healing", 15, aiavatar.getHealth());

		// Apply Spell
		sundropElixir.applySpell(null, TilesGenerator.getUnitTile(aiavatar));

		// Check for the unit's updated health
		assertEquals("The unit's health is healed", 19, aiavatar.getHealth());

		// Apply spell again and it should not increase the unit's maximum health
		sundropElixir.applySpell(null, TilesGenerator.getUnitTile(aiavatar));
		assertEquals("The unit's health is healed", aiavatar.getMaximumHealth(), aiavatar.getHealth());
	}

	@Test
	public void testTrueStrikeApplySpell() {
		Unit avatar = humanPlayer.getAvatar();
		avatar.setHealth(16);
		// Creating instance of TrueStrike
		TrueStrike trueStrike = new TrueStrike();

		// Check for the unit's health before applying spell
		assertEquals("The unit's health is healed", 16, avatar.getHealth());

		// Apply Spell
		trueStrike.applySpell(null, TilesGenerator.getUnitTile(avatar));

		// Check for the unit's health after casting spell
		assertEquals("The unit's health after applying True Strike", 14, avatar.getHealth());
	}
	
	@Test
	public void testWraithlingSwarmApplySpell() {
		Unit avatar = humanPlayer.getAvatar();
		avatar.setHealth(16);
		// Creating instance of WraithlingSwarm
		WraithlingSwarm wraithlingSwarm = new WraithlingSwarm();
		gameState.setSpellToCast(wraithlingSwarm);

		// Wraithling summon status verify
		assertFalse("Wraithling Summon Status must be false", GameLogic.wraithlingSummonStatus());

		// Apply Spell
		wraithlingSwarm.applySpell(null, board.getTile(4, 2));
		// Test if a new wraithling is added to the tile
		assertTrue("Wraithling summoned", board.getTile(4, 2).getUnit() instanceof Wraithlings);

		// Wraithling summon status verify
		assertTrue("Wraithling Summon Status must be true", GameLogic.wraithlingSummonStatus());
		
		wraithlingSwarm.applySpell(null, board.getTile(5, 2));
		wraithlingSwarm.applySpell(null, board.getTile(6, 2));
		
		// Test if a new wraithling is added to the tile
		assertTrue("Wraithling summoned", board.getTile(5, 2).getUnit() instanceof Wraithlings);
		assertTrue("Wraithling summoned", board.getTile(6, 2).getUnit() instanceof Wraithlings);
		// Wraithling summon status verify
		assertFalse("Wraithling Summon Status must be false", GameLogic.wraithlingSummonStatus());		
	}

}
