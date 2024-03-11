import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;

import structures.GameState;
import structures.basic.*;
import structures.basic.spell.*;
import utils.BasicObjectBuilders;

public class GameStateTest {
    private GameState gameState;

    // Setup
    @Before
    public void setUp() {
        gameState = GameState.getInstance();
        gameState.setGrid(BasicObjectBuilders.loadGrid());
        gameState.setHumanPlayer(new Player());
        gameState.setAIPlayer(new AIPlayer());
        gameState.setCurrentPlayer(gameState.getHumanPlayer());
        gameState.setTurn(1);
    }
    
    // Test next turn
    @Test
    public void testNextTurn() {
        assertEquals(1, gameState.getTurn());
        gameState.nextTurn();
        assertEquals(2, gameState.getTurn());
    }

    // Test CurrentPlayer
    @Test
    public void testIsCurrentPlayerHuman() {
        assertTrue(gameState.isCurrentPlayerHuman());
        gameState.switchCurrentPlayer();
        assertFalse(gameState.isCurrentPlayerHuman());
    }

    // Test isSpellWraithlingSwarm
    @Test
    public void testIsSpellWraithlingSwarm() {
        assertFalse(gameState.isSpellWraithlingSwarm());
        gameState.setSpellToCast(new HornOfForsaken());
        assertFalse(gameState.isSpellWraithlingSwarm());
     
        gameState.setSpellToCast(new WraithlingSwarm());
        assertTrue(gameState.isSpellWraithlingSwarm());
    }
}
