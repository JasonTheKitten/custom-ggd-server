package everyos.ggd.server.event.imp;

import everyos.ggd.server.event.AuthenticateEvent;
import everyos.ggd.server.event.Event;

public class AuthenticateEventImp implements AuthenticateEvent {

	@Override
	public int code() {
		return Event.AUTHENTICATE;
	}
	
	//TODO: Extra fields

}
