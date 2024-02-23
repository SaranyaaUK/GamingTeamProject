package structures.basic;

import utils.StaticConfFiles;
import utils.BasicObjectBuilders;

/**
 * A basic representation of of the Player. A player
 * has health and mana.
 * 
 * @author Dr. Richard McCreadie
 *
 */

/*
 * Signature
 * 
 * Attributes
 * CardManager myCardManager
 * Unit myAvatar
 * 
 * Methods
 * CardManager getCardManager()
 * void loadAvatar()
 * Unit getAvatar()
 * String getAvatarConfig()
 * 
 */

public class Player {

	int health;
	int mana;
	CardManager myCardManager;
	Unit myAvatar;

	public Player() {
		this(20, 0);
	}

	public Player(int health, int mana) {
		super();
		this.health = health;
		this.mana = mana;
		this.loadAvatar();
	}

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

	/*
	 * Returns the Player's avatar configuration
	 */
	public String getAvatarConfig() {
		return StaticConfFiles.humanAvatar;
	}

	/*
	 * Returns the Player's avatar (unit) object
	 */
	public Unit getAvatar() {
		return this.myAvatar;
	}

	/*
	 * Loads the Player's avatar (unit) object
	 */
	public void loadAvatar() {
		// The human player avatar will have the id of 41 (1-40 for each of the cards)
		int humanAvatarID = 41;
		this.myAvatar = BasicObjectBuilders.loadUnit(this.getAvatarConfig(), humanAvatarID, Unit.class);
	}

	/*
	 * Returns the Player's card manager (deck and hand cards)
	 */
	public CardManager getCardManager() {
		return this.myCardManager;
	}

	/*
	 * Returns the player's deck status
	 */
	public boolean isDeckEmpty() {

		return false;
		// return this.getCardManager().isDeckEmpty();
	}

}
