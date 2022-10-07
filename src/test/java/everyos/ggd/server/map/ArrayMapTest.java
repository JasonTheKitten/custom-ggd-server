package everyos.ggd.server.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ArrayMapTest {

	private int[][] mapTiles = new int[][] {
		new int[] { 0, 1 },
		new int[] { 2, 3 },
		new int[] { 4, 5 }
	};
	
	private Map map = new ArrayMap(mapTiles);
	
	@Test
	@DisplayName("Wall tile is not passable")
	public void wallTileIsNotPassable() {
		Tile tile = map.getTile(0, 0);
		Assertions.assertFalse(tile.greenCanPass());
		Assertions.assertFalse(tile.purpleCanPass());
		Assertions.assertFalse(tile.hasSpiritFlame());
		Assertions.assertFalse(tile.hasMegaFlame());
	}
	
	@Test
	@DisplayName("Empty tile is passable")
	public void emptyTileIsPassable() {
		Tile tile = map.getTile(1, 0);
		Assertions.assertTrue(tile.greenCanPass());
		Assertions.assertTrue(tile.purpleCanPass());
		Assertions.assertFalse(tile.hasSpiritFlame());
		Assertions.assertFalse(tile.hasMegaFlame());
	}
	
	@Test
	@DisplayName("Green tile is passable by green")
	public void greenTileIsPassableByGreen() {
		Tile tile = map.getTile(0, 1);
		Assertions.assertTrue(tile.greenCanPass());
		Assertions.assertFalse(tile.purpleCanPass());
		Assertions.assertFalse(tile.hasSpiritFlame());
		Assertions.assertFalse(tile.hasMegaFlame());
	}
	
	@Test
	@DisplayName("Purple tile is passable by purple")
	public void purpleTileIsPassableByGreen() {
		Tile tile = map.getTile(1, 1);
		Assertions.assertFalse(tile.greenCanPass());
		Assertions.assertTrue(tile.purpleCanPass());
		Assertions.assertFalse(tile.hasSpiritFlame());
		Assertions.assertFalse(tile.hasMegaFlame());
	}
	
	@Test
	@DisplayName("Spirit flame tile is passable")
	public void spiritFlameTileIsPassable() {
		Tile tile = map.getTile(0, 2);
		Assertions.assertTrue(tile.greenCanPass());
		Assertions.assertTrue(tile.purpleCanPass());
		Assertions.assertTrue(tile.hasSpiritFlame());
		Assertions.assertFalse(tile.hasMegaFlame());
	}
	
	@Test
	@DisplayName("Mega flame tile is passable")
	public void megaFlameTileIsPassable() {
		Tile tile = map.getTile(1, 2);
		Assertions.assertTrue(tile.greenCanPass());
		Assertions.assertTrue(tile.purpleCanPass());
		Assertions.assertFalse(tile.hasSpiritFlame());
		Assertions.assertTrue(tile.hasMegaFlame());
	}
	
	@Test
	@DisplayName("Out of bounds tile is not passable")
	public void outOfBoundsTileIsNotPassable() {
		Tile tile = map.getTile(-1, -1);
		Assertions.assertFalse(tile.greenCanPass());
		Assertions.assertFalse(tile.purpleCanPass());
		Assertions.assertFalse(tile.hasSpiritFlame());
		Assertions.assertFalse(tile.hasMegaFlame());
		
		tile = map.getTile(2, 3);
		Assertions.assertFalse(tile.greenCanPass());
		Assertions.assertFalse(tile.purpleCanPass());
		Assertions.assertFalse(tile.hasSpiritFlame());
		Assertions.assertFalse(tile.hasMegaFlame());
	}
	
}
