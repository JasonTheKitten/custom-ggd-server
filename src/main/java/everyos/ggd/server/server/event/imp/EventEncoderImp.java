package everyos.ggd.server.server.event.imp;

import everyos.ggd.server.event.Event;
import everyos.ggd.server.event.InitialStatePushEvent;
import everyos.ggd.server.event.MessageEvent;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.server.event.EventEncoder;
import everyos.ggd.server.server.message.MessageEncoder;
import everyos.ggd.server.socket.SocketArray;
import everyos.ggd.server.socket.decoder.SocketDecoder;
import everyos.ggd.server.socket.encoder.SocketEncoder;
import everyos.ggd.server.socket.imp.SocketArrayImp;

public class EventEncoderImp implements EventEncoder {

	private final SocketEncoder encoder;
	private final SocketDecoder decoder;
	private final MessageEncoder messageEncoder;
	
	public EventEncoderImp(SocketEncoder encoder, SocketDecoder decoder, MessageEncoder messageEncoder) {
		this.encoder = encoder;
		this.decoder = decoder;
		this.messageEncoder = messageEncoder;
	}

	@Override
	public SocketArray encodeEvent(Event event) {
		switch (event.code()) {
		case Event.PING:
			return encodePingEvent(event);
		case Event.PONG:
			return encodePongEvent(event);
		case Event.AUTHENTICATE:
			return encodeAuthenticateEvent(event);
		case Event.INITIAL_STATE_PUSH:
			return encodeInitialStatePushEvent((InitialStatePushEvent) event);
		case Event.MESSAGE:
			return encodeMessageEvent((MessageEvent) event);
		default:
			throw new RuntimeException("No such event [" + event.code() + "]");
		}
	};
	
	private SocketArray encodePingEvent(Event event) {
		SocketArray array = createSocketArray();
		array.set(0, Event.PING);
		
		return array;
	}
	
	private SocketArray encodePongEvent(Event event) {
		SocketArray array = createSocketArray();
		array.set(0, Event.PONG);
		
		return array;
	}
	
	private SocketArray encodeAuthenticateEvent(Event event) {
		SocketArray array = createSocketArray();
		array.set(0, Event.AUTHENTICATE);
		
		return array;
	}
	
	private SocketArray encodeInitialStatePushEvent(InitialStatePushEvent event) {
		SocketArray array = createSocketArray();
		array.set(0, Event.INITIAL_STATE_PUSH);
		array.set(2, event.getClientId());
		array.set(3, event.getClientToken());
		
		return array;
	}
	
	private SocketArray encodeMessageEvent(MessageEvent event) {
		SocketArray messages = createSocketArray();
		Message[] messagesArr = event.getMessages();
		for (int i = 0; i < messagesArr.length; i++) {
			messages
				.overload(i)
				.set(0, messageEncoder.encode(messagesArr[i]));
		}
		messages.overload(0);
		
		SocketArray messageData = createSocketArray();
		messageData.set(0, "/m");
		messageData.set(1, messages);
		
		SocketArray array = createSocketArray();
		array.set(0, Event.MESSAGE);
		array.set(1, messageData);
		
		return array;
	}
	
	private SocketArray createSocketArray() {
		return new SocketArrayImp(decoder, encoder);
	}
	
}
