package gamelogic;

import akka.actor.ActorRef;
import structures.GameState;

public class ProcessEndTurnClicked {
    public static void processEndTurnClicked(ActorRef out) {

        GameState gameState = GameState.getInstance();

        Actions.resetAfterOneTurn(out);

        if (!gameState.isCurrentPlayerHuman()) {
            AIPlayerLogic logic = new AIPlayerLogic(out, gameState);
            Thread ai = new Thread(logic);
            ai.start();
        }
    }

}
