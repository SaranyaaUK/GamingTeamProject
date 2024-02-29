import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import structures.basic.Grid;
import utils.BasicObjectBuilders;

/*
 *  Test the Grid Class
 */

public class GridTest {

	@Test
	public void testGetBoardTiles() {
		Grid myGrid = BasicObjectBuilders.loadGrid();
		assertFalse("After the grid is loaded the board tiles should not be empty", myGrid.getBoardTiles() == null);
	}
	
	@Test
	public void testGetTiles() {
		Grid myGrid = BasicObjectBuilders.loadGrid();
		assertEquals("Test tile position", 8, myGrid.getTile(8,3).getTilex());
		assertEquals("Test tile position", 3, myGrid.getTile(8,3).getTiley());
		
		// Out of bound values should return null
		assertEquals("Test tile position", null, myGrid.getTile(10,3));
		assertEquals("Test tile position", null, myGrid.getTile(1,6));
	}

}
