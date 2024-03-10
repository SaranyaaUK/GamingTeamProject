package structures.basic.creatures;

/*
 *  Provoke.java
 *  
 *  An interface helps to identify the creatures with this ability.
 *  Gives the ability for unit with this power to move and attack 
 *  in the same turn as it is summoned
 *  
 */
public interface Rush {
	
	// These attributes are used to override the default statistics
	// of a unit with Rush ability
	boolean isMoved = false;
	boolean isExhausted = false;
}
