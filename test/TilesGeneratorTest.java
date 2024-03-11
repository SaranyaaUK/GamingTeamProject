import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;

import structures.GameState;
import structures.basic.*;
import utils.BasicObjectBuilders;
import utils.TilesGenerator;


public class TilesGeneratorTest {

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

	@Test
	public void testGetUnitTile() {
		// Get Unit's tile
		assertEquals(2, TilesGenerator.getUnitTile(humanPlayer.getAvatar()).getTilex());
		assertEquals(3, TilesGenerator.getUnitTile(humanPlayer.getAvatar()).getTiley());

		assertEquals(8, TilesGenerator.getUnitTile(aiPlayer.getAvatar()).getTilex());
		assertEquals(3, TilesGenerator.getUnitTile(aiPlayer.getAvatar()).getTiley());
	}

	@Test
	public void testIsValidTile() {
		// Check Tile validity
		assertFalse(TilesGenerator.isValidTile(10, 6));
		assertFalse(TilesGenerator.isValidTile(-1, 5));
		assertFalse(TilesGenerator.isValidTile(3, -1));
		assertFalse(TilesGenerator.isValidTile(-1, 0));

		assertTrue(TilesGenerator.isValidTile(9, 1));
		assertTrue(TilesGenerator.isValidTile(1, 1));
		assertTrue(TilesGenerator.isValidTile(9, 5));
		assertTrue(TilesGenerator.isValidTile(1, 5));
	}

	@Test
	public void testGetFriendlyandEnemyUnitTiles() {
		// Verify that the friendly unit tiles is not empty - by default it holds the avatar
		assertFalse(TilesGenerator.getFriendlyUnitTiles().isEmpty());
		assertTrue(TilesGenerator.getFriendlyUnitTiles().size()==1);

		// Sample Units
		Unit unit1 = GameLogic.getCreatureObject(humanPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(unit1, board.getTile(3, 4));
		Unit unit2 = GameLogic.getCreatureObject(humanPlayer.getCardManager().getMyDeck().get(2));
		GameLogic.associateUnitWithTile(unit2, board.getTile(5, 2));

		humanPlayer.addUnits(unit1);
		humanPlayer.addUnits(unit2);

		// Verify Friendly Units after the units addition
		assertTrue(TilesGenerator.getFriendlyUnitTiles().size()==3);

		gameState.switchCurrentPlayer();

		assertFalse(TilesGenerator.getFriendlyUnitTiles().size()==3);
	}

	@Test
	public void testGetEnemyUnitTiles() {
		// Verify that the enemy unit tiles is not empty - by default it holds the ai avatar
		assertFalse(TilesGenerator.getEnemyUnitTiles().isEmpty());
		assertTrue(TilesGenerator.getEnemyUnitTiles().size()==1);

		Unit unit1 = GameLogic.getCreatureObject(aiPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(unit1, board.getTile(7, 2));
		Unit unit2 = GameLogic.getCreatureObject(aiPlayer.getCardManager().getMyDeck().get(2));
		GameLogic.associateUnitWithTile(unit2, board.getTile(9, 3));
		Unit unit3 = GameLogic.getCreatureObject(aiPlayer.getCardManager().getMyDeck().get(3));
		GameLogic.associateUnitWithTile(unit3, board.getTile(6, 4));

		aiPlayer.addUnits(unit1);
		aiPlayer.addUnits(unit2);
		aiPlayer.addUnits(unit3);

		// Verify Enemy Units after the units addition
		assertTrue(TilesGenerator.getEnemyUnitTiles().size()==4);

		gameState.switchCurrentPlayer();

		assertFalse(TilesGenerator.getEnemyUnitTiles().size()==4);
	}

	@Test
	public void testGetMovableTiles() {    	
		// Human Unit
		// Unit has no obstruction
		Unit unit1 = GameLogic.getCreatureObject(humanPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(unit1, board.getTile(5, 3));
		humanPlayer.addUnits(unit1);
		assertEquals("Unit 1 has no obstruction, should return 12 tiles", 12,
				TilesGenerator.getMovableTiles(unit1).size());

		// Friendly units obstruction
		Unit unit2 = GameLogic.getCreatureObject(humanPlayer.getCardManager().getMyDeck().get(2));
		GameLogic.associateUnitWithTile(unit2, board.getTile(4, 2));
		humanPlayer.addUnits(unit2);

		Unit unit3 = GameLogic.getCreatureObject(humanPlayer.getCardManager().getMyDeck().get(3));
		GameLogic.associateUnitWithTile(unit3, board.getTile(3, 2));
		humanPlayer.addUnits(unit3);

		assertEquals("Unit 2 has 2 friendly obstruction, and near the board's edge, hence returns 9 tiles", 9,
				TilesGenerator.getMovableTiles(unit2).size());

		// Friendly units + Enemy unit obstruction
		Unit aiUnit1 = GameLogic.getCreatureObject(aiPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(aiUnit1, board.getTile(5, 2));
		aiPlayer.addUnits(aiUnit1);	

		assertEquals("Unit 2 has 2 friendly obstruction and 1 enemy, and is near the board's edge, hence returns 7 tiles", 7,
				TilesGenerator.getMovableTiles(unit2).size());

		assertEquals("Unit 1 has 1 friendly obstruction and 1 enemy, and is near the board's edge, hence returns 9 tiles", 9, 
				TilesGenerator.getMovableTiles(unit1).size());

		// For the AI Unit
		gameState.switchCurrentPlayer();
		assertEquals("AI Unit has 2 enemy obstruction, and is near the board's edge, hence returns 6 tiles", 6,
				TilesGenerator.getMovableTiles(aiUnit1).size());

	}

	@Test
	public void testGetTilesToSummon() {
		// Human Unit
		Unit unit1 = GameLogic.getCreatureObject(humanPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(unit1, board.getTile(3, 2));
		humanPlayer.addUnits(unit1);

		// AI Unit
		Unit aiUnit1 = GameLogic.getCreatureObject(aiPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(aiUnit1, board.getTile(3, 3));
		aiPlayer.addUnits(aiUnit1);

		assertEquals("The summoning tiles should be 11", 11, TilesGenerator.getTilesToSummon().size());

		// Switch player
		gameState.switchCurrentPlayer();

		// 8 around avatar and 6 around the ai unit
		assertEquals("The summoning tiles should be 14", 14, TilesGenerator.getTilesToSummon().size());
	}

	@Test
	public void testGetEnemiesWithinMovableDistance() {
		// Human Unit
		// Avatar at (2,3)
		// Unit at (5,3)
		Unit unit1 = GameLogic.getCreatureObject(humanPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(unit1, board.getTile(5, 3));
		humanPlayer.addUnits(unit1);

		// Enemy avatar at (8,3)
		// Enemy unit at (5,3)
		// Enemy unit at (1,1)
		Unit aiUnit1 = GameLogic.getCreatureObject(aiPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(aiUnit1, board.getTile(5, 2));
		aiPlayer.addUnits(aiUnit1);
		
		Unit aiUnit2 = GameLogic.getCreatureObject(aiPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(aiUnit2, board.getTile(1, 1));
		aiPlayer.addUnits(aiUnit2);	
		
		// Only 2 units are within attackable distance
		assertEquals("Only 1 unit in move and attack distance", 2, 
				TilesGenerator.getEnemiesWithinMovableDistance(unit1).size());
	}

	@Test
	public void testGetAdjacentTiles() {
		// Human Unit
		Unit unit1 = GameLogic.getCreatureObject(humanPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(unit1, board.getTile(3, 2));
		humanPlayer.addUnits(unit1);

		assertEquals("Each tile should have 8 adjacent tiles", 8, TilesGenerator.getAdjacentTiles(unit1).size());

	}

	@Test
	public void testGetAdjacentEnemyTiles() {
		// Human Unit
		// Unit 1
		Unit unit1 = GameLogic.getCreatureObject(humanPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(unit1, board.getTile(5, 3));
		humanPlayer.addUnits(unit1);

		// Unit 2
		Unit unit2 = GameLogic.getCreatureObject(humanPlayer.getCardManager().getMyDeck().get(2));
		GameLogic.associateUnitWithTile(unit2, board.getTile(4, 2));
		humanPlayer.addUnits(unit2);

		// Enemy Unit
		Unit aiUnit1 = GameLogic.getCreatureObject(aiPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(aiUnit1, board.getTile(5, 2));
		aiPlayer.addUnits(aiUnit1);
		
		assertEquals("There is one enemy unit in an adjacent tile", 1, TilesGenerator.getAdjacentEnemyTiles(unit1).size());
	}

	@Test
	public void testHasProvokeUnitAround() {
		// Unit 1 
		Unit unit1 = GameLogic.getCreatureObject(humanPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(unit1, board.getTile(5, 3));
		humanPlayer.addUnits(unit1);

		// Enemy Provoke Unit
		Unit aiUnit1 = GameLogic.getCreatureObject(aiPlayer.getCardManager().getMyDeck().get(1));
		GameLogic.associateUnitWithTile(aiUnit1, board.getTile(5, 2));
		aiPlayer.addUnits(aiUnit1);
		
		assertTrue("Has Provoke unit adjacent",TilesGenerator.hasProvokeUnitAround(unit1));		
		
		// Remove the provoke unit and add another unit
		aiPlayer.getMyUnits().remove(aiPlayer.getMyUnits().size()-1);

		aiUnit1 = GameLogic.getCreatureObject(aiPlayer.getCardManager().getMyDeck().get(0));
		GameLogic.associateUnitWithTile(aiUnit1, board.getTile(5, 2));
		aiPlayer.addUnits(aiUnit1);

		assertFalse("No Provoke unit around",TilesGenerator.hasProvokeUnitAround(unit1));
	}
}
