package everyos.ggd.server.game;

public interface Match {
	
	int getId();
	
	boolean addPlayer(Player player);

	Player getPlayer(int playerId);

}
