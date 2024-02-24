package structures.basic;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Help of the CardManager class goes here
 *
 */



public class CardManager {

    public static final int MAX_HAND_CARDS = 6;
    private Collection<Card> myDeck = new ArrayList<>();
    private Collection<Card> myHandCards = new ArrayList<>();

    /**
     * Generates a deck of cards.
     * The boolean parameter can be used to specify if the deck should be shuffled.
     *
     * @param shuffle If true, the deck will be shuffled after generation.
     */
    public void generateDeck(boolean shuffle) {
        // Implement deck generation logic here
        // Example: Populate myDeck with Card objects

        if (shuffle) {
            // Shuffle the deck if necessary
            // Collections.shuffle((List<?>) myDeck); // Uncomment after importing java.util.Collections
        }
    }

    /**
     * Draws a specified number of cards from the deck to the hand, without exceeding the MAX_HAND_CARDS limit.
     *
     * @param count The number of cards to draw.
     */
    public void drawCardFromDeck(int count) {
        for (int i = 0; i < count; i++) {
            if (myHandCards.size() < MAX_HAND_CARDS && !myDeck.isEmpty()) {
                // Assuming Card objects can be removed from myDeck and added to myHandCards
                Card card = myDeck.iterator().next();
                myHandCards.add(card);
                myDeck.remove(card);
            }
        }
    }

    /**
     * Checks if the deck is empty.
     *
     * @return True if the deck is empty, false otherwise.
     */
    public boolean isDeckEmpty() {
        return myDeck.isEmpty();
    }

    /**
     * Gets the cards currently in the hand.
     *
     * @return A collection of Card objects in the hand.
     */
    public Collection<Card> getHandCards() {
        return new ArrayList<>(myHandCards); // Return a copy to prevent external modification
    }

}

