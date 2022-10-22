package everyos.ggd.server.game.vanilla.state.bot;

import java.util.ArrayList;
import java.util.List;

import everyos.ggd.server.game.Player;
import everyos.ggd.server.message.Message;

public class BotPlayerImp implements Player {

	private final int id;
	private final String authenticationKey;
	private final List<Message> incomingMessages = new ArrayList<>();
	private final List<Message> outgoingMessages = new ArrayList<>();

	public BotPlayerImp(int id, String authenticationKey) {
		this.id = id;
		this.authenticationKey = authenticationKey;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getAuthenticationKey() {
		return this.authenticationKey;
	}

	@Override
	public boolean isBot() {
		return true;
	}

	@Override
	public void onMessageFromServer(Message message) {
		incomingMessages.add(message);
	}

	@Override
	public List<Message> getQueuedMessagesFromClient() {
		return outgoingMessages;
	}

	@Override
	public void ping() {
		
	}

}
