package structures;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import structures.basic.Card;
import structures.basic.Grid;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.spell.Spell;

public class GameState {
    public static final int INITIAL_HEALTH = 20;
    public static final int INITIAL_MANA = 0;
    public static final int INITIAL_ATTACK = 2;

    public boolean gameInitialised = false;
    public boolean something = false;

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

    // Constructors
    private GameState() {
        // Initialize default values
        this.gameInitialised = false;
        this.turn = 1; // Assuming the game starts with turn 0
        this.handPosition = -1;
        this.spellToCast = null;
        this.highlightedFriendlyTiles = new HashSet<Tile>();
        this.highlightedEnemyTiles = new HashSet<Tile>();
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

    public void setGameInitialised(boolean gameInitialized) {
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

    public void resetHandPosition() {
        handPosition = -1;
    }

    public boolean isCardClicked() {
        return this.handPosition != -1;
    }

    public Card getClickedCard() {
        int handPosition = this.getHandPosition();
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

    // Method to increment the turn
    public void nextTurn() {
        this.turn++;
        // Additional logic for turn transition can be added here
        // For example, updating player mana based on the new turn
        updatePlayersMana();
    }

    // method to update players' mana at the start of each turn
    private void updatePlayersMana() {
        if (humanPlayer != null) {
            humanPlayer.setMana(this.turn + 1); // Assuming mana is turn + 1
        }
        if (AIPlayer != null) {
            AIPlayer.setMana(this.turn + 1);
        }
    }

    public void switchCurrentPlayer() {
        if (currentPlayer == humanPlayer) {
            currentPlayer = AIPlayer;
        } else {
            currentPlayer = humanPlayer;
        }
    }


}