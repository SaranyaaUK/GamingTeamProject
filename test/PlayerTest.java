
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import structures.basic.Player;
import utils.StaticConfFiles;

public class PlayerTest {

	@Test
	/*
	 * Test Player's avatar configuration
	 */
	public void testPlayerAvatarConfig() {

		Player humanPlayer = new Player();
		assertEquals(humanPlayer.getAvatarConfig(), StaticConfFiles.humanAvatar);

		// To do for AI Player - after I merge with Chen's code
	}

	@Test
	/*
	 * Test Player's statistics with accessors and mutators
	 */
	public void testPlayerStatics() {

		Player humanPlayer = new Player();
		assertEquals(humanPlayer.getHealth(), 20);
		assertEquals(humanPlayer.getMana(), 0);

		humanPlayer = new Player(18, 3);
		assertEquals(humanPlayer.getHealth(), 18);
		assertEquals(humanPlayer.getMana(), 3);

		humanPlayer.setHealth(12);
		assertEquals(humanPlayer.getHealth(), 12);

		humanPlayer.setMana(9);
		assertEquals(humanPlayer.getMana(), 9);
	}

	@Test
	public void testPlayerAvatar() {
		Player humanPlayer = new Player();
		assertTrue(humanPlayer.getAvatar() != null);
		assertEquals(humanPlayer.getAvatar().getId(), 41); // Looks like a bad test point (need to discuss)

		// To do for AI Player - after I merge with Chen's code
	}

	// To do Tests
	@Test
	public void testPlayerCardManager() {
		// Player humanPlayer = new Player();

		// To do after I merge with Charli's code

	}
}
