package utils;

import java.util.ArrayList;
import java.util.List;

import structures.GameState;
import structures.basic.Grid;
import structures.basic.Player;
import structures.basic.Position;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.creatures.Provoke;
import structures.basic.creatures.Flying;

/*
 *  TilesGenerator - holds logic to generate valid tiles for highlighting
 *  for summoning, spell casting, moving and attacking.
 *
 *  Can be used for AI to get the tiles for AI Logic
 */
public class TilesGenerator {

    /*
     * Gives a list of tiles containing current player's
     * enemy units
     */
    public static List<Tile> getEnemyUnitTiles() {

        GameState gameState = GameState.getInstance();

        List<Tile> enemyTiles = new ArrayList<Tile>();
        if (gameState.isCurrentPlayerHuman()) {
            for (Unit unit : gameState.getAIPlayer().getMyUnits()) {
                enemyTiles.add(getUnitTile(unit));
            }
        } else {
            for (Unit unit : gameState.getHumanPlayer().getMyUnits()) {
                enemyTiles.add(getUnitTile(unit));
            }
        }
        return enemyTiles;
    }

    /*
     * Gives a list of tiles containing current player's
     * friendly/allied units
     */
    public static List<Tile> getFriendlyUnitTiles() {

        GameState gameState = GameState.getInstance();

        List<Tile> alliedTiles = new ArrayList<Tile>();
        if (gameState.isCurrentPlayerHuman()) {
            for (Unit unit : gameState.getHumanPlayer().getMyUnits()) {
                alliedTiles.add(getUnitTile(unit));
            }
        } else {
            for (Unit unit : gameState.getAIPlayer().getMyUnits()) {
                alliedTiles.add(getUnitTile(unit));
            }
        }
        return alliedTiles;
    }

    /*
     * Get the avatar's tile
     *
     */
    public static List<Tile> getAvatarTile(Unit myAvatar) {

        List<Tile> avatarTile = new ArrayList<Tile>();

        // Get the avatar's tile information
        Tile tile = getUnitTile(myAvatar);
        avatarTile.add(tile);

        return avatarTile;
    }

    /*
     * Get the unit's tile object
     * 
     * @return Tile object
     *
     */
    public static Tile getUnitTile(Unit myUnit) {

        GameState gameState = GameState.getInstance();
        Position unitPosition = myUnit.getPosition();
        int tilex = unitPosition.getTilex();
        int tiley = unitPosition.getTiley();

        // Get the unit's tile information
        return gameState.getGrid().getTile(tilex, tiley);
    }

    /*
     * Gives a list of tiles to summon the unit corresponding to the creature card
     *
     */
    public static List<Tile> getTilesToSummon() {

        GameState gameState = GameState.getInstance();
        Player currentPlayer = gameState.getCurrentPlayer();
        List<Unit> playerUnits = currentPlayer.getMyUnits();

        // List of tiles to highlight
        List<Tile> unitTiles = new ArrayList<Tile>();

        // Find the unit's adjacent positions
        for (int i = 0; i < playerUnits.size(); i++) {
            unitTiles.addAll(getAdjacentTiles(playerUnits.get(i)));
        }
        // Remove Tiles with units in it
        unitTiles.removeAll(getFriendlyUnitTiles());
        unitTiles.removeAll(getEnemyUnitTiles());

        return unitTiles;
    }

    // check if there is provoke around
    public static List<Tile> getMovableTiles(Unit unit) {
        GameState gameState = GameState.getInstance();
        Grid myGrid = gameState.getGrid();

        Position unitPosition = unit.getPosition(); // Get the unit's position
        int tilex = unitPosition.getTilex();
        int tiley = unitPosition.getTiley();

        List<Tile> unitTiles = getAdjacentTiles(unit);

        // Logic to decide whether to highlight the second tile in the cardinal
        // direction, if
        // there is obstruction we do not highlight
        int x = GameState.getInstance().getGrid().getGridXSize();
        int y = GameState.getInstance().getGrid().getGridYSize();
        int front, behind, above, below;
        if (gameState.isCurrentPlayerHuman()) {
            front = tilex + 1;
            behind = tilex - 1;
            // Front
            if (front <= x && front >= 1) {
                if (myGrid.getTile(front, tiley).getUnit() == null) {
                    if ((front + 1 <= x && front + 1 >= 1)) {
                        unitTiles.add(myGrid.getTile(front + 1, tiley));
                    }
                }
            }
            // Behind
            if (behind >= 1 && behind <= x) {
                if (myGrid.getTile(behind, tiley).getUnit() == null) {
                    if ((behind - 1 <= x && behind - 1 > 1)) {
                        unitTiles.add(myGrid.getTile(behind - 1, tiley));
                    }
                }
            }
        } else {
            front = tilex - 1;
            behind = tilex + 1;
            // Front
            if (front <= x && front >= 0) {
                if (myGrid.getTile(front, tiley).getUnit() == null) {
                    if ((front - 1 <= x && front - 1 >= 1)) {
                        unitTiles.add(myGrid.getTile(front - 1, tiley));
                    }
                }
            }
            // Behind
            if (behind >= 1 && behind <= x) {
                if (myGrid.getTile(behind, tiley).getUnit() == null) {
                    if ((behind + 1 <= x && behind + 1 >= 1)) {
                        unitTiles.add(myGrid.getTile(behind + 1, tiley));
                    }
                }
            }
        }

        // Above
        if (tiley - 1 >= 1 && tiley - 1 <= y) {
            if (myGrid.getTile(tilex, tiley - 1).getUnit() == null) {
                if ((tiley - 2 <= y && tiley - 2 >= 1)) {
                    unitTiles.add(myGrid.getTile(tilex, tiley - 2));
                }
            }
        }
        // Below
        if (tiley + 1 >= 1 && tiley + 1 <= y) {
            if (myGrid.getTile(tilex, tiley + 1).getUnit() == null) {
                if ((tiley + 2 <= y && tiley + 2 >= 1)) {
                    unitTiles.add(myGrid.getTile(tilex, tiley + 2));
                }
            }
        }

        // Check if diagonal tile need to be removed
        // front and above || front and below occupied
        if (gameState.isCurrentPlayerHuman()) {
            front = tilex + 1;
            behind = tilex - 1;
            above = tiley - 1;
            below = tiley + 1;
            removeDiagonalTiles(unitTiles, tilex, tiley, above, below, front, behind);
        } else {
            // AI Player
            front = tilex - 1;
            behind = tilex + 1;
            above = tiley - 1;
            below = tiley + 1;
            removeDiagonalTiles(unitTiles, tilex, tiley, above, below, front, behind);
        }

        // Remove Tiles with units in it
        unitTiles.removeAll(getFriendlyUnitTiles());
        unitTiles.removeAll(getEnemyUnitTiles());

        return unitTiles;
    }

    public static List<Tile> removeDiagonalTiles(List<Tile> unitTiles, int tilex, int tiley, int above, int below,
            int front, int behind) {
        GameState gameState = GameState.getInstance();
        Grid myGrid = gameState.getGrid();

        if ((TilesGenerator.isValidTile(front, tiley) && (myGrid.getTile(front, tiley).getUnit() != null))
                && (TilesGenerator.isValidTile(tilex, above) && (myGrid.getTile(tilex, above).getUnit() != null))) {
            unitTiles.remove(myGrid.getTile(front, above));
        }

        if ((TilesGenerator.isValidTile(front, tiley) && (myGrid.getTile(front, tiley).getUnit() != null))
                && (TilesGenerator.isValidTile(tilex, below) && (myGrid.getTile(tilex, below).getUnit() != null))) {
            unitTiles.remove(myGrid.getTile(front, below));
        }

        if ((TilesGenerator.isValidTile(behind, tiley) && (myGrid.getTile(behind, tiley).getUnit() != null))
                && (TilesGenerator.isValidTile(tilex, above) && (myGrid.getTile(tilex, above).getUnit() != null))) {
            unitTiles.remove(myGrid.getTile(behind, above));
        }

        if ((TilesGenerator.isValidTile(behind, tiley) && (myGrid.getTile(behind, tiley).getUnit() != null))
                && (TilesGenerator.isValidTile(tilex, below) && (myGrid.getTile(tilex, below).getUnit() != null))) {
            unitTiles.remove(myGrid.getTile(behind, below));
        }

        return unitTiles;
    }

    public static boolean isValidTile(int tilex, int tiley) {
        GameState gameState = GameState.getInstance();

        Grid myGrid = gameState.getGrid();
        int x = myGrid.getGridXSize();
        int y = myGrid.getGridYSize();

        if (tilex > x || tiley > y || tilex < 1 || tiley < 1) {
            return false;
        }
        return true;
    }

    // Get adjacent tiles given a unit
    public static List<Tile> getAdjacentTiles(Unit unit) {
        // List of tiles to highlight
        List<Tile> unitTiles = new ArrayList<Tile>();

        GameState gameState = GameState.getInstance();
        Position unitPosition = unit.getPosition(); // Get the unit's position
        int tilex = unitPosition.getTilex();
        int tiley = unitPosition.getTiley();

        Grid myGrid = gameState.getGrid();
        for (int j = -1; j <= 1; j++) {
            for (int k = -1; k <= 1; k++) {
                if (!isValidTile(tilex, tiley) && !(j == 0 && k == 0)) {
                    continue;
                }
                Tile tile = myGrid.getTile(tilex + j, tiley + k);
                if (tile == null) {
                    continue;
                }
                unitTiles.add(tile);
            }
        }

        return unitTiles;
    }

    // if the tile is already moved, only get enemy tiles within attack distance
    // if the tile has not moved, get enemy tiles within movable distance
    // if the unit is already moved, only get enemy unit within attack distance
    // if the unit has not moved, get enemy unit within movable distance
    // check if there is provoke unit around
    // check if is YoungFlamewing
    public static List<Tile> getAttackableTiles(Unit unit) {
        List<Tile> attackableTiles = new ArrayList<Tile>();
        if (unit.isMoved()) {

            if (hasProvokeUnitAround(unit)) {
                if (!getProvokeUnitSurroundingTiles(unit).isEmpty()) {
                    attackableTiles.addAll(getProvokeUnitSurroundingTiles(unit));
                }
            } else {
                if (!getEnemiesWithinAttackDistance(unit).isEmpty()) {
                    attackableTiles.addAll(getEnemiesWithinAttackDistance(unit));
                }
            }
        } else {
            if (unit instanceof Flying) {
                if (hasProvokeUnitAround(unit)) {
                    if (!getProvokeUnitSurroundingTiles(unit).isEmpty()) {
                        attackableTiles.addAll(getProvokeUnitSurroundingTiles(unit));
                    }
                } else {
                    if (!getYoungFlamewingAttackableTiles(unit).isEmpty()) {
                        attackableTiles.addAll(getYoungFlamewingAttackableTiles(unit));
                    }
                }
            } else {
                if (hasProvokeUnitAround(unit)) {
                    if (!getProvokeUnitSurroundingTiles(unit).isEmpty()) {
                        attackableTiles.addAll(getProvokeUnitSurroundingTiles(unit));
                    }
                } else {
                    if (!getEnemiesWithinMovableDistance(unit).isEmpty()) {
                        attackableTiles.addAll(getEnemiesWithinMovableDistance(unit));
                    }
                }
            }
        }
        return attackableTiles;
    }

    public static List<Tile> getEnemiesWithinAttackDistance(Unit unit) {
        ArrayList<Tile> adjacentEnemyTiles = new ArrayList<Tile>();
        int tileX = unit.getPosition().getTilex();
        int tileY = unit.getPosition().getTiley();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newX = tileX + i;
                int newY = tileY + j;
                // check if new X and Y is valid and has a unit
                if (isValidTile(newX, newY)) {
                    Tile adjacentTile = GameState.getInstance().getGrid().getTile(newX, newY);
                    if (adjacentTile != null && adjacentTile.getUnit() != null) {
                        if (adjacentTile.getUnit().isHumanUnit() != unit.isHumanUnit()) {
                            adjacentEnemyTiles.add(adjacentTile);
                        }
                    }
                }
            }
        }
        return adjacentEnemyTiles;
    }

    public static List<Tile> getEnemiesWithinMovableDistance(Unit unit) {
        ArrayList<Tile> withinMovableEnemyTiles = new ArrayList<Tile>();
        int tileX = unit.getPosition().getTilex();
        int tileY = unit.getPosition().getTiley();
        int[][] offsets = { { -2, 0 }, { 2, 0 }, { 0, -2 }, { 0, 2 }, { 0, -1 }, { 1, 0 }, { 0, 1 }, { -1, 0 },
                { -1, -1 }, { 1, -1 }, { 1, 1 }, { -1, 1 } };
        for (int[] offset : offsets) {
            int newX = tileX + offset[0];
            int newY = tileY + offset[1];
            // check if new X and Y is valid and has a unit
            if (isValidTile(newX, newY)) {
                Tile adjacentTile = GameState.getInstance().getGrid().getTile(newX, newY);
                if (adjacentTile != null && adjacentTile.getUnit() != null) {
                    if (adjacentTile.getUnit().isHumanUnit() != unit.isHumanUnit()) {
                        withinMovableEnemyTiles.add(adjacentTile);
                    }
                }
            }
        }
        return withinMovableEnemyTiles;
    }

    public static List<Tile> getProvokeUnitSurroundingTiles(Unit myUnit) {
        List<Tile> tilesWithProvoke = new ArrayList<>();
        List<Tile> adjacentEnemyTiles = getEnemiesWithinAttackDistance(myUnit);
        for (Tile adjacentTile : adjacentEnemyTiles) {
            Unit unit = adjacentTile.getUnit();
            if (unit != null && unit instanceof Provoke) {
                tilesWithProvoke.add(adjacentTile); // add provoke only to the attackable tile list
            }
        }
        return tilesWithProvoke;
    }

    public static List<Tile> getYoungFlamewingAttackableTiles(Unit unit) {
        List<Tile> yfwAttackableTiles = new ArrayList<>();
        // scan all tiles in the board and store in List
        Tile[][] allTiles = GameState.getInstance().getGrid().getBoardTiles();
        List<Tile> allTileList = new ArrayList<>();
        for (Tile[] row : allTiles) {
            for (Tile tiles : row) {
                allTileList.add(tiles);
            }
        }
        // scan all tiles that has enemy unit but surrounding with unoccupied tile
        for (Tile currentTile : allTileList) {
            // check is has enemy unit
            if (currentTile.getUnit().isHumanUnit() != unit.isHumanUnit()) {
                List<Tile> surroundingTiles = getAdjacentTiles(currentTile.getUnit());
                // check if surrounding is unoccupied
                for (Tile surroundingTile : surroundingTiles) {
                    if (surroundingTile.getUnit() == null) {
                        // add to the young flamewing attackable list
                        yfwAttackableTiles.add(currentTile);
                        break;
                    }
                }
            }
        }

        return yfwAttackableTiles;
    }

    public static boolean hasProvokeUnitAround(Unit myUnit) {
        List<Tile> surroundingTiles = getAdjacentTiles(myUnit);
        for (Tile adjacentTile : surroundingTiles) {
            Unit unit = adjacentTile.getUnit();
            if (unit != null && unit.isHumanUnit() != myUnit.isHumanUnit() && unit instanceof Provoke) {
                return true;
            }
        }
        return false;
    }

    public static List<Tile> getAdjacentEnemyTiles(Tile tile) {
        return null;
    }
}
