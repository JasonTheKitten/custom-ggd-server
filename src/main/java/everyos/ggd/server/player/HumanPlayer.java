package everyos.ggd.server.player;

import java.util.List;

import everyos.ggd.server.message.Message;

public interface HumanPlayer extends Player {

	List<Message> getQueuedMessagesFromServer();
	
	void onMessageFromClient(Message message);

	void setConnected(boolean b);
	
	boolean getConnected();
	
}
