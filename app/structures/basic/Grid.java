package structures.basic;

/**
 * Represents the game board as a grid of tiles.
 */
public class Grid {
    private Tile[][] tiles; // A 2D array to hold the tiles
    private int width; // The number of tiles horizontally
    private int height; // The number of tiles vertically

    // Constructor for Grid, assuming width and height are known at creation time
    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        generateGrid(); // Generates the grid with the specified dimensions
    }

    /**
     * Generates the grid by initializing each tile.
     * This method can be expanded to include custom initialization based on game rules.
     */
    public void generateGrid() {
        tiles = new Tile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Initialize each Tile. Customize as necessary, for example, setting textures or properties.
                tiles[x][y] = new Tile(/* parameters if needed */);
            }
        }
    }

    /**
     * Retrieves a tile at the specified grid coordinates.
     *
     * @param tilex The x-coordinate of the tile.
     * @param tiley The y-coordinate of the tile.
     * @return The Tile at the specified coordinates, or null if out of bounds.
     */
    public Tile getTile(int tilex, int tiley) {
        if (tilex >= 0 && tilex < width && tiley >= 0 && tiley < height) {
            return tiles[tilex][tiley];
        } else {
            // Out of bounds
            return null;
        }
    }

    /**
     * Provides access to the entire grid.
     *
     * @return The 2D array of Tiles representing the grid.
     */
    public Tile[][] getGrid() {
        return tiles;
    }

}

