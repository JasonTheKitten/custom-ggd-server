package everyos.ggd.server.game;

import java.util.List;

import everyos.ggd.server.message.Message;

public interface HumanPlayer extends Player {

	List<Message> getQueuedMessagesFromServer();
	
	void onMessageFromClient(Message message);
	
}
