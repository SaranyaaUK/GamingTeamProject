package gamelogic;

public class Bounty {
    // General
    public static final double MANA_COST = 1.0;
    public static final double UNIT_HP = 1.2;
    public static final double MY_GENERAL_RETREAT = 9999;
    public static final double MY_GENERAL_RETREAT_V2 = -12.0;
    public static final double GENERAL_HP = 1.4;
    public static final double GENERAL_HP_WHEN_LETHAL = 9999;
    public static final double UNIT_ATK = 1.0;
    public static final double GENERAL_ATK = 1.0;
    public static final double ARTIFACT_DURABILITY = 1;
    public static final double MODIFIER = 2;
    public static final double WATCH_WITH_TRIGGER = 30;
    public static final double THRESHOLD_COUNTERATTACK_HP_PCT_GENERAL = 0.3;
    public static final double THRESHOLD_HP_GENERAL_RETREAT = 10.0;

    // Unit-specific
    public static final double DISTANCE_FROM_BEST_ENEMY_TARGET = -0.5;
    public static final double DISTANCE_FROM_BEST_ENEMY_TARGET_RANGED = 3;
    public static final double DISTANCE_FROM_BEST_ENEMY_TARGET_BLASTATTACK_X = 3;
    public static final double DISTANCE_FROM_BEST_ENEMY_TARGET_BLASTATTACK_Y = -3;
    public static final double DISTANCE_FROM_BEST_ENEMY_TARGET_BLASTATTACK_X_V2 = -6;
    public static final double DISTANCE_FROM_BEST_ENEMY_TARGET_EVASIVE = 3;
    public static final double BUFFER_HP_EVASIVE_THRESHOLD = 5;
    public static final double GENERAL_HP_EVASIVE_THRESHOLD = 10;
    public static final double DISTANCE_FROM_NEAREST_BONUS_MANA = -2;
    public static final double DISTANCE_FROM_PROVOKE = -3;
    public static final double QUANTITY_SURROUNDING_ENEMIES_HIGH_HP = 0.05;
    public static final double QUANTITY_SURROUNDING_ENEMIES_LOW_HP = -0.05;
    public static final double HIGH_HP_THRESHOLD = 4;
    public static final double DISTANCE_FROM_MY_GENERAL = -0.05;
    public static final double DISTANCE_FROM_MY_GENERAL_RETREATING = -10.0;
    public static final double DISTANCE_FROM_MY_GENERAL_FACTOR = 20;
    public static final double DISTANCE_FROM_OPPONENT_GENERAL = -0.05;
    public static final double DISTANCE_FROM_OPPONENT_GENERAL_WHEN_LETHAL = -99;

    // Positioning
    public static final double DISTANCE_FROM_BACKSTAB = -3;
    public static final double FRENZY_PER_UNIT = 1;
    public static final double PROVOKE_PER_UNIT = 1;
    public static final double PROVOKE_ENEMY_GENERAL = 47;
    public static final double ZEAL_ACTIVE = 0.7;
    public static final double NO_FOLLOWUP_TARGET = -20;

    // Attack
    public static final double TARGET_ENEMIES_IN_SAME_ROW = 15;
    public static final double TARGET_AT_RANGE = 25;
    public static final double TARGET_BACKSTAB_PROC = 30;
    public static final double TARGET_COUNTERATTACK_NOT_LETHAL = 15;

    // Removal/disable
    public static final double REMOVAL_PER_UNIT_SCORE = 5.0;
    public static final double REMOVAL_OVERKILL = -6.0;
    public static final double DISPEL_PER_UNIT_SCORE = 3.25;
    public static final double DISPEL_WASTED = -5;
    public static final double UNIT_SCORE_TONED_DOWN = 0.10;
    public static final double STUN_PER_UNIT_ATK = 2.0;
    public static final double STUN_WASTED = -5;
    public static final double UNIT_TRANSFORM = 2.0;

    // Damage
    public static final double DAMAGE_PER_UNIT_SCORE = 0.5;
    public static final double DAMAGE_PER_GENERAL_HP = 2.0;
    public static final double SHADOW_CREEP_DAMAGE = -2;
    public static final double DAMAGE_LETHAL = 40;
    public static final double FORCEFIELD_POP = 5;

    // Healing
    public static final double HEALING_PER_UNIT_SCORE = 1.0;
    public static final double HEALING_OVERHEAL = -6.0;
    public static final double HEALING_PER_UNIT_DAMAGE = 1.25;


}
