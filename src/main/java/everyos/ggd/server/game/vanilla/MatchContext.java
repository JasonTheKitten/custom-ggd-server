package everyos.ggd.server.game.vanilla;

import everyos.ggd.server.game.Player;
import everyos.ggd.server.message.Message;

public interface MatchContext {

	Player[] getPlayers();

	void setGameState(GameState gameState);
	
	String getMapName();

	int getMatchId();

	void broadcast(Message message);

}