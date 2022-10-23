package everyos.ggd.server.game;

import everyos.ggd.server.player.Player;

public interface Match {
	
	int getId();
	
	boolean addPlayer(Player player);

	Player getPlayer(int playerId);

}
