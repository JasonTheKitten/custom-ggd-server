package everyos.ggd.server.server.event;

import everyos.ggd.server.event.Event;
import everyos.ggd.server.socket.SocketArray;

public interface EventEncoder {

	SocketArray encodeEvent(Event event);
	
	
}
