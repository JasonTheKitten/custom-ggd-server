package everyos.ggd.server.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MapLoaderTest {

	@Test
	@DisplayName("Can parse map from string")
	public void canParseMapFromString() {
		MatchMap map = MapLoader.loadFromString("01\n10");
		assert0(map, 0, 0);
		assert1(map, 1, 0);
		assert1(map, 0, 1);
		assert0(map, 1, 1);
	}

	private void assert0(MatchMap map, int x, int y) {
		Tile tile = map.getTile(x, y);
		Assertions.assertFalse(tile.greenCanPass());
		Assertions.assertFalse(tile.purpleCanPass());
	}
	
	private void assert1(MatchMap map, int x, int y) {
		Tile tile = map.getTile(x, y);
		Assertions.assertTrue(tile.greenCanPass());
		Assertions.assertTrue(tile.purpleCanPass());
	}
	
}
