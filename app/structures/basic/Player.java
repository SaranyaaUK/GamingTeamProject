package structures.basic;

import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class Player {

	private int health;
	private int mana;
	private CardManager myCardManager;
	private Unit myAvatar;

	public Player(int health, int mana) {
		this.health = health;
		this.mana = mana;
		this.myCardManager = new CardManager();
		loadAvatar();
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

	public String getAvatarConfig() {
		return StaticConfFiles.humanAvatar;
	}

	public Unit getAvatar() {
		return myAvatar;
	}

	private void loadAvatar() {
		int humanAvatarID = 41; // This assumes avatars are uniquely identified
		myAvatar = BasicObjectBuilders.loadUnit(getAvatarConfig(), humanAvatarID, Unit.class);
	}

	public CardManager getCardManager() {
		return myCardManager;
	}
}