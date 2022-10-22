package everyos.ggd.server.game.vanilla.state.player;

public interface PlayerStats {

	int getScore();

	void incrementScore(int score);
	
	int getStolen();
	
	void incrementStolen(int stolen);
	
	int getStolenFrom();
	
	void incrementStolenFrom(int stolen);
	
}
