package gamelogic;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Card;
import structures.basic.GameLogic;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.spell.Spell;
import utils.TilesGenerator;

import java.util.*;

public class AIPlayerLogic implements Runnable {
	GameState gameState;
	ActorRef out;

	AIPlayerLogic(ActorRef out, GameState gameState) {
		this.out = out;
		this.gameState = gameState;
	}

	@Override
	public void run() {
		Player aiPlayer = gameState.getAIPlayer();

		//First check all the units on board and find their best action
		ArrayList<Unit> myUnitsCopy = new ArrayList<>(aiPlayer.getMyUnits());
		for (Unit unit : myUnitsCopy) {
			Map<Tile, Unit> bestActionForUnit = AIScoring.getBestActionForUnit(unit);
			Tile moveTarget = (Tile) bestActionForUnit.keySet().toArray()[0];
			if (moveTarget != null && moveTarget != TilesGenerator.getUnitTile(unit)) {
				Actions.unitMove(out, unit, moveTarget);
			}
			Unit attackTarget = bestActionForUnit.get(moveTarget);
			if (attackTarget != null && bestActionForUnit.get(moveTarget) != null) {
				Actions.unitAttack(out, unit, attackTarget);
			}
		}

		List<Card> myHandCards = aiPlayer.getMyHandCards();
		//high-mana-cost card first
		myHandCards.sort(Comparator.comparingInt(Card::getManacost));

		//First try to use spell cards as many as possible
		ArrayList<Card> myHandCardsCopy = new ArrayList<>(myHandCards);
		for (Card card: myHandCardsCopy) {
			if (!card.getIsCreature()) {
				if (aiPlayer.getMana() <= card.getManacost()) {
					break;
				}
				Spell spell = GameLogic.getSpellObject(card.getId());
				gameState.setSpellToCast(spell);
				Tile bestTargetForSpellCard = AIScoring.findBestTargetForSpellCard(spell);
				if (bestTargetForSpellCard != null) {
					gameState.setHandPosition(myHandCards.indexOf(card) + 1);
					System.out.println("Calling processCardUse from AI - Spell usage");
					ProcessTileClicked.processCardUse(out, bestTargetForSpellCard, card);
				}
			}
		}

		//Then try to use creature cards as many as possible
		for (Card card: myHandCardsCopy) {
			if (card.getIsCreature()) {
				if (aiPlayer.getMana() <= card.getManacost()) {
					break;
				}
				Tile bestPositionForSummonCreature = AIScoring.findBestPositionForSummonCreature(card);
				if (bestPositionForSummonCreature != null) {
					gameState.setHandPosition(myHandCards.indexOf(card) + 1);
					System.out.println("Calling processCardUse from AI - Unit summon");
					ProcessTileClicked.processCardUse(out, bestPositionForSummonCreature, card);
				}
			}
		}

		ProcessEndTurnClicked.processEndTurnClicked(out);
	}
}
