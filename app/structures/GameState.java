package structures;

import structures.basic.*;
import structures.basic.spell.Spell;
import structures.basic.spell.WraithlingSwarm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GameState {
    public static final int INITIAL_HEALTH = 20;
    public static final int INITIAL_MANA = 0;
    public static final int INITIAL_ATTACK = 2;
    public static final int MAXIMUM_MANA = 9;

    public boolean gameInitialised;

    private Grid grid; // Represents the game grid (Tiles on the board)
    private Player humanPlayer; // Human player
    private Player AIPlayer; // AI player
    private Player currentPlayer; // CurrentPlayer
    private int turn; // Tracks the current game turn
    private int handPosition; // Indicates if a card is clicked.
    private Spell spellToCast;
    private Unit currentUnit;
    private Set<Tile> highlightedFriendlyTiles;
    private Set<Tile> highlightedEnemyTiles;
    private static GameState gameState;
    private boolean endTurnClicked;
    private boolean gameEnded;

	// Constructors
    private GameState() {
        // Initialize default values
        this.gameInitialised = false;
        this.turn = 1; // Assuming the game starts with turn 0
        this.handPosition = -1;
        this.spellToCast = null;
        this.highlightedFriendlyTiles = new HashSet<>();
        this.highlightedEnemyTiles = new HashSet<>();
        this.endTurnClicked = false;
        this.gameEnded = false;
    }

    public static GameState getInstance() {
        if (gameState == null) {
            gameState = new GameState();
        }
        return gameState;
    }

    // Getter and setter for gameInitialized
    public boolean isGameInitialised() {
        return gameInitialised;
    }

    public void setGameInitialised(boolean gameInitialised) {
        this.gameInitialised = gameInitialised;
    }

    // Getter and setter for grid
    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    // Getter and setter for players
    public void setHumanPlayer(Player humanPlayer) {
        this.humanPlayer = humanPlayer;
    }

    public Player getHumanPlayer() {
        return humanPlayer;
    }

    public void setAIPlayer(Player AIPlayer) {
        this.AIPlayer = AIPlayer;
    }

    public Player getAIPlayer() {
        return AIPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    // Getter and setter for turn
    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getHandPosition() {
        return handPosition;
    }

    public void setHandPosition(int handPosition) {
        this.handPosition = handPosition;
    }


    public boolean isCardClicked() {
        return this.handPosition != -1;
    }

    public Card getClickedCard() {
        int handPosition = this.getHandPosition();
        if (handPosition < 0) {
        	return null;
        }
        return this.getCurrentPlayer().getMyHandCards().get(handPosition - 1);
    }

    public Spell getSpellToCast() {
        return spellToCast;
    }

    public void setSpellToCast(Spell spellToCast) {
        this.spellToCast = spellToCast;
    }

    public Unit getCurrentUnit() {
        return currentUnit;
    }

    public void setCurrentUnit(Unit currentUnit) {
        this.currentUnit = currentUnit;
    }

    public Set<Tile> getHighlightedFriendlyTiles() {
        return highlightedFriendlyTiles;
    }

    public void setHighlightedFriendlyTiles(Collection<Tile> toHighlightTiles) {
        this.highlightedFriendlyTiles.clear();
        this.highlightedFriendlyTiles.addAll(toHighlightTiles);
    }

    public Set<Tile> getHighlightedEnemyTiles() {
        return highlightedEnemyTiles;
    }

    public void setHighlightedEnemyTiles(Collection<Tile> toHighlightTiles) {
        this.highlightedEnemyTiles.clear();
        this.highlightedEnemyTiles.addAll(toHighlightTiles);
    }

    public boolean isEndTurnClicked() {
		return endTurnClicked;
	}

	public void setEndTurnClicked(boolean endTurnClicked) {
		this.endTurnClicked = endTurnClicked;
	}

    public boolean isGameEnded() {
		return gameEnded;
	}

	public void setGameEnded(boolean gameEnded) {
		this.gameEnded = gameEnded;
	}

	// Method to increment the turn
    public void nextTurn() {
        this.turn++;
    }

    /*
     *  swtichCurrentPlayer()
     *  
     */
    public void switchCurrentPlayer() {
        if (currentPlayer.equals(humanPlayer)) {
            currentPlayer = AIPlayer;
        } else {
            currentPlayer = humanPlayer;
        }
    }
    
    /*
     *  isCurrentPlayerHuman()
     * 
     *  @return boolean - True if current player is human
     */
    public boolean isCurrentPlayerHuman() {
    	return currentPlayer.equals(humanPlayer);
    }
    
    /*
     *  isSpellWraithlingSwarm
     *  
     *  @return boolean - True if current spell to be casted is 
     *  WraithlingSwarm
     *  
     */
    public boolean isSpellWraithlingSwarm() {
        return !(getSpellToCast() == null) && (getSpellToCast() instanceof WraithlingSwarm);
    }
}