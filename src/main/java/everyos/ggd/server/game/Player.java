package everyos.ggd.server.game;

import everyos.ggd.server.message.Message;

public interface Player {

	int getId();

	String getAuthenticationKey();

	boolean isBot();
	
	void onMessage(Message message);

}
