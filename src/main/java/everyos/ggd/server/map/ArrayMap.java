package everyos.ggd.server.map;

import everyos.ggd.server.map.imp.TileImp;

public class ArrayMap implements Map {

	private int[][] tiles;

	public ArrayMap(int[][] tiles) {
		this.tiles = tiles;
	}

	@Override
	public int getWidth() {
		return tiles[0].length;
	}

	@Override
	public int getHeight() {
		return tiles.length;
	}

	@Override
	public Tile getTile(int x, int y) {
		if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) {
			return new TileImp(false, false, false, false);
		}
		
		int tile = tiles[y][x];
		return new TileImp(
			tile > 0 && tile != 3,
			tile > 0 && tile != 2,
			tile == 4,
			tile == 5
		);
	}
	
}
