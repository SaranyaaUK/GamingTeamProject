package structures.basic.spell;

import java.util.List;

import akka.actor.ActorRef;
import structures.basic.Tile;

/**
 * 
 *  Spell.java
 *  
 *  Interface for the spell cards
 *
 */

// Interface for the Spell Cards
public interface Spell {

    // Implements the effect of the spell
    public void applySpell(ActorRef out, Tile tile);
    // Generates the valid tiles to be highlighted
    public List<Tile> getTargetTilesToHighlight();
}
