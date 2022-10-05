package everyos.ggd.server.event;

import everyos.ggd.server.event.imp.PingEventImp;
import everyos.ggd.server.event.imp.PongEventImp;

public interface Event {

	public static final int PING = 0;
	public static final int PONG = 1;
	public static final int INITIAL_STATE_PUSH = 2;
	public static final int MESSAGE = 3;
	public static final int AUTHENTICATE = 4;
	
	public int code();
	
	public static PingEvent createPingEvent() {
		return new PingEventImp();
	}

	public static Event createPongEvent() {
		return new PongEventImp();
	}
	
}
