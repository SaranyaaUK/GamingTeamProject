import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import structures.basic.Card;
import structures.basic.CardManager;

public class CardManagerTest {

    @Test
    public void testDeckGenerationForHumanPlayer() {
        CardManager cardManager = new CardManager(true);
        assertFalse("Deck should not be empty after generation for a human player", cardManager.isDeckEmpty());
    }

    @Test
    public void testDeckGenerationForAIPlayer() {
        CardManager cardManager = new CardManager(false);
        assertFalse("Deck should not be empty after generation for an AI player", cardManager.isDeckEmpty());
    }

    @Test
    public void testDrawCardFromDeck() {
        CardManager cardManager = new CardManager(true);
        boolean success = cardManager.drawCardFromDeck(1);
        assertTrue("Drawing a card should be successful", success);
        assertEquals("Hand should have 1 card after drawing", 1, cardManager.getHandCards().size());
    }


    @Test
    public void testGetHandCards() {
        CardManager cardManager = new CardManager(true);
        cardManager.drawCardFromDeck(3);
        List<Card> handCards = cardManager.getHandCards();
        assertEquals("Hand should have 3 cards after drawing", 3, handCards.size());
    }

    @Test
    public void testMaxHandCardsLimit() {
        CardManager cardManager = new CardManager(true);
        cardManager.drawCardFromDeck(cardManager.MAX_HAND_CARDS + 2); // Attempt to draw more than MAX_HAND_CARDS
        assertEquals("Hand should not exceed MAX_HAND_CARDS", cardManager.MAX_HAND_CARDS, cardManager.getHandCards().size());
    }
}
