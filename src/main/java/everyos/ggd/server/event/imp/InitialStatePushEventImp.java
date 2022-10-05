package everyos.ggd.server.event.imp;

import everyos.ggd.server.event.Event;
import everyos.ggd.server.event.InitialStatePushEvent;

public class InitialStatePushEventImp implements InitialStatePushEvent {

	private final int clientId;
	private final String clientToken;

	public InitialStatePushEventImp(int clientId, String clientToken) {
		this.clientId = clientId;
		this.clientToken = clientToken;
	}

	@Override
	public int code() {
		return Event.INITIAL_STATE_PUSH;
	}
	
	@Override
	public int getClientId() {
		return clientId;
	}
	
	@Override
	public String getClientToken() {
		return clientToken;
	}

}
