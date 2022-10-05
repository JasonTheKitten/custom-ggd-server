package everyos.ggd.server.game.vanilla;

import java.util.List;

import everyos.ggd.server.event.Event;
import everyos.ggd.server.game.Player;
import everyos.ggd.server.message.Message;

public interface GameState {
	
	void start();

	List<Message> handleEvent(Player player, Event event);

	void ping();

}
