package structures.basic.creatures;

import structures.basic.Unit;

public class SaberspineTiger extends Unit implements Rush {

	SaberspineTiger() {
		// Should call the setMoved and setExhausted method
		super();
		this.setMoved(isMoved);
		this.setExhausted(isExhausted);
	}
}
