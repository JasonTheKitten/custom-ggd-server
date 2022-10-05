package everyos.ggd.server.game;

import java.util.List;

import everyos.ggd.server.event.Event;
import everyos.ggd.server.message.Message;

public interface Match {
	
	int getId();
	
	boolean addPlayer(Player player);

	Player getPlayer(int playerId);

	List<Message> handleEvent(Player player, Event event);

}
