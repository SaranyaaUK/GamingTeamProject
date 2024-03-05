package structures.basic.creatures;

import akka.actor.ActorRef;

public interface DeathWatch {
	public void reactToDeath(ActorRef out);
}
