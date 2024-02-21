package structures.basic;
// Import any packages as needed

import utils.BasicObjectBuilders;

/**
 * The grid class contains 2 division of tiles object (5*9).
 * and contain the methods for generate the grid(board), get tile return the target tile,
 * and method for return the grid
 *
 */

public class Grid {

    /*
     * Signature
     * 
     * Attributes
     * Tile[][] grid
     * 
     * Methods
     * void generateGrid()
     * Tile getTile(int, int)
     * Tile[][] getGrid()
     * 
     */
    private Tile[][] tiles = null;
    public Grid(){
        tiles = new Tile[5][9];
    }

    public Tile getTiles(int x,int y) {
        return tiles[x][y];
    }
    public Tile[][] getGrid(){
        return tiles;
    }
    //The generateGrid method will generate back-end tiles
    public void generateGrid(){
       Tile tile;
        for (int i = 0; i < tiles.length; i++) {
            for(int j=0;j<tiles[i].length;j++){
                tile = BasicObjectBuilders.loadTile(i,j);
                tiles[i][j]=tile;
            }
        }
    }



}
