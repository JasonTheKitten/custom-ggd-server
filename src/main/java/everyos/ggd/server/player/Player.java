package everyos.ggd.server.player;

import java.util.List;

import everyos.ggd.server.message.Message;

public interface Player {

	int getId();

	String getAuthenticationKey();

	boolean isBot();
	
	void onMessageFromServer(Message message);
	
	List<Message> getQueuedMessagesFromClient();
	
	void ping();

}
