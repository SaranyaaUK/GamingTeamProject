import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gamelogic.Actions;
import structures.GameState;
import structures.basic.*;
import utils.BasicObjectBuilders;

public class UnitActionsTest {

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
	}

	// Place Unit
	@Test
	public void testPlaceUnit() {
		// Set clicked card
		humanPlayer.getCardManager().drawCardFromDeck(2);
		gameState.setHandPosition(1);
		Tile tile = board.getTile(3, 5);

		// Place unit
		Actions.placeUnit(null, tile);

		assertTrue("The unit is associated with the given tile", tile.getUnit()!= null);

		// Get the last unit summoned
		List<Unit> units = gameState.getCurrentPlayer().getMyUnits();
		Unit summonedUnit =  units.get(units.size()-1);

		assertEquals("The unit is placed in the assigned x tile", summonedUnit.getPosition().getTilex(), tile.getTilex());
		assertEquals("The unit is placed in the assigned y tile", summonedUnit.getPosition().getTiley(), tile.getTiley());
		// Check the unit status upon summoning
		assertTrue("The unit must be exhausted", summonedUnit.isExhausted());
	}

	// Place Wraithlings
	@Test
	public void testPlaceWraithlings() {
		// Set clicked card
		Tile tile = board.getTile(4, 3);

		// Place wraithling
		Actions.placeWraithling(null, tile);

		assertTrue("The unit is associated with the given tile", tile.getUnit()!= null);

		// Get the last unit summoned
		List<Unit> units = gameState.getCurrentPlayer().getMyUnits();
		Unit summonedUnit =  units.get(units.size()-1);

		assertEquals("The unit is placed in the assigned tile", summonedUnit.getPosition().getTilex(), tile.getTilex());
		assertEquals("The unit is placed in the assigned tile", summonedUnit.getPosition().getTiley(), tile.getTiley());
		// Check the unit status upon summoning
		assertTrue("The unit must be exhausted", summonedUnit.isExhausted());
	}

	// Unit move
	@Test
	public void testUnitMove() {
		// Unit
		Unit unit = GameLogic.getCreatureObject(humanPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(unit, board.getTile(3, 4));
		unit.setMoved(false);
		// To move tile
		Tile tile = board.getTile(3, 5);

		// Move unit
		Actions.unitMove(null, unit, tile);

		// Check the unit's position
		assertEquals("The unit is moved to the new tile", unit.getPosition().getTilex(), tile.getTilex());
		assertEquals("The unit is moved to the new tile", unit.getPosition().getTiley(), tile.getTiley());
		// Check the unit status after moving
		assertTrue("The unit moved", unit.isMoved());
	}

	// Check Diagonal Movement
	@Test
	public void testDiagonalMovement() {
		// Unit 1
		Unit unit1 = GameLogic.getCreatureObject(humanPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(unit1, board.getTile(5,3));
		// Unit 2
		Unit unit2 = GameLogic.getCreatureObject(humanPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(unit2, board.getTile(5, 2));

		assertFalse(Actions.checkDiagonalMovement(unit1,board.getTile(6, 3)));

		// Unit 3
		Unit unit = GameLogic.getCreatureObject(humanPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(unit, board.getTile(6, 3));

		assertFalse(Actions.checkDiagonalMovement(unit1, board.getTile(6, 2)));
	}

	// Unit attack
	@Test
	public void testUnitAdjacentAttack() {
		// Unit
		Unit unit = GameLogic.getCreatureObject(humanPlayer.getCardManager().getMyDeck().get(2));
		GameLogic.associateUnitWithTile(unit, board.getTile(3, 4));
		unit.setExhausted(false);

		// AI Unit
		Unit aiunit = GameLogic.getCreatureObject(aiPlayer.getCardManager().getMyDeck().get(1));
		GameLogic.associateUnitWithTile(aiunit, board.getTile(3, 5));
		
		
		int aiUnitHealth = aiunit.getHealth();
		int unitAttack = unit.getAttack();
		
		System.out.println(aiUnitHealth+" "+unitAttack);
		// Unit attack
		Actions.unitAdjacentAttack(null, unit, aiunit);

		assertTrue("The unit must be exhausted", unit.isExhausted());
		assertEquals("The enemy health is reduced", aiunit.getHealth(), Math.min(aiUnitHealth-unitAttack, 0));
	}

}
