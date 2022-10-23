package everyos.ggd.server.bot.state;

import java.util.List;

import everyos.ggd.server.message.Message;

public interface BotState {

	List<Message> ping(List<Message> serverMessages);
	
}
