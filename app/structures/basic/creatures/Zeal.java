package structures.basic.creatures;

import akka.actor.ActorRef;

/*
 *  Zeal.java
 *  
 *  An interface helps to identify the creatures with this ability.
 *  Reacts to owning player's avatar taking damage.
 */

public interface Zeal {
	public void applyZeal(ActorRef out);
}
