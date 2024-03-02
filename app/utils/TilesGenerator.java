package utils;

import java.util.ArrayList;
import java.util.List;

import structures.GameState;
import structures.basic.Grid;
import structures.basic.Player;
import structures.basic.Position;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.creatures.DeathWatch;
import structures.basic.creatures.Provoke;
import structures.basic.creatures.YoungFlamewing;

/*
 *  TilesGenerator - holds logic to generate valid tiles for highlighting
 *  for summoning, spell casting, moving and attacking.
 *
 *  Can be used for AI to get the tiles for AI Logic
 */
public class TilesGenerator {

    /*
     *  Gives a list of tiles containing current player's
     *  enemy units
     */
    public static List<Tile> getEnemyUnitTiles() {

//		If you need gameState uncomment below line
//		GameState gameState = GameState.getInstance();

        return null;
    }

    /*
     *  Gives a list of tiles containing current player's
     *  friendly/allied units
     */
    public static List<Tile> getFriendlyUnitTiles() {

//		If you need gameState uncomment below line
//		GameState gameState = GameState.getInstance();

        return null;

    }

    /*
     *  Get the avatar's tile
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
     * 	Get the unit's tile object
     *  @return Tile object
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
     *  Gives a list of tiles to summon the unit corresponding to the creature card
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
            Position unitPosition = playerUnits.get(i).getPosition(); // Get the unit's position
            int tilex = unitPosition.getTilex();
            int tiley = unitPosition.getTiley();

            Grid myGrid = gameState.getGrid();
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (tilex + i > 9 || tiley + j > 5 || tilex + i < 1 || tiley + j < 1) {
                        continue;
                    }
                    Tile tile = myGrid.getTile(tilex + j, tiley + k);
                    unitTiles.add(tile);
                }
            }
        }
        // Remove Tiles with units in it
        // This is a workaround, the below two lines should be uncommented after its implementation is done and we
        // can get rid of this workaround.
        unitTiles.removeAll(getAvatarTile(gameState.getHumanPlayer().getAvatar()));
//		unitTiles.removeAll(getFriendlyUnitTiles());
//		unitTiles.removeAll(getEnemyUnitTiles());

        return unitTiles;
    }


    //if the unit is already moved, only get enemy unit within attack distance
    //if the unit has not moved, get enemy unit within movable distance
    //check if there is provoke unit around
    //check if is YoungFlamewing
    public static List<Tile> getAttackableTiles(Tile tile) {
        List<Tile> attackableTiles = new ArrayList<Tile>();
        if (tile.getUnit().isMoved()) {
            if (hasProvokeUnitAround(tile)) {
                attackableTiles.addAll(getProvokeUnitSurroundingTiles(tile));
            } else {
                attackableTiles.addAll(getEnemiesWithinAttackDistance(tile));
            }
        } else {
            if (tile.getUnit() instanceof YoungFlamewing) {
                if (hasProvokeUnitAround(tile)) {
                    attackableTiles.addAll(getProvokeUnitSurroundingTiles(tile));
                } else {
                    attackableTiles.addAll(getYoungFlamewingAttackableTiles(tile));
                }
            } else {
                if (hasProvokeUnitAround(tile)) {
                    attackableTiles.addAll(getProvokeUnitSurroundingTiles(tile));
                } else {
                    attackableTiles.addAll(getEnemiesWithinMovableDistance(tile));
                }
            }
        }
        return attackableTiles;
    }

    public static List<Tile> getEnemiesWithinAttackDistance(Tile tile) {
        ArrayList<Tile> adjacentEnemyTiles = new ArrayList<Tile>();
        int tileX = tile.getTilex();
        int tileY = tile.getTiley();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newX = tileX + i;
                int newY = tileY + j;
                int tileXSize = GameState.getInstance().getGrid().gridxsize;
                int tileYSize = GameState.getInstance().getGrid().gridysize;
                //check if new X and Y is valid and not the tile itself
                if (newX >= 0 && newX < tileXSize && newY >= 0 && newY < tileYSize && !(i == 0 && j == 0)) {
                    Tile adjacentTile = GameState.getInstance().getGrid().getTile(newX, newY);
                    if (adjacentTile.getUnit() != null && adjacentTile != null) {
                        if (adjacentTile.getUnit().isHumanUnit() != tile.getUnit().isHumanUnit()) {
                            adjacentEnemyTiles.add(adjacentTile);
                        }
                    }
                }
            }
        }
        return adjacentEnemyTiles;
    }

    public static List<Tile> getEnemiesWithinMovableDistance(Tile tile) {
        ArrayList<Tile> withinMovableEnemyTiles = new ArrayList<Tile>();
        int tileX = tile.getTilex();
        int tileY = tile.getTiley();
        int[][] offsets = {{-2, 0}, {2, 0}, {0, -2}, {0, 2}, {0, -1}, {1, 0}, {0, 1}, {-1, 0}, {-1, -1}, {1, -1}, {1, 1}, {-1, 1}};
        for (int[] offset : offsets) {
            int newX = tileX + offset[0];
            int newY = tileY + offset[1];
            int tileXSize = GameState.getInstance().getGrid().gridxsize;
            int tileYSize = GameState.getInstance().getGrid().gridysize;
            //check if new X and Y is valid and not the tile itself
            if (newX >= 0 && newX < tileXSize && newY >= 0 && newY < tileYSize) {
                Tile adjacentTile = GameState.getInstance().getGrid().getTile(newX, newY);
                if (adjacentTile.getUnit() != null && adjacentTile != null) {
                    if (adjacentTile.getUnit().isHumanUnit() != tile.getUnit().isHumanUnit()) {
                        withinMovableEnemyTiles.add(adjacentTile);
                    }
                }
            }
        }
        return withinMovableEnemyTiles;
    }

    public static List<Tile> getProvokeUnitSurroundingTiles(Tile tile) {
        List<Tile> tilesWithProvoke = new ArrayList<>();
        List<Tile> adjacentEnemyTiles = getEnemiesWithinAttackDistance(tile);
        for (Tile adjacentTile : adjacentEnemyTiles) {
            Unit unit = adjacentTile.getUnit();
            if (unit != null && unit instanceof Provoke) {
                tilesWithProvoke.add(adjacentTile); // add provoke only to the attackable tile list
            }
        }
        return tilesWithProvoke;
    }

    public static List<Tile> getYoungFlamewingAttackableTiles(Tile tile) {
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
            if (currentTile.getUnit().isHumanUnit() != tile.getUnit().isHumanUnit()) {
                List<Tile> surroundingTiles = getSurroundingTiles(currentTile);
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

    public static List<Tile> getSurroundingTiles(Tile tile) {
        ArrayList<Tile> adjacentTiles = new ArrayList<Tile>();
        int tileX = tile.getTilex();
        int tileY = tile.getTiley();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newX = tileX + i;
                int newY = tileY + j;
                int tileXSize = GameState.getInstance().getGrid().gridxsize;
                int tileYSize = GameState.getInstance().getGrid().gridysize;
                //check if new X and Y is valid and not the tile itself
                if (newX >= 0 && newX < tileXSize && newY >= 0 && newY < tileYSize && !(i == 0 && j == 0)) {
                    Tile adjacentTile = GameState.getInstance().getGrid().getTile(newX, newY);
                }

            }
        }
        return adjacentTiles;
    }

    public static boolean hasProvokeUnitAround(Tile tile) {
        List<Tile> surroundingTiles = getSurroundingTiles(tile);
        for (Tile adjacentTile : surroundingTiles) {
            Unit unit = adjacentTile.getUnit();
            if (unit != null && unit.isHumanUnit() != tile.getUnit().isHumanUnit() && unit instanceof Provoke) {
                return true;
            }
        }
        return false;
    }

    //check if there is provoke around
    public static List<Tile> getMovableTiles(Tile tile) {
        return null;
    }


}
