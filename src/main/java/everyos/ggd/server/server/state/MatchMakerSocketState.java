package everyos.ggd.server.server.state;

import java.util.function.Consumer;

import everyos.ggd.server.event.Event;
import everyos.ggd.server.event.imp.InitialStatePushEventImp;
import everyos.ggd.server.matchmaker.MatchMaker;
import everyos.ggd.server.message.imp.ServerConnectMessageImp;
import everyos.ggd.server.server.message.MessageUtil;
import everyos.ggd.server.session.SessionData;

public class MatchMakerSocketState implements SocketState {
	
	private static final int maxPlayersConnected = Integer.valueOf(System.getProperty("MAX_PLAYERS", "1"));
	private static int playersConnected = 0;
	
	private final MatchMaker matchMaker;
	
	private boolean hasSentInitialStatePush = false;

	public MatchMakerSocketState(MatchMaker matchMaker) {
		this.matchMaker = matchMaker;
	}

	@Override
	public void handleEvent(Event event, Consumer<Event> out) {
		switch (event.code()) {
		case Event.PING:
			handleAuthenticateEvent(event, out);
			break;
		}
	}
	
	@Override
	public void ping(Consumer<Event> out) {
		
	}
	
	@Override
	public void onDisconnect() {
		
	}

	private void handleAuthenticateEvent(Event event, Consumer<Event> out) {
		if (hasSentInitialStatePush) {
			return;
		}
		hasSentInitialStatePush = true;
		
		SessionData sessionData = matchMaker.createSession();
		sendInitialStatePush(sessionData, out);
		sendMatchURL(sessionData, out);
		
		//TODO: Start at proper time
		if (++playersConnected == maxPlayersConnected) {
			matchMaker.fulfillCurrentMatch();
			playersConnected = 0;
		}
	}

	private void sendMatchURL(SessionData sessionData, Consumer<Event> out) {
		String url = sessionData.toString();
		String matchName = "mtch-" + sessionData.matchId();
		out.accept(MessageUtil.message(new ServerConnectMessageImp(
			url, matchName
		)));
	}

	private void sendInitialStatePush(SessionData sessionData, Consumer<Event> out) {
		Event initialStatePush = new InitialStatePushEventImp(
			sessionData.matchId(),
			sessionData.authenticationKey());
		out.accept(initialStatePush);
	}

}
