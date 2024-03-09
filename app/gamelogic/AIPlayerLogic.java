package gamelogic;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.spell.Spell;
import utils.TilesGenerator;

import java.util.*;

public class AIPlayerLogic implements Runnable {
    GameState gameState;
    ActorRef out;


    @Override
    public void run() {
        Player aiPlayer = gameState.getAIPlayer();

        //First check all the units on board and find their best action
        ArrayList<Unit> myUnits = aiPlayer.getMyUnits();
        for (Unit unit : myUnits) {
            Map<Tile, Unit> bestActionForUnit = AIScoring.getBestActionForUnit(unit);
            Tile moveTarget = (Tile) bestActionForUnit.keySet().toArray()[0];
            if (moveTarget != TilesGenerator.getUnitTile(unit)) {
                Actions.unitMove(out, unit, moveTarget);
            }
            Unit attackTarget = bestActionForUnit.get(moveTarget);
            if (bestActionForUnit.get(moveTarget) != null) {
                Actions.unitAttack(out, unit, attackTarget);
            }
        }

        List<Card> myHandCards = aiPlayer.getMyHandCards();
        //high-mana-cost card first
        myHandCards.sort(Comparator.comparingInt(Card::getManacost));

        //First try to use spell cards as many as possible
        for (Card card : myHandCards) {
            if (card instanceof Spell) {
                if (aiPlayer.getMana() <= card.getManacost()) {
                    break;
                }
                Tile bestTargetForSpellCard = AIScoring.findBestTargetForSpellCard((Spell) card);
                if (bestTargetForSpellCard != null) {
                    ProcessTileClicked.processCardUse(out, bestTargetForSpellCard, card);
                }
            }
        }

        //Then try to use creature cards as many as possible

        for (Card card : myHandCards) {
            if (!(card instanceof Spell)) {
                if (aiPlayer.getMana() <= card.getManacost()) {
                    break;
                }
                Tile bestPositionForSummonCreature = AIScoring.findBestPositionForSummonCreature(card);
                if (bestPositionForSummonCreature != null) {
                    ProcessTileClicked.processCardUse(out, bestPositionForSummonCreature, card);
                }
            }
        }

        ProcessEndTurnClicked.processEndTurnClicked(out);
    }
}
