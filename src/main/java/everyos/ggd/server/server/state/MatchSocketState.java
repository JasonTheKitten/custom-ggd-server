package everyos.ggd.server.server.state;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import everyos.ggd.server.event.Event;
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
			sendInitialStatePush(match.getId(), player.getAuthenticationKey(), out);
			break;
		default:
			List<Message> messages = match
				.handleEvent(player, event);
			queuedMessages.addAll(messages);
		}
	}

	@Override
	public void ping(Consumer<Event> out) {
		queuedMessages.addAll(player.getQueuedMessages());
		if (queuedMessages.size() > 0) {
			sendQueuedMessages(out);
		}
	}

	private void sendQueuedMessages(Consumer<Event> out) {
		Message[] messages = new Message[] { queuedMessages.remove(0) };//queuedMessages.toArray(new Message[queuedMessages.size()]);
		out.accept(new MessageEventImp(messages));
		//queuedMessages.clear();
	}
	
	private void sendInitialStatePush(int matchId, String authKey, Consumer<Event> out) {
		Event initialStatePush = new InitialStatePushEventImp(matchId, authKey);
		out.accept(initialStatePush);
	}

}
