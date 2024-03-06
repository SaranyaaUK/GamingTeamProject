package gamelogic;

import structures.basic.Card;
import structures.basic.GameLogic;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.spell.BeamShock;
import structures.basic.spell.Spell;
import structures.basic.spell.SundropElixir;
import structures.basic.spell.TrueStrike;
import utils.TilesGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AIScoring {

    //Here the return result should be a map with only one entry set
    //The key should be a tile, if it's the same as the unit's current tile, it means the unit should not move
    //The value should be a unit, if it's not null, it means my unit should attack this unit
    public static Map<Tile, Unit> getBestActionForUnit(Unit unit) {
        Map<Tile, Unit> bestAction = new HashMap<>();
        List<Tile> movableTiles = TilesGenerator.getMovableTiles(unit);
        movableTiles.add(TilesGenerator.getUnitTile(unit));
        //to store the attackable unit's tile and its attack score
        Map<Tile, Double> attackableTilesScore = new HashMap<>();
        Tile bestTile = null;
        Unit bestAttackTarget = null;
        double maxScore = Double.MIN_VALUE;

        //check every movable tile
        for (Tile movableTile : movableTiles) {
            double scoreForPosition = getScoreForPosition(unit, movableTile);
            List<Tile> adjacentEnemyTiles = TilesGenerator.getAdjacentEnemyTiles(movableTile);
            if (!adjacentEnemyTiles.isEmpty()) {
                //check every unit which can be attack at this movable tile
                for (Tile adjacentEnemyTile : adjacentEnemyTiles) {
                    double scoreForAttack;
                    if (!attackableTilesScore.containsKey(adjacentEnemyTile)) {
                        scoreForAttack = getScoreForUnitAttack(unit, adjacentEnemyTile.getUnit());
                        attackableTilesScore.put(adjacentEnemyTile, scoreForAttack);
                    } else {
                        scoreForAttack = attackableTilesScore.get(adjacentEnemyTile);

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
        bestAction.put(bestTile, bestAttackTarget);
        return bestAction;
    }


    public static Tile findBestPositionForSummonCreature(Card card) {
        Unit unit = GameLogic.getCreatureObject(card);
        List<Tile> tilesToSummon = TilesGenerator.getTilesToSummon();
        Tile bestPosition = null;
        if (!tilesToSummon.isEmpty()) {
            double maxScore = Double.MIN_VALUE;
            for (Tile tile : tilesToSummon) {
                double currentScore = AIScoring.getScoreForPosition(unit, tile);
                if (currentScore > maxScore) {
                    maxScore = currentScore;
                    bestPosition = tile;
                }
            }
        }
        return bestPosition;

    }

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


    //1.consider the health of my avatar
    //check if my avatar's health is under THRESHOLD_HP_GENERAL_RETREAT
    //2.consider the distance from my avatar
    //2.consider the distance of enemy avatar
    //3.consider different ability: flying/zeal/provoke
    public static double getScoreForPosition(Unit unit, Tile position) {
        return 0.0;

    }

    private static double getScoreForUnitAttack(Unit unit, Unit target) {
        //check hp/attack of my unit and target
        //check distance between target and my avatar
        //check if there is any modifier
        //check if it is avatar
        //check if it is lethal
        //if so, check attack and hp change triggered by the deathwatch

        return 0.0;

    }



    private static double getScoreForSpellTarget(Spell spell, Tile target) {
        double score = 0.0;
        if(spell instanceof SundropElixir){
            score = getScoreForSundropElixir(target);
        }
        if(spell instanceof TrueStrike){
            score = getScoreForTrueStrike(target);
        }
        if(spell instanceof BeamShock){
            score = getScoreForBeamShock(target);
        }
        return score;

    }

    //check enemy which is near my avatar
    //check enemy which is have higher attack and health
    private static double getScoreForBeamShock(Tile target) {
        return 0;
    }

    //check enemy avatar
    //check enemy which is near my avatar
    //check enemy which is have higher attack and health
    private static double getScoreForTrueStrike(Tile target) {
        return 0;
    }


    //check my avatar
    //check the unit with high attack
    //check the unit near enemy avatar
    //check how many amount of heal(4) can be used
    private static double getScoreForSundropElixir(Tile target) {
        return 0;
    }


}
