package everyos.ggd.server.server.state;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import everyos.ggd.server.event.Event;
import everyos.ggd.server.event.MessageEvent;
import everyos.ggd.server.event.imp.InitialStatePushEventImp;
import everyos.ggd.server.event.imp.MessageEventImp;
import everyos.ggd.server.game.HumanPlayer;
import everyos.ggd.server.game.Match;
import everyos.ggd.server.message.Message;

public class MatchSocketState implements SocketState {

	private final Match match;
	private final HumanPlayer player;
	private final List<Message> queuedMessages = new ArrayList<>();

	public MatchSocketState(Match match, HumanPlayer player) {
		this.match = match;
		this.player = player;
	}

	@Override
	public void handleEvent(Event event, Consumer<Event> out) {
		switch (event.code()) {
		case Event.AUTHENTICATE:
			handleAuthenticateEvent(out);
			break;
		case Event.MESSAGE:
			handleMessageEvent((MessageEvent) event);
		}
	}

	@Override
	public void ping(Consumer<Event> out) {
		queuedMessages.addAll(player.getQueuedMessagesFromServer());
		if (queuedMessages.size() > 0) {
			sendQueuedMessages(out);
		}
	}
	
	@Override
	public void onDisconnect() {
		player.setConnected(false);
	}

	private void handleAuthenticateEvent(Consumer<Event> out) {
		sendInitialStatePush(match.getId(), player.getAuthenticationKey(), out);
	}
	
	private void handleMessageEvent(MessageEvent event) {
		for (Message message: event.getMessages()) {
			player.onMessageFromClient(message);
		}
	}

	private void sendQueuedMessages(Consumer<Event> out) {
		Message[] messages = queuedMessages.toArray(new Message[queuedMessages.size()]);
		out.accept(new MessageEventImp(messages));
		queuedMessages.clear();
	}
	
	private void sendInitialStatePush(int matchId, String authKey, Consumer<Event> out) {
		Event initialStatePush = new InitialStatePushEventImp(matchId, authKey);
		out.accept(initialStatePush);
	}

}
