package everyos.ggd.server.game.vanilla;

import everyos.ggd.server.game.Player;
import everyos.ggd.server.map.MatchMap;
import everyos.ggd.server.message.Message;

public interface MatchContext {

	Player[] getPlayers();

	void setGameState(GameState gameState);
	
	MatchMap getMap();
	
	String getMapName();

	int getMatchId();

	void broadcast(Message message);
	
	void rebroadcast(Message message, int broadcasterId);

}
