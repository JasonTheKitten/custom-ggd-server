package everyos.ggd.server.map;

import everyos.ggd.server.physics.Location;

public interface MatchMap {
	
	int getWidth();
	
	int getHeight();

	Tile getTile(int x, int y);

	Tile getTile(Location location);
	
}
