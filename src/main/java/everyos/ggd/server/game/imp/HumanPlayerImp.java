package everyos.ggd.server.game.imp;

import java.util.ArrayList;
import java.util.List;

import everyos.ggd.server.game.HumanPlayer;
import everyos.ggd.server.message.Message;

public class HumanPlayerImp implements HumanPlayer {

	private final int playerId;
	private final String authenticationKey;
	private final List<Message> messageFromServerQueue;
	private final List<Message> messageFromClientQueue;

	public HumanPlayerImp(int playerId, String authenticationKey) {
		this.playerId = playerId;
		this.authenticationKey = authenticationKey;
		this.messageFromServerQueue = new ArrayList<>(1);
		this.messageFromClientQueue = new ArrayList<>(1);
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
		return false;
	}

	@Override
	public void onMessageFromServer(Message message) {
		synchronized (messageFromServerQueue) {
			messageFromServerQueue.add(message);
		}
	}

	//TODO: Re-send match info upon socket reconnect
	@Override
	public List<Message> getQueuedMessagesFromServer() {
		synchronized (messageFromServerQueue) {
			List<Message> messages = List.copyOf(messageFromServerQueue);
			messageFromServerQueue.clear();
			
			return messages;
		}
	}

	@Override
	public void onMessageFromClient(Message message) {
		messageFromClientQueue.add(message);
	}
	
	@Override
	public List<Message> getQueuedMessagesFromClient() {
		List<Message> messages = List.copyOf(messageFromClientQueue);
		messageFromClientQueue.clear();
		
		return messages;
	}

	@Override
	public void ping() {
		
	}
	
}
