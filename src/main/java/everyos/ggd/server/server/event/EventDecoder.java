package everyos.ggd.server.server.event;

import everyos.ggd.server.event.Event;
import everyos.ggd.server.socket.SocketArray;

public interface EventDecoder {

	Event decodeEvent(SocketArray array);
	
}
