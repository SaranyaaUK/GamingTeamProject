package structures.basic;

import utils.OrderedCardLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Help of the CardManager class goes here
 */

public class CardManager {

    /*
     * Signature
     *
     * Attributes
     * Collection<Card> myDeck
     * Collection<Card> myHandCards
     * final MAX_HAND_CARDS = 6
     *
     * Methods
     *
     * void generateDeck(boolean)
     * void drawCardFromDeck(int)
     * boolean isDeckEmpty()
     * Collection<Card> getHandCards()
     *
     */

    private List<Card> myDeck;
    private List<Card> myHandCards;
    public final int MAX_HAND_CARDS = 6;

    public CardManager(boolean isHumanPlayer) {
        generateDeck(isHumanPlayer);
        myHandCards = new ArrayList<>();
    }

    /**
     * Generates a deck of cards for a player. The deck is different for human and
     * AI players.
     *
     * @param isHumanPlayer If true, generates the deck for a human player;
     *                      otherwise, for an AI player.
     */
    private void generateDeck(boolean isHumanPlayer) {
        if (isHumanPlayer) {
            this.myDeck = OrderedCardLoader.getPlayer1Cards(20);
        } else {
            this.myDeck = OrderedCardLoader.getPlayer2Cards(20);
        }
    }

    /**
     * Draws a specified number of cards from the deck to the hand. If the deck
     * doesn't have enough cards,
     * or the hand is at max capacity, the operation might not fully complete.
     *
     * @param CardCount The number of cards to draw.
     * @return true if the operation is successful, false if the deck is empty
     *         before drawing all cards.
     */
    public boolean drawCardFromDeck(int CardCount) {
        if (isDeckEmpty()) {
            return false;
        }
        for (int i = 0; i < CardCount; i++) {
            Card currentCard = myDeck.remove(myDeck.size() - 1);
            if (myHandCards.size() < MAX_HAND_CARDS) {
                myHandCards.add(currentCard);
            }
        }
        return true;
    }

    public boolean isDeckEmpty() {
        return myDeck.isEmpty();
    }

    public List<Card> getHandCards() {
        return this.myHandCards;
    }

}
