package gamelogic;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.*;
import structures.basic.creatures.Rush;
import structures.basic.spell.Spell;
import utils.TilesGenerator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class AIPlayerLogic {
    GameState gameState;
    ActorRef out;

    public AIPlayerLogic(ActorRef out, GameState gameState) {
        this.out = out;
        this.gameState = gameState;
    }


    public void run() {
        Player aiPlayer = gameState.getAIPlayer();

        //First check all the units on board and find their best action
        List<Unit> myUnits = aiPlayer.getMyUnits();
        for (Unit unit : myUnits) {

            executeBestAction(unit);

        }
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Card> myHandCards = aiPlayer.getMyHandCards();

        //high-mana-cost card first
        myHandCards.sort(Comparator.comparingInt(Card::getManacost).reversed());

        //First try to use spell cards as many as possible
        for (Card card : myHandCards) {
            if (!card.isCreature()) {
                //System.out.println("now try to use Spell card: " + card.getCardname());
                if (aiPlayer.getMana() < card.getManacost()) {
                    continue;
                }
                Spell spell = GameLogic.getSpellObject(card.getId());
                gameState.setSpellToCast(spell);
                Tile bestTargetForSpellCard = AIScoring.findBestTargetForSpellCard(spell);
                if (bestTargetForSpellCard != null) {
                    System.out.println("Cast spell" + "---------" + card.getCardname());
                    gameState.setHandPosition(myHandCards.indexOf(card) + 1);
                    ProcessTileClicked.processCardUse(out, bestTargetForSpellCard, card);
                }
            }
        }

        //Then try to use creature cards as many as possible

        for (Card card : myHandCards) {
            if (card.isCreature()) {
                System.out.println("now try to summon a unit: " + card.getCardname());
                if (aiPlayer.getMana() < card.getManacost()) {
                    continue;
                }
                Tile bestPositionForSummonCreature = AIScoring.findBestPositionForSummonCreature(card);
                if (bestPositionForSummonCreature != null) {
                    System.out.println("Unit summon" + "---------" + card.getCardname());
                    gameState.setHandPosition(myHandCards.indexOf(card) + 1);
                    ProcessTileClicked.processCardUse(out, bestPositionForSummonCreature, card);
                    if (card instanceof Rush) {
                        executeBestAction(myUnits.get(myUnits.size() - 1));
                    }
                }
            }
        }
        ProcessEndTurnClicked.processEndTurnClicked(out);
    }

    private void executeBestAction(Unit unit) {
        Map<Tile, Unit> bestActionForUnit = AIScoring.getBestActionForUnit(unit);
        Tile moveTarget = (Tile) bestActionForUnit.keySet().toArray()[0];
        System.out.println(moveTarget.getTilex() + "----" + moveTarget.getTiley());
        if (moveTarget != TilesGenerator.getUnitTile(unit)) {
            Actions.unitMove(out, unit, moveTarget);
        }

        Unit attackTarget = bestActionForUnit.get(moveTarget);
        if (bestActionForUnit.get(moveTarget) != null) {
            Actions.unitAttack(out, unit, attackTarget);
        }
    }
}

