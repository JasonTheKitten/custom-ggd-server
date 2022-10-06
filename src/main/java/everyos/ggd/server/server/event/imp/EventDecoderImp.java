package everyos.ggd.server.server.event.imp;

import everyos.ggd.server.event.Event;
import everyos.ggd.server.event.imp.AuthenticateEventImp;
import everyos.ggd.server.event.imp.MessageEventImp;
import everyos.ggd.server.event.imp.PingEventImp;
import everyos.ggd.server.event.imp.PongEventImp;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.server.event.EventDecoder;
import everyos.ggd.server.socket.SocketArray;

public class EventDecoderImp implements EventDecoder {

	@Override
	public Event decodeEvent(SocketArray array) {
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
	
	private Event decodePingEvent(SocketArray array) {
		return new PingEventImp();
	}
	
	private Event decodePongEvent(SocketArray array) {
		return new PongEventImp();
	}
	
	private Event decodeAuthenticateEvent(SocketArray array) {
		return new AuthenticateEventImp();
	}
	
	private Event decodeMessageEvent(SocketArray array) {
		return new MessageEventImp(new Message[0]);
	}
	
}
