package structures.basic.creatures;

import akka.actor.ActorRef;

/*
 *  DeathWatch.java
 *  
 *  An interface helps to identify the creatures with this ability 
 *  and gives the required methods to be implemented.
 */
public interface DeathWatch {
	// Method to be invoked to implement the actions to react to a unit's death
	public void reactToDeath(ActorRef out);
}
