package gamelogic;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.*;
import structures.basic.spell.Spell;
import utils.TilesGenerator;

import java.util.*;

public class AIPlayerLogic implements Runnable {
	GameState gameState;
	ActorRef out;

	public AIPlayerLogic(ActorRef out, GameState gameState) {
		this.out = out;
		this.gameState = gameState;
	}


	@Override
	public void run() {
		Player aiPlayer = gameState.getAIPlayer();

		//First check all the units on board and find their best action
		ArrayList<Unit> myUnits = aiPlayer.getMyUnits();

		for (Unit unit : myUnits) {
			Map<Tile, Unit> bestActionForUnit = AIScoring.getBestActionForUnit(unit);
			Tile moveTarget = (Tile) bestActionForUnit.keySet().toArray()[0];
			// System.out.println(moveTarget.getTilex() + "----" + moveTarget.getTiley());
			if (moveTarget != TilesGenerator.getUnitTile(unit)) {
				//System.out.println((out == null) + "----" + (unit == null) + "------" + (moveTarget == null));
				Actions.unitMove(out, unit, moveTarget);
			}
			Unit attackTarget = bestActionForUnit.get(moveTarget);
			if (bestActionForUnit.get(moveTarget) != null) {
				Actions.unitAttack(out, unit, attackTarget);
			}
		}

		List<Card> myHandCards = aiPlayer.getMyHandCards();
		for (Card card : myHandCards) {
			System.out.println(card.getCardname() + "---------" + card.getUnitConfig());

		}

		//high-mana-cost card first
		ArrayList<Card> myHandCardsCopy = new ArrayList<>(myHandCards);
		myHandCardsCopy.sort(Comparator.comparingInt(Card::getManacost));

		//First try to use spell cards as many as possible
		for (Card card : myHandCardsCopy) {
			if (!card.isCreature()) {
				System.out.println(card.getCardname());
				if (aiPlayer.getMana() <= card.getManacost()) {
					break;
				}
				Spell spell = GameLogic.getSpellObject(card.getId());
				gameState.setSpellToCast(spell);
				Tile bestTargetForSpellCard = AIScoring.findBestTargetForSpellCard(spell);
				if (bestTargetForSpellCard != null) {
					System.out.println("Cast spell" + "---------" + card.getCardname());
					gameState.setHandPosition(myHandCardsCopy.indexOf(card) + 1);
					ProcessTileClicked.processCardUse(out, bestTargetForSpellCard, card);
				}
			}
		}

		//Then try to use creature cards as many as possible

		for (Card card : myHandCardsCopy) {
			if (card.isCreature()) {
				System.out.println(card.getCardname() + "is not a spell");
				if (aiPlayer.getMana() <= card.getManacost()) {
					break;
				}
				Tile bestPositionForSummonCreature = AIScoring.findBestPositionForSummonCreature(card);
				if (bestPositionForSummonCreature != null) {
					System.out.println("Unit summon" + "---------" + card.getCardname());
					gameState.setHandPosition(myHandCardsCopy.indexOf(card) + 1);
					ProcessTileClicked.processCardUse(out, bestPositionForSummonCreature, card);
				}
			}
		}
		
		System.out.println("Hand Cards after Spell and Creature card usage");
		for (Card card : myHandCardsCopy) {	
			System.out.println(card.getCardname() + "---------" + card.getUnitConfig());

		}
		gameState.getCurrentPlayer().getCardManager().setHandCards(myHandCardsCopy);
		ProcessEndTurnClicked.processEndTurnClicked(out);
	}
}
