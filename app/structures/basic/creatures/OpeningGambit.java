package structures.basic.creatures;

import akka.actor.ActorRef;
import structures.basic.Tile;

/*
 *  OpeningGambit.java
 *  
 *  An interface helps to identify the creatures with this ability 
 *  and gives the required methods to be implemented.
 */
public interface OpeningGambit {
	// Implement the effects to be triggred when a unit with this 
	// ability is summoned on to the board
	void reactToUnitsSummon(ActorRef out, Tile tile);
}
