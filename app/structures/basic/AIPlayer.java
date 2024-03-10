package structures.basic;


import utils.StaticConfFiles;

/**
 * A basic representation of the AI Player. AI player
 * extends from the player.
 * <p>
 * AI player related decision logic goes here.
 *
 * @author Dr. Richard McCreadie
 */

public class AIPlayer extends Player {

    public AIPlayer() {
        super(false);
        this.setCardManager(new CardManager(false));
    }


    @Override
    public String getAvatarConfig() {
        return StaticConfFiles.aiAvatar;
    }


    /**
     * getAvatarID()
     *
     * @returns A unique id to identify the AI's avatar
     */
    public int getAvatarID() {
        return 42;
    }

}
