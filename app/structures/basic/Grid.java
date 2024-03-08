package structures.basic;

import utils.BasicObjectBuilders;
import java.io.File;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 *  Grid.java
 *  
 *  Represents the game board as a grid of tiles.
 */
public class Grid {
    Tile[][] boardTiles; // A 2D array to hold the tiles
    @JsonIgnore
    private static ObjectMapper mapper = new ObjectMapper(); // Jackson Java Object Serializer, is used to read java
                                                             // objects from a file
    int gridxsize;
    int gridysize;
    int gridmargin;
    int gridTopLeftx;
    int gridTopLefty;

    // Constructor for Grid
    @JsonCreator
    public Grid(@JsonProperty("boardTiles") Tile[][] boardTiles,
            @JsonProperty("gridxsize") int gridxsize,
            @JsonProperty("gridysize") int gridysize,
            @JsonProperty("gridmargin") int gridmargin,
            @JsonProperty("gridTopLeftx") int gridTopLeftx,
            @JsonProperty("gridTopLefty") int gridTopLefty) {
        this.boardTiles = boardTiles;
        this.gridxsize = gridxsize;
        this.gridysize = gridysize;
        this.gridmargin = gridmargin;
        this.gridTopLeftx = gridTopLeftx;
        this.gridTopLefty = gridTopLefty;
    }

    /**
     * Generates the grid by initializing each tile.
     * This method can be expanded to include custom initialization based on game
     * rules.
     * 
     */
    private void generateGrid() {
        for (int x = 0; x < gridxsize; x++) {
            for (int y = 0; y < gridysize; y++) {
                // Initialize each Tile. Customize as necessary, for example, setting textures
                // or properties.
                boardTiles[x][y] = BasicObjectBuilders.loadTile(x+1, y+1);
            }
        }
    }

    /**
     * Retrieves a tile at the specified grid coordinates.
     *
     * @param tilex The x-coordinate of the tile(1-9).
     * @param tiley The y-coordinate of the tile(1-5).
     * @return The Tile at the specified coordinates, or null if out of bounds.
     * 
     */
    public Tile getTile(int tilex, int tiley) {
        if (tilex > 0 && tilex <= gridxsize && tiley > 0 && tiley <= gridysize) {
            return boardTiles[--tilex][--tiley];
        } else {
            // Out of bounds
            return null;
        }
    }

    /**
     * Provides access to the entire grid.
     *
     * @return The 2D array of Tiles representing the grid.
     * 
     */
    public Tile[][] getBoardTiles() {
        return boardTiles;
    }

    /**
     * Construct a grid from the configuration file
     * parameters.
     * 
     * @param configFile
     * @return
     */
    public static Grid constructGrid(String configFile) {

        try {
            Grid myGrid = mapper.readValue(new File(configFile), Grid.class);
            myGrid.boardTiles = new Tile[myGrid.gridxsize][myGrid.gridysize];
            myGrid.generateGrid();
            return myGrid;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public int getGridXSize() {
    	return this.gridxsize;
    }
    
    public int getGridYSize() {
    	return this.gridysize;
    }

}
