package everyos.ggd.server.server.state;

import java.util.function.Consumer;

import everyos.ggd.server.common.SessionData;
import everyos.ggd.server.event.Event;
import everyos.ggd.server.event.imp.InitialStatePushEventImp;
import everyos.ggd.server.matchmaker.MatchMaker;
import everyos.ggd.server.message.imp.ServerConnectMessageImp;
import everyos.ggd.server.server.message.MessageUtil;

public class MatchMakerSocketState implements SocketState {
	
	private final MatchMaker matchMaker;

	public MatchMakerSocketState(MatchMaker matchMaker) {
		this.matchMaker = matchMaker;
	}

	@Override
	public void handleEvent(Event event, Consumer<Event> out) {
		switch (event.code()) {
		case Event.AUTHENTICATE:
			handleAuthenticateEvent(event, out);
			break;
		}
	}
	
	@Override
	public void ping(Consumer<Event> out) {
		
	}

	private void handleAuthenticateEvent(Event event, Consumer<Event> out) {
		SessionData sessionData = matchMaker.createSession();
		sendInitialStatePush(sessionData, out);
		sendMatchURL(sessionData, out);
		
		//TODO: Start at proper time
		matchMaker.fulfillCurrentMatch();
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