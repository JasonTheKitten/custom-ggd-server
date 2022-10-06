package everyos.ggd.server.server.event.imp;

import java.util.ArrayList;
import java.util.List;

import everyos.ggd.server.event.Event;
import everyos.ggd.server.event.imp.AuthenticateEventImp;
import everyos.ggd.server.event.imp.MessageEventImp;
import everyos.ggd.server.event.imp.PingEventImp;
import everyos.ggd.server.event.imp.PongEventImp;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.server.event.EventDecoder;
import everyos.ggd.server.server.message.MessageDecoder;
import everyos.ggd.server.socket.SocketArray;

public class EventDecoderImp implements EventDecoder {
	
	private final MessageDecoder messageDecoder;
	
	public EventDecoderImp(MessageDecoder messageDecoder) {
		this.messageDecoder = messageDecoder;
	}

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
		List<Message> decodedMessages = new ArrayList<>();
		
		SocketArray messageData = array.getArray(1);
		SocketArray messages = messageData.getArray(1);
		for (int i = 0; messages.hasOverload(i) && messages.overload(i).hasKey(0); i++) {
			Message decodedMessage = messageDecoder.decode(
				messages.overload(i).getArray(0));
			decodedMessages.add(decodedMessage);
		}
		
		return new MessageEventImp(decodedMessages.toArray(new Message[decodedMessages.size()]));
	}
	
}
