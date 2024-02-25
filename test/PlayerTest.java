import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import structures.GameState;
import structures.basic.AIPlayer;
import structures.basic.Player;
import utils.StaticConfFiles;

/*
 *  Test the Player and AI Player classes
 */
public class PlayerTest {

	@Test
	/*
	 * Test Player's avatar configuration
	 */
	public void testPlayerAvatarConfig() {

		Player humanPlayer = new Player();
		assertEquals(humanPlayer.getAvatarConfig(), StaticConfFiles.humanAvatar);

		Player aiPlayer = new AIPlayer();
		assertEquals(aiPlayer.getAvatarConfig(), StaticConfFiles.aiAvatar);
	}

	@Test
	/*
	 * Test Player's statistics with accessors and mutators
	 */
	public void testPlayerStatics() {

		Player humanPlayer = new Player();
		assertEquals("Player's Initial health should be 20", humanPlayer.getHealth(), GameState.INITIAL_HEALTH);
		assertEquals("Player's Initial mana should be 0", humanPlayer.getMana(), GameState.INITIAL_MANA);

		// Check Player Statistic set at construction
		humanPlayer = new Player(18, 3);
		assertEquals("Player's health should be equal to the set value", humanPlayer.getHealth(), 18);
		assertEquals("Player's mana should be equal to the set value", humanPlayer.getMana(), 3);

		// Check player's setHealth method
		humanPlayer.setHealth(12);
		assertEquals("Player's health should be equal to the set value", humanPlayer.getHealth(), 12);

		// Check Player's setMana method
		humanPlayer.setMana(9);
		assertEquals("Player's mana should be equal to the set value", humanPlayer.getMana(), 9);
	}

	@Test
	/*
	 * Test Player's avatar initialisations
	 */
	public void testPlayerAvatar() {
		Player humanPlayer = new Player();
		assertTrue("Player's avatar should not be null after instantiating the player", humanPlayer.getAvatar() != null);
		assertEquals("Player's avatar ID should be 41", humanPlayer.getAvatar().getId(), humanPlayer.getAvatarID());

		// AI Player
		Player aiPlayer = new AIPlayer();
		assertTrue("AI's avatar should not be null after instantiating the player", aiPlayer.getAvatar() != null);
		assertEquals("AI's avatar ID should be 42", aiPlayer.getAvatar().getId(), aiPlayer.getAvatarID());
	}

	@Test
	/*
	 * Test Player's card manager
	 */
	public void testPlayerCardManager() {
		// Check Player deck 
		Player humanPlayer = new Player();
		assertFalse("After player's construct the deck should not be empty", humanPlayer.isMyDeckEmpty());
		
		// Check Player's handCards - Should empty initially, until drawn from deck
        assertTrue("Hand should have 0 cards initially", humanPlayer.getMyHandCards().isEmpty());
        
        // Now draw n cards and check the hand cards
        humanPlayer.getCardManager().drawCardFromDeck(2);
        assertEquals("Hand should have 2 cards after drawing", 2, humanPlayer.getMyHandCards().size());
	}
}
