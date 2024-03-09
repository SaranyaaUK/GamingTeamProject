package gamelogic;

/**
 * Constants for AI Scoring Logic in Duelyst
 * This class defines a series of constants used within the AI scoring system. These constants are utilized to evaluate
 * actions and positions of units on the battlefield, guiding the AI's decision-making process to create challenging and
 * strategic gameplay. Each constant serves a specific purpose in the scoring algorithms, affecting how units are valued
 * based on their health, attack strength, positioning, and special abilities or conditions.
 *
 * Note: The values and structure outlined in this file are part of the open-source codebase for Duelyst.
 */


public class BOUNTY {
    // General
    public static final double UNIT_HP = 1.2;
    public static final double GENERAL_HP = 1.4;
    public static final double GENERAL_HP_WHEN_LETHAL = 9999;
    public static final double UNIT_ATK = 1.0;
    public static final double GENERAL_ATK = 1.0;

    // Unit-specific
    public static final double DISTANCE_FROM_MY_GENERAL = -0.05;
    public static final double DISTANCE_FROM_MY_GENERAL_FACTOR = 20;
    public static final double DISTANCE_FROM_OPPONENT_GENERAL = -0.05;

    // Positioning
    public static final double PROVOKE_PER_UNIT = 1;
    public static final double FLYING_DISTANT = 10;


    // Removal/disable
    public static final double REMOVAL_PER_UNIT_SCORE = 5.0;
    public static final double STUN_PER_UNIT_ATK = 2.0;


    // Damage
    public static final double DAMAGE_PER_UNIT_SCORE = 0.5;
    public static final double DAMAGE_PER_GENERAL_HP = 2.0;


    // Healing
    public static final double HEALING_PER_UNIT_SCORE = 1.0;
    public static final double HEALING_OVERHEAL = -6.0;
    public static final double HEALING_PER_UNIT_DAMAGE = 1.25;
    public static final double HEALING_PER_GENERAL_DAMAGE = 1.5;


    //Modifier
    public static final int MODIFIER_DEATHWATCH = 30;
    public static final int MODIFIER_FLYING = 10;
    public static final int MODIFIER_OPENINGGAMBIT = 10;
    public static final int MODIFIER_PROVOKE = 10;
    public static final int MODIFIER_RUSH = 10;
    public static final int MODIFIER_ZEAL = 30;


}
