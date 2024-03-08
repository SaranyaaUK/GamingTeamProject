package structures.basic;


import structures.GameState;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/**
 * A basic representation of the AI Player. AI player
 * extends from the player.
 * 
 * AI player related decision logic goes here.
 * 
 * @author Dr. Richard McCreadie
 *
 */

public class AIPlayer extends Player {

    public AIPlayer() {
        super();
        this.setAvatar(loadAvatar());
    }

    public AIPlayer(int health, int mana) {
        super(health, mana);
        this.setCardManager(new CardManager(false));
        this.setAvatar(loadAvatar());
    }
    
    @Override
    /**
     *  getAvatarConfig()
     *  
	 *  Get the avatar's configuration file
	 *  
	 *  @return String
	 */
    public String getAvatarConfig() {
        return StaticConfFiles.aiAvatar;
    }

    /**
     *  loadAvatar()
     *  
	 *  Loads the avatar using the config file
	 *  
	 *  @return Unit
	 */
    private Unit loadAvatar() {
        int aiAvatarID = getAvatarID(); // This assumes avatars are uniquely identified
        Unit avatar = BasicObjectBuilders.loadUnit(getAvatarConfig(), aiAvatarID, Unit.class);
		this.addUnits(avatar);
		avatar.setHealth(GameState.INITIAL_HEALTH);
		avatar.setAttack(GameState.INITIAL_ATTACK);
		avatar.setMaximumHealth(avatar.getHealth());
		avatar.setHumanUnit(false);
		avatar.setExhausted(false);
		avatar.setMoved(false);
		return avatar;
    }
    
    /**
     *  getAvatarID()
     *  
	 *  @returns  A unique id to identify the AI's avatar
	 */
	public int getAvatarID() {
		return 42;
	}

}
