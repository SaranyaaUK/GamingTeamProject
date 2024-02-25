package structures.basic;
// Import any packages as needed

/**
 * Help of the GameLogic class goes here
 *
 */

public class GameLogic {

	/*
	 *  Method to associate a unit with the tile, when a unit is created 
	 *  and we have it's tile position this method can be invoked 
	 *  It take the tile's position and also set the tile's unit attribute
	 *  accordingly.
	 */
    public static void associateUnitWithTile(Unit unit, Tile tile) {
        // Update the Unit's position to match the Tile's coordinates
        unit.setPositionByTile(tile);
        // Set the Unit on the Tile
        tile.setUnit(unit);
    }

}
