package everyos.ggd.server.game.vanilla;

import java.util.List;

import everyos.ggd.server.game.vanilla.state.entity.EntityRegister;
import everyos.ggd.server.game.vanilla.state.game.GameState;
import everyos.ggd.server.map.MatchMap;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.player.Player;

public interface MatchContext {

	Player[] getPlayers();

	void setGameState(GameState gameState);
	
	MatchMap getMap();
	
	String getMapName();

	int getMatchId();

	void broadcast(Message message);
	
	void rebroadcast(Message message, int broadcasterId);

	void broadcastMessages(List<Message> messages);

	EntityRegister getEntityRegister();

}
