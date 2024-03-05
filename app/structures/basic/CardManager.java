package structures.basic;

import utils.OrderedCardLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * CardManager helps to manage the deck and hand cards for the player
 * 
 */

public class CardManager {

	// Player's Deck
    private List<Card> myDeck;
    // Player's Hand cards 
    private List<Card> myHandCards;
    // Maximum allowable cards in hand
    public final int MAX_HAND_CARDS = 6;

    public CardManager(boolean isHumanPlayer) {
        generateDeck(isHumanPlayer);
        myHandCards = new ArrayList<>(MAX_HAND_CARDS);
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
            this.myDeck = OrderedCardLoader.getPlayer1Cards(2);
        } else {
            this.myDeck = OrderedCardLoader.getPlayer2Cards(2);
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
            Card currentCard = myDeck.remove(0); // Always take card from the first position
            if (myHandCards.size() < MAX_HAND_CARDS) {
                myHandCards.add(currentCard);
            }
        }
        return true;
    }

    /*
     *  Gives the deck status
     *  
     *  @return true if the deck is empty, else false.
     */
    public boolean isDeckEmpty() {
        return myDeck.isEmpty();
    }

    /*  Gives the hand cards
     * 
     *  return list of cards in hand
     */
    public List<Card> getHandCards() {
        return this.myHandCards;
    }
    
    /*
     *  Delete card
     */
    public void deleteHandCardAt(int handPosition) {
    	this.myHandCards.remove(handPosition);
    }
    
    /*
     *  Get card
     */
    public Card getHandCardAt(int handPosition) {
    	return this.myHandCards.get(handPosition);
    }
    
    public List<Card> getMyDeck() {
    	return this.myDeck;
    }
}
