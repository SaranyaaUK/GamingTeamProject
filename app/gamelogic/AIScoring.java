package gamelogic;

import structures.GameState;
import structures.basic.*;
import structures.basic.creatures.*;
import structures.basic.spell.BeamShock;
import structures.basic.spell.Spell;
import structures.basic.spell.SundropElixir;
import structures.basic.spell.TrueStrike;
import utils.TilesGenerator;

import java.util.*;

/**
 * The AIScoring class is responsible for determining the most advantageous actions for AI-controlled units
 * within the game. It evaluates potential moves and attacks to generate a strategic advantage based on the
 * current game state. This class utilizes a scoring system to assess and compare the effectiveness of various
 * actions,including moving units, attacking enemy units, summoning creatures, and using spell cards.
 */

public class AIScoring {

    /**
     * Determines the best action for a given unit, considering all possible moves and attacks.The method
     * evaluates all movable tiles for the unit, including its current position, and calculates a score for
     * each potential action based on strategic positioning and the opportunity to attack enemy units.
     * The highest-scoring action is selected, with the chosen tile and target unit (if any) returned in a map.
     *
     * @param unit The unit for which to determine the best action.
     * @return A map with one entry: the key is the best tile for the unit to move to (or its current tile to
     * stay),and the value is the enemy unit to attack (or null if no attack should be made).
     */
    public static Map<Tile, Unit> getBestActionForUnit(Unit unit) {
        System.out.println("Now try to getBestActionForUnit!" + unit.getId());
        Map<Tile, Unit> bestAction = new HashMap<>();
        List<Tile> movableTiles = TilesGenerator.getMovableTiles(unit);
        System.out.println("movableTileSize: " + movableTiles.size());
        movableTiles.add(TilesGenerator.getUnitTile(unit));
        //to store the attackable unit's tile and its attack score
        Map<Tile, Double> attackableTilesScore = new HashMap<>();
        Tile bestTile = null;
        Unit bestAttackTarget = null;
        double maxScore = -2000;

        //check every movable tile
        for (Tile movableTile : movableTiles) {
            double scoreForPosition = getScoreForPosition(unit, movableTile);

            System.out.println(" " + unit.getId() + "-----" + scoreForPosition + "-----");

            List<Tile> adjacentEnemyTiles = TilesGenerator.getAdjacentEnemyTiles(movableTile);
            System.out.println("adjacentEnemyTilesSize: " + adjacentEnemyTiles.size());
            if (!adjacentEnemyTiles.isEmpty()) {
                System.out.println("adjacentEnemyTiles is not empty");
                //check every unit which can be attack at this movable tile
                for (Tile adjacentEnemyTile : adjacentEnemyTiles) {
                    double scoreForAttack;
                    if (!attackableTilesScore.containsKey(adjacentEnemyTile)) {
                        scoreForAttack = getScoreForUnitAttack(unit, adjacentEnemyTile.getUnit());
                        attackableTilesScore.put(adjacentEnemyTile, scoreForAttack);
                    } else {
                        scoreForAttack = attackableTilesScore.get(adjacentEnemyTile);
                        System.out.println("scoreForAttack: " + scoreForAttack);

                    }
                    if (scoreForAttack > 0) {
                        if (scoreForAttack + scoreForPosition > maxScore) {
                            bestTile = movableTile;
                            bestAttackTarget = adjacentEnemyTile.getUnit();
                            maxScore = scoreForAttack + scoreForPosition;
                        }
                    } else {
                        if (scoreForPosition > maxScore) {
                            maxScore = scoreForAttack + scoreForPosition;
                            bestTile = movableTile;
                            bestAttackTarget = null;
                        }
                    }
                }
            }
        }
        if (bestTile == null) {
            bestAction.put(TilesGenerator.getUnitTile(unit), bestAttackTarget);

        } else {
            bestAction.put(bestTile, bestAttackTarget);

        }

        return bestAction;
    }


    /**
     * Finds the best position to summon a creature based on the given card.
     * Evaluates potential summoning tiles and selects the position that maximizes strategic advantage,
     * considering factors such as proximity to enemies and allies.
     *
     * @param card The card representing the creature to be summoned.
     * @return The best tile for summoning the creature, or null if no suitable position is found.
     */
    public static Tile findBestPositionForSummonCreature(Card card) {
        Unit unit = GameLogic.getCreatureObject(card);
        //System.out.println("findBestPositionForSummonCreature " + unit.getId());
        List<Tile> tilesToSummon = TilesGenerator.getTilesToSummon();
        //System.out.println(tilesToSummon.size());
        Tile bestPosition = null;
        if (!tilesToSummon.isEmpty()) {
            double maxScore = -2000;
            for (Tile tile : tilesToSummon) {
                double currentScore = AIScoring.getScoreForPosition(unit, tile);
                // System.out.println("findBestPositionForSummonCreature score" +currentScore );
                if (currentScore > maxScore) {
                    //System.out.println("now I find a place" +tile );
                    maxScore = currentScore;
                    bestPosition = tile;
                }
            }

        }
        return bestPosition;

    }


    /**
     * Identifies the optimal target tile for a spell card, aiming to maximize the spell's impact.
     * Evaluates potential targets based on the spell's effects and selects the tile that offers
     * the greatest strategic value.
     *
     * @param spell The spell card to evaluate for targeting.
     * @return The best target tile for the spell, or null if no suitable target is found.
     */
    public static Tile findBestTargetForSpellCard(Spell spell) {
        List<Tile> targets = spell.getTargetTilesToHighlight();
        Tile bestTarget = null;
        if (!targets.isEmpty()) {
            double maxScore = Double.MIN_VALUE;
            for (Tile tile : targets) {
                double currentScore = getScoreForSpellTarget(spell, tile);
                if (currentScore > maxScore) {
                    maxScore = currentScore;
                    bestTarget = tile;
                }
            }
        }
        return bestTarget;
    }


    /**
     * Calculates a score for positioning a unit on a specific tile, considering various strategic factors.
     * Factors include proximity to both the AI's and the opponent's avatars, the unit's abilities (e.g., flying, provoke),
     * and the tactical importance of the tile's location.
     *
     * @param unit     The unit to be evaluated.
     * @param position The tile position to evaluate.
     * @return A score reflecting the strategic value of placing the unit on the specified tile.
     */
    public static double getScoreForPosition(Unit unit, Tile position) {
        //System.out.println("AIScoring getScoreForPosition: " + unit.getId() + position.getTilex() + position.getTiley());
        double score = 0;
        GameState gameState = GameState.getInstance();
        score += getScoreForProximityToAvatars(position, gameState.getHumanPlayer().getAvatar(), gameState.getAIPlayer().getAvatar());
        if (unit instanceof Provoke) {
            score += getBountyForProvokePosition(position);
        }
        if (unit instanceof Flying) {
            score += getBountyForFlyingPosition(position);
        }
        return score;
    }


    /**
     * Calculates a bonus score for units with the Flying ability based on their position.
     * Flying units may receive a bonus for being distant from enemy units.
     *
     * @param position The tile position of the flying unit to evaluate.
     * @return A double representing the bounty score for being in a position away from enemy units.
     */
    private static double getBountyForFlyingPosition(Tile position) {
        List<Tile> adjacentEnemyTiles = TilesGenerator.getAdjacentEnemyTiles(position);
        if (adjacentEnemyTiles.isEmpty()) {
            return BOUNTY.FLYING_DISTANT;
        }
        return 0;

    }


    /**
     * Calculates a bonus score for units with the Provoke ability based on their position.
     * Provoke units receive a bonus for being adjacent to enemy units, encouraging positions that maximize
     * their ability to disrupt enemy movements and attacks.
     *
     * @param position The tile position of the provoke unit to evaluate.
     * @return A double representing the bounty score for being adjacent to enemy units, enhancing the Provoke ability's impact.
     */
    private static double getBountyForProvokePosition(Tile position) {
        List<Tile> adjacentEnemyTiles = TilesGenerator.getAdjacentTiles(position);
        if (!adjacentEnemyTiles.isEmpty()) {
            return adjacentEnemyTiles.size() * BOUNTY.PROVOKE_PER_UNIT;
        }
        return 0;
    }

    /**
     * Calculates a score for a unit based on its proximity to both avatars. This method encourages strategic
     * positioning relative to both the friendly and enemy avatars, factoring in health and the tactical
     * advantages of closeness or distance.
     *
     * @param unit        The unit whose positioning to evaluate.
     * @param humanAvatar The human player's avatar.
     * @param AIAvatar    The AI player's avatar.
     * @return A double representing the calculated score based on proximity to avatars.
     */
    private static double getScoreForProximityToAvatars(Unit unit, Unit humanAvatar, Unit AIAvatar) {
        double score = 0;
        Tile target = TilesGenerator.getUnitTile(unit);
        Tile humanAvatarTile = TilesGenerator.getUnitTile(humanAvatar);
        Tile AIAvatarTile = TilesGenerator.getUnitTile(humanAvatar);
        if (unit.isHumanUnit()) {
            score += getDistanceBetweenTiles(target, humanAvatarTile) * getBountyfForDistanceFromMyGeneral(humanAvatar.getHealth());
            score += getDistanceBetweenTiles(target, AIAvatarTile) * BOUNTY.DISTANCE_FROM_OPPONENT_GENERAL;
        } else {
            score += getDistanceBetweenTiles(target, AIAvatarTile) * getBountyfForDistanceFromMyGeneral(AIAvatar.getHealth());
            score += getDistanceBetweenTiles(target, humanAvatarTile) * BOUNTY.DISTANCE_FROM_OPPONENT_GENERAL;
        }
        return score;
    }

    private static double getScoreForProximityToAvatars(Tile targetTile, Unit humanAvatar, Unit AIAvatar) {
       // System.out.println("AIScoring getScoreForProximityToAvatars");
        double score = 0;
        Tile humanAvatarTile = TilesGenerator.getUnitTile(humanAvatar);
        Tile AIAvatarTile = TilesGenerator.getUnitTile(AIAvatar);
        score += getDistanceBetweenTiles(targetTile, AIAvatarTile) * getBountyfForDistanceFromMyGeneral(AIAvatar.getHealth());
        score += getDistanceBetweenTiles(targetTile, humanAvatarTile) * BOUNTY.DISTANCE_FROM_OPPONENT_GENERAL;
        return score;
    }


    /**
     * Calculates a modifier for a unit's score based on its distance from its own avatar and the avatar's current
     * health. A lower health value increases the importance of being close to protect the avatar, reflected in a
     * higher score.
     *
     * @param myGeneralHealth The health of the avatar (general) belonging to the unit's side.
     * @return A double representing the distance-based modifier for the unit's score, adjusted by the avatar's health.
     */
    private static double getBountyfForDistanceFromMyGeneral(int myGeneralHealth) {
        return BOUNTY.DISTANCE_FROM_MY_GENERAL * (BOUNTY.DISTANCE_FROM_MY_GENERAL_FACTOR / myGeneralHealth);
    }


    /**
     * Calculates the net attack score for a unit attacking a target, considering both the potential damage dealt to
     * the target and the counter-damage the attacking unit might receive.
     *
     * @param unit   The attacking unit.
     * @param target The unit being attacked.
     * @return A double value representing the net score of the attack, with positive values indicating favorable outcomes.
     */
    private static double getScoreForUnitAttack(Unit unit, Unit target) {
        //check hp/attack of my unit and target
        //check distance between target and my avatar
        //check if there is any modifier
        //check if it is avatar
        //check if it is lethal
        //if so, check attack and hp change triggered by the deathwatch

        double myAtkDamage = getDamageScore(target, unit.getAttack());
        double enemyAtkDamage = getDamageScore(unit, target.getAttack());

        return myAtkDamage - enemyAtkDamage;

    }

    /**
     * Calculates a score for dealing damage to a unit, taking into account the target's strategic value, its health,
     * and whether the damage could be lethal. This score reflects the effectiveness of the attack, considering potential
     * benefits like reducing enemy forces or even eliminating high-value targets. Additional calculations include
     * adjustments for attacking avatars and considerations for abilities that trigger upon death.
     *
     * @param targetUnit The unit receiving damage.
     * @param ATK        The attack value of the unit dealing damage.
     * @return A double value representing the score of dealing damage
     */
    public static double getDamageScore(Unit targetUnit, int ATK) {
        GameState gameState = GameState.getInstance();
        double score = 0;

        double targetScore = 0.05 * getScoreForUnit(targetUnit) * getScoreForPosition(targetUnit, TilesGenerator.getUnitTile(targetUnit));
        int targetHealth = targetUnit.getHealth();
        int remainingHealth = ATK - targetHealth;
        boolean isAvatar = (targetUnit == gameState.getHumanPlayer().getAvatar() || targetUnit == gameState.getAIPlayer().getAvatar());
        int hpLoss = Math.min(ATK, targetHealth);


        score += targetScore * BOUNTY.DAMAGE_PER_UNIT_SCORE;

        // prefer damage that may be lethal
        if (remainingHealth <= 0) {
            score += targetScore * BOUNTY.REMOVAL_PER_UNIT_SCORE;
            score += hpLoss * BOUNTY.DAMAGE_PER_UNIT_SCORE;
            //calculate score for deathwatch
            List<Unit> humanUnits = gameState.getHumanPlayer().getMyUnits();
            for (Unit unit : humanUnits) {
                if (unit != targetUnit && unit instanceof DeathWatch) {
                    score -= getDeathWatchScore(unit);
                }
            }
        }

        // prefer damage on enemy avatar
        if (isAvatar) {
            score += Math.max(1.0, 25.0 / targetHealth) * ATK * BOUNTY.DAMAGE_PER_GENERAL_HP;
            if (remainingHealth < 0) {
                score += BOUNTY.GENERAL_HP_WHEN_LETHAL;
            }
        }

        return score;

    }

    private static double getScoreForSpellTarget(Spell spell, Tile target) {
        double score = 0.0;
        if (spell instanceof SundropElixir) {
            score = getScoreForSundropElixir(target);
        }
        if (spell instanceof TrueStrike) {
            score = getScoreForTrueStrike(target);
        }
        if (spell instanceof BeamShock) {
            score = getScoreForBeamShock(target);
        }
        return score;

    }

    private static double getScoreForBeamShock(Tile target) {
        return getScoreForStun(target.getUnit());
    }

    private static double getScoreForTrueStrike(Tile target) {
        return getDamageScore(target.getUnit(), 2);
    }


    private static double getScoreForSundropElixir(Tile target) {
        return getScoreForHeal(target.getUnit(), 4);
    }


    /**
     * Calculates the overall score for a given unit, taking into account its health, attack values, and special modifiers.
     * This method differentiates between general units (avatars) and regular units, applying different bounty values to
     * their health and attack scores accordingly.
     *
     * @param unit The unit for which to calculate the score.
     * @return A double representing the calculated score, reflecting the unit's potential impact on the game.
     */
    private static double getScoreForUnit(Unit unit) {
        GameState gameState = GameState.getInstance();
        double score = 0;
        if (unit == gameState.getAIPlayer().getAvatar() || unit == gameState.getHumanPlayer().getAvatar()) {
            score += (unit.getHealth() * BOUNTY.GENERAL_HP) + (unit.getAttack() * BOUNTY.GENERAL_ATK);
        } else {
            score += (unit.getHealth() * BOUNTY.UNIT_HP) + (unit.getAttack() * BOUNTY.UNIT_ATK);
        }
        // add modifier score
        score += getScoreForModifiers(unit);
        return score;

    }

    private static double getScoreForModifiers(Unit unit) {
        double score = 0;
        if (unit instanceof Provoke) {
            score += BOUNTY.MODIFIER_PROVOKE;
        }
        if (unit instanceof DeathWatch) {
            score += BOUNTY.MODIFIER_DEATHWATCH;
        }
        if (unit instanceof Zeal) {
            score += BOUNTY.MODIFIER_ZEAL;
        }
        if (unit instanceof OpeningGambit) {
            score += BOUNTY.MODIFIER_OPENINGGAMBIT;
        }
        if (unit instanceof Rush) {
            score += BOUNTY.MODIFIER_RUSH;
        }
        if (unit instanceof Flying) {
            score += BOUNTY.MODIFIER_FLYING;
        }
        return score;
    }

    /**
     * Calculates the score for healing a unit, taking into account the amount of damage the unit has taken,
     * the unit's overall value based on its health and attack, and whether the unit is an avatar (general) or not.
     *
     * @param unit       The unit to potentially heal.
     * @param healAmount The amount of health to be restored.
     * @return A double representing the calculated score for healing the unit, factoring in its strategic value and
     * the efficiency of the healing amount relative to its current health deficit.
     */

    private static double getScoreForHeal(Unit unit, int healAmount) {
        GameState gameState = GameState.getInstance();
        double score = 0;
        int damageTaken = unit.getMaximumHealth() - unit.getHealth();
        if (damageTaken > 0) {
            double unitScore = getScoreForUnit(unit);

            //prefer to heal high-value units
            score += unitScore * BOUNTY.HEALING_PER_UNIT_SCORE;
            if (unit == gameState.getAIPlayer().getAvatar() || unit == gameState.getHumanPlayer().getAvatar()) {
                score += Math.max(1.0, (double) 25 / unit.getHealth()) * (damageTaken * 0.5) * BOUNTY.HEALING_PER_GENERAL_DAMAGE;
            } else {
                score += Math.max(1.0, (unit.getMaximumHealth() * 0.5) / unit.getHealth()) * damageTaken * BOUNTY.HEALING_PER_UNIT_DAMAGE;
            }

            double overHealing = -Math.min(0, damageTaken - healAmount);
            score += overHealing * BOUNTY.HEALING_OVERHEAL;
        }
        return score;
    }

    private static double getScoreForStun(Unit unit) {
        double score = 0;
        if (!unit.isStunned()) {
            score += unit.getAttack() * BOUNTY.STUN_PER_UNIT_ATK;
        }
        return score;
    }

    private static int getDistanceBetweenTiles(Tile tile, Tile target) {
        return Math.abs(tile.getXpos() - target.getXpos()) + Math.abs(tile.getYpos() - target.getYpos());
    }


    public static double getDeathWatchScore(Unit unit) {
        double score = 0;
        if (unit instanceof BadOmen) {
            score += 1 * getScoreForUnit(unit) * BOUNTY.UNIT_ATK;
        }
        if (unit instanceof ShadowWatcher) {
            score += 1 * getScoreForUnit(unit) * BOUNTY.UNIT_ATK;
            score += 1 * getScoreForUnit(unit) * BOUNTY.UNIT_HP;
        }
        if (unit instanceof BloodmoonPriestess) {
            List<Tile> adjacentTiles = TilesGenerator.getAdjacentTiles(unit);
            if (adjacentTiles.size() != 0) {
                score += 2;
            }
        }
        if (unit instanceof ShadowDancer) {
            score += getScoreForUnit(unit) * BOUNTY.GENERAL_HP;
            score += getScoreForHeal(unit, 1);
        }
        return score;
    }

}
