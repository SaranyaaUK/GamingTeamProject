package structures.basic.creatures;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.basic.Unit;

/*
 *  ShadowWatcher.java
 *
 *  This class implements the Death interface, reacts when a unit dies on the board
 *
 */
public class ShadowWatcher extends Unit implements DeathWatch {

    /*
     *  Reacts to death of any unit on the board
     */
    @Override
    public void reactToDeath(ActorRef out) {
        // TODO Auto-generated method stub

        // The unit gains +1 attack and +1 health permanently
        // (Cannot increase the creature's maximum health)
        // Add any method that is needed
        this.setAttack(this.getAttack() + 1);
        this.setHealth(Math.min(this.getMaximumHealth(), this.getHealth() + 1));

        // Update in front-end
        BasicCommands.setUnitAttack(out, this, this.getAttack());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BasicCommands.setUnitHealth(out, this, this.getHealth());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
