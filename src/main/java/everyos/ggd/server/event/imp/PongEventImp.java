package everyos.ggd.server.event.imp;

import everyos.ggd.server.event.Event;
import everyos.ggd.server.event.PongEvent;

public class PongEventImp implements PongEvent {

	@Override
	public int code() {
		return Event.PONG;
	}

}
