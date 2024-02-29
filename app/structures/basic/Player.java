package structures.basic;

import java.util.List;
import java.util.ArrayList;
import structures.GameState;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/**
 * A basic representation of of the Player. A player
 * has health and mana.
 * 
 * @author Dr. Richard McCreadie
 *
 */

public class Player {

	private int health;
	private int mana;
	// Player's cardManager to keep track of deck Cards and hand Cards
	private CardManager myCardManager;
	// Player's avatar representation
	private Unit myAvatar;
	// Player's units
	private ArrayList<Unit> myUnits;

	public Player () {
		this(GameState.INITIAL_HEALTH, GameState.INITIAL_MANA);
	}

	public Player(int health, int mana) {
		this.health = health;
		this.mana = mana;
		// Initialise the units list
		this.myUnits = new ArrayList<Unit>();
		// Set the CardManager and the avatar
		setCardManager(new CardManager(true));
		setAvatar(loadAvatar());
		
	}

	// Getter and Setters
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public Unit getAvatar() {
		return myAvatar;
	}

	public void setAvatar(Unit avatar) {
		myAvatar = avatar;
	}

	public CardManager getCardManager() {
		return myCardManager;
	}
	
	public void setCardManager(CardManager cardManager) {
		this.myCardManager = cardManager;
	}
	
	public ArrayList<Unit> getMyUnits() {
		return myUnits;
	}

	public void addUnits(Unit unit) {
		this.myUnits.add(unit);
	}

	/*
	 *  Get the avatar's configuration file
	 */
	public String getAvatarConfig() {
		return StaticConfFiles.humanAvatar;
	}

	/*
	 *  Loads the avatar using the config file
	 */
	private Unit loadAvatar() {
		int humanAvatarID = getAvatarID(); // To uniquely identify the avatar
		Unit avatar = BasicObjectBuilders.loadUnit(getAvatarConfig(), humanAvatarID, Unit.class);
		this.addUnits(avatar);
		return avatar;
	}

	/*
	 *  @returns True if the player's deck is empty
	 */
	public boolean isMyDeckEmpty() {
		return myCardManager.isDeckEmpty();
	}
	
	/*
	 *  @returns the player's hand cards
	 */
	public List<Card> getMyHandCards() {
		return myCardManager.getHandCards();
	}
	
	/*
	 *  @returns  A unique id to identify the avatar
	 */
	public int getAvatarID() {
		return 41;
	}
}