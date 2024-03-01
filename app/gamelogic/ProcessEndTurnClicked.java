package gamelogic;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.AIPlayer;
import structures.basic.Tile;

public class ProcessEndTurnClicked {
    public static void processEndTurnClicked() {

        GameState gameState = GameState.getInstance();

        Actions.resetAfterOneTurn();

        if (gameState.getCurrentPlayer() == gameState.getAIPlayer()) {
            AIPlayerLogic logic = new AIPlayerLogic();
            Thread ai = new Thread(logic);
            ai.start();
        }
    }

}
