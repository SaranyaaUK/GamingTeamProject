package gamelogic;

import akka.actor.ActorRef;
import structures.GameState;

public class ProcessEndTurnClicked {
    public static void processEndTurnClicked(ActorRef out) {
        System.out.println("processEndTurnClicked");

        GameState gameState = GameState.getInstance();

        Actions.resetAfterOneTurn(out);

        if (gameState.getCurrentPlayer() == gameState.getAIPlayer()) {
            AIPlayerLogic logic = new AIPlayerLogic(gameState, out);
            Thread ai = new Thread(logic);
            ai.start();
        }
    }

}
