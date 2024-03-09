package gamelogic;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.AIPlayer;
import structures.basic.Tile;

public class ProcessEndTurnClicked {
    public static void processEndTurnClicked(ActorRef out) {

        GameState gameState = GameState.getInstance();

        Actions.resetAfterOneTurn(out);

        if (gameState.getCurrentPlayer() == gameState.getAIPlayer()) {
            AIPlayerLogic logic = new AIPlayerLogic();
            Thread ai = new Thread(logic);
            ai.start();
        }
    }

}
