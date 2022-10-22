package everyos.ggd.server.game.vanilla.state.bot;

import java.util.List;

import everyos.ggd.server.message.Message;

public interface BotState {

	List<Message> ping(List<Message> queuedMessages);
	
}
