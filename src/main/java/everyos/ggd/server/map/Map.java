package everyos.ggd.server.map;

public interface Map {
	
	int getWidth();
	
	int getHeight();

	Tile getTile(int x, int y);
	
}
