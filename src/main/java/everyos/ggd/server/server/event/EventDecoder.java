package everyos.ggd.server.server.event;

import everyos.ggd.server.event.Event;
import everyos.ggd.server.event.imp.AuthenticateEventImp;
import everyos.ggd.server.event.imp.MessageEventImp;
import everyos.ggd.server.event.imp.PingEventImp;
import everyos.ggd.server.event.imp.PongEventImp;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.socket.SocketArray;

public class EventDecoder {

	private EventDecoder() {}
	
	public static Event decodeEvent(SocketArray array) {
		int code = array.getInt(0);
		switch (code) {
		case Event.PING:
			return decodePingEvent(array);
		case Event.PONG:
			return decodePongEvent(array);
		case Event.AUTHENTICATE:
			return decodeAuthenticateEvent(array);
		case Event.MESSAGE:
			return decodeMessageEvent(array);
		default:
			throw new RuntimeException("No such event [" + code + "]");
		}
	}
	
	private static Event decodePingEvent(SocketArray array) {
		return new PingEventImp();
	}
	
	private static Event decodePongEvent(SocketArray array) {
		return new PongEventImp();
	}
	
	private static Event decodeAuthenticateEvent(SocketArray array) {
		return new AuthenticateEventImp();
	}
	
	private static Event decodeMessageEvent(SocketArray array) {
		return new MessageEventImp(new Message[0]);
	}
	
}
