package structures.basic.spell;

import java.util.List;
import structures.basic.Tile;
import structures.GameState;

// Interface for the Spell Cards
public interface Spell {

    // Implements the effect of the spell
    public void applySpell(Tile tile);
    // Generates the valid tiles that needs to be highlighted
    public List<Tile> getTargetTilesToHighlight();
}
