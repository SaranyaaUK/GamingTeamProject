package structures.basic.creatures;

import structures.basic.Unit;

/*
 *  SaberspineTiger.java
 *  
 *  This class implements the Rush interface. It has the ability to
 *  move and attack on the first turn it is summoned
 *  
 */
public class SaberspineTiger extends Unit implements Rush {

	SaberspineTiger() {
		// Should call the setMoved and setExhausted method
		// To enable move and attack in the first round
		super();
		this.setMoved(isMoved);
		this.setExhausted(isExhausted);
	}
}
