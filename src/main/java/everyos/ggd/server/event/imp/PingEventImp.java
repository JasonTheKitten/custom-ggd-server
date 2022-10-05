package everyos.ggd.server.event.imp;

import everyos.ggd.server.event.Event;
import everyos.ggd.server.event.PingEvent;

public class PingEventImp implements PingEvent {

	@Override
	public int code() {
		return Event.PING;
	}

}
