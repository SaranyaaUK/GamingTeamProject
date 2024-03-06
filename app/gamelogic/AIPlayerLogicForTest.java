package gamelogic;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.spell.Spell;
import utils.TilesGenerator;

import java.util.List;

public class AIPlayerLogicForTest implements Runnable {
    GameState gameState;
    ActorRef out;

    public AIPlayerLogicForTest(GameState gameState, ActorRef out) {
        this.gameState = gameState;
        this.out = out;
    }

    @Override
    public void run() {
        Player aiPlayer = gameState.getAIPlayer();
        List<Card> handCards = aiPlayer.getMyHandCards();
        while (handCards.size() > 0 && handCards.get(0).getManacost() <= aiPlayer.getMana()) {
            Card card = handCards.get(0);
            if (card instanceof Spell) {
                List<Tile> tiles = ((Spell) card).getTargetTilesToHighlight();
                if (tiles != null && tiles.size() > 0) {
                    ProcessTileClicked.processCardUse(out, tiles.get(0));
                }
            }
        }
        for (Unit unit : aiPlayer.getMyUnits()) {
            if (!unit.isMoved()) {
                List<Tile> tiles = TilesGenerator.getMovableTiles(unit);
                if (!tiles.isEmpty()) {
                    Actions.unitMove(out, unit, tiles.get(0));
                }
                List<Tile> attackableTiles = TilesGenerator.getAttackableTiles(unit);

                if (!attackableTiles.isEmpty()) {
                    Actions.unitAttack(out, unit, attackableTiles.get(0).getUnit());
                }
            }
        }
        ProcessEndTurnClicked.processEndTurnClicked(out);
    }


}
