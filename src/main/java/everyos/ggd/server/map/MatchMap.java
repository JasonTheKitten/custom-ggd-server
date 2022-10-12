package everyos.ggd.server.map;

public interface MatchMap {
	
	int getWidth();
	
	int getHeight();

	Tile getTile(int x, int y);
	
}
