package everyos.ggd.server.game.imp;

import java.util.ArrayList;
import java.util.List;

import everyos.ggd.server.game.HumanPlayer;
import everyos.ggd.server.message.Message;

public class HumanPlayerImp implements HumanPlayer {

	private final int playerId;
	private final String authenticationKey;
	private final boolean isBot;
	private final List<Message> messageQueue;

	public HumanPlayerImp(int playerId, String authenticationKey, boolean isBot) {
		this.playerId = playerId;
		this.authenticationKey = authenticationKey;
		this.isBot = isBot;
		this.messageQueue = new ArrayList<>();
	}
	
	@Override
	public int getId() {
		return playerId;
	}

	@Override
	public String getAuthenticationKey() {
		return authenticationKey;
	}
	
	@Override
	public boolean isBot() {
		return isBot;
	}

	
	@Override
	public void onMessage(Message message) {
		messageQueue.add(message);
	}

	//TODO: Re-send match info upon socket reconnect
	@Override
	public List<Message> getQueuedMessages() {
		List<Message> messages = List.copyOf(messageQueue);
		messageQueue.clear();
		
		return messages;
	}
	
}
