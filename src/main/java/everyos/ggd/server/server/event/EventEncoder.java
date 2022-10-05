package everyos.ggd.server.server.event;

import everyos.ggd.server.event.Event;
import everyos.ggd.server.event.InitialStatePushEvent;
import everyos.ggd.server.event.MessageEvent;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.server.message.MessageEncoder;
import everyos.ggd.server.socket.SocketArray;
import everyos.ggd.server.socket.decoder.SocketDecoder;
import everyos.ggd.server.socket.decoder.imp.SocketDecoderImp;
import everyos.ggd.server.socket.encoder.SocketEncoder;
import everyos.ggd.server.socket.encoder.imp.SocketEncoderImp;
import everyos.ggd.server.socket.imp.SocketArrayImp;

public final class EventEncoder {
	
	//TODO: Dependency injection
	private static final SocketEncoder encoder = new SocketEncoderImp();
	private static final SocketDecoder decoder = new SocketDecoderImp();

	private EventEncoder() {}

	public static SocketArray encodeEvent(Event event) {
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
	
	private static SocketArray encodePingEvent(Event event) {
		SocketArray array = createSocketArray();
		array.set(0, Event.PING);
		
		return array;
	}
	
	private static SocketArray encodePongEvent(Event event) {
		SocketArray array = createSocketArray();
		array.set(0, Event.PONG);
		
		return array;
	}
	
	private static SocketArray encodeAuthenticateEvent(Event event) {
		SocketArray array = createSocketArray();
		array.set(0, Event.AUTHENTICATE);
		
		return array;
	}
	
	private static SocketArray encodeInitialStatePushEvent(InitialStatePushEvent event) {
		SocketArray array = createSocketArray();
		array.set(0, Event.INITIAL_STATE_PUSH);
		array.set(2, event.getClientId());
		array.set(3, event.getClientToken());
		
		return array;
	}
	
	private static SocketArray encodeMessageEvent(MessageEvent event) {
		SocketArray messages = createSocketArray();
		Message[] messagesArr = event.getMessages();
		for (int i = 0; i < messagesArr.length; i++) {
			messages.set(i, MessageEncoder.encode(messagesArr[i]));
		}
		
		SocketArray messageData = createSocketArray();
		messageData.set(0, "/m");
		messageData.set(1, messages);
		
		SocketArray array = createSocketArray();
		array.set(0, Event.MESSAGE);
		array.set(1, messageData);
		
		return array;
	}
	
	private static SocketArray createSocketArray() {
		return new SocketArrayImp(decoder, encoder);
	}
	
}
