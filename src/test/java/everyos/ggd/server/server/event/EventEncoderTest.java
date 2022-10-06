package everyos.ggd.server.server.event;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import everyos.ggd.server.event.AuthenticateEvent;
import everyos.ggd.server.event.Event;
import everyos.ggd.server.event.InitialStatePushEvent;
import everyos.ggd.server.event.MessageEvent;
import everyos.ggd.server.event.PingEvent;
import everyos.ggd.server.event.PongEvent;
import everyos.ggd.server.event.imp.AuthenticateEventImp;
import everyos.ggd.server.event.imp.InitialStatePushEventImp;
import everyos.ggd.server.event.imp.MessageEventImp;
import everyos.ggd.server.event.imp.PingEventImp;
import everyos.ggd.server.event.imp.PongEventImp;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.imp.ClearSessionDataMessageImp;
import everyos.ggd.server.server.event.imp.EventEncoderImp;
import everyos.ggd.server.server.message.MessageEncoder;
import everyos.ggd.server.server.message.imp.MessageEncoderImp;
import everyos.ggd.server.socket.SocketArray;
import everyos.ggd.server.socket.decoder.SocketDecoder;
import everyos.ggd.server.socket.decoder.imp.SocketDecoderImp;
import everyos.ggd.server.socket.encoder.SocketEncoder;
import everyos.ggd.server.socket.encoder.imp.SocketEncoderImp;

public class EventEncoderTest {
	
	private EventEncoder eventEncoder;
	
	@BeforeEach
	private void beforeEach() {
		SocketEncoder encoder = new SocketEncoderImp();
		SocketDecoder decoder = new SocketDecoderImp();
		MessageEncoder messageEncoder = new MessageEncoderImp(encoder, decoder);
		eventEncoder = new EventEncoderImp(encoder, decoder, messageEncoder);
	}
	
	@Test
	@DisplayName("Can encode ping event")
	public void canEncodePingEvent() {
		PingEvent event = new PingEventImp();
		SocketArray encoded = eventEncoder.encodeEvent(event);
		Assertions.assertEquals(Event.PING, encoded.getInt(0));
	}
	
	@Test
	@DisplayName("Can encode pong event")
	public void canEncodePongEvent() {
		PongEvent event = new PongEventImp();
		SocketArray encoded = eventEncoder.encodeEvent(event);
		Assertions.assertEquals(Event.PONG, encoded.getInt(0));
	}
	
	@Test
	@DisplayName("Can encode authenticate event")
	public void canEncodeAuthenticateEvent() {
		AuthenticateEvent event = new AuthenticateEventImp();
		SocketArray encoded = eventEncoder.encodeEvent(event);
		Assertions.assertEquals(Event.AUTHENTICATE, encoded.getInt(0));
	}
	
	@Test
	@DisplayName("Can encode initial state push")
	public void canEncodeInitialStatePush() {
		InitialStatePushEvent event = new InitialStatePushEventImp(12, "XKCD");
		SocketArray encoded = eventEncoder.encodeEvent(event);
		Assertions.assertEquals(Event.INITIAL_STATE_PUSH, encoded.getInt(0));
		Assertions.assertEquals(12, encoded.getInt(2));
		Assertions.assertEquals("XKCD", encoded.getString(3));
	}
	
	@Test
	@DisplayName("Can encode empty message")
	public void canEncodeEmptyMessage() {
		MessageEvent event = new MessageEventImp(new Message[0]);
		SocketArray encoded = eventEncoder.encodeEvent(event);
		Assertions.assertEquals(Event.MESSAGE, encoded.getInt(0));
		SocketArray encodedData = encoded.getArray(1);
		Assertions.assertArrayEquals(new int[] { 0, 1 }, encodedData.keys());
		Assertions.assertEquals("/m", encodedData.getString(0));
		SocketArray messages = encodedData.getArray(1);
		Assertions.assertArrayEquals(new int[0], messages.keys());
	}
	
	@Test
	@DisplayName("Can encode multiple messages")
	public void canEncodeMultipleMessages() {
		MessageEvent event = new MessageEventImp(new Message[] {
			new ClearSessionDataMessageImp(),
			new ClearSessionDataMessageImp()
		});
		SocketArray encoded = eventEncoder.encodeEvent(event);
		SocketArray encodedData = encoded.getArray(1);
		SocketArray messages = encodedData.getArray(1);
		Assertions.assertEquals(true, messages.hasOverload(1));
		Assertions.assertEquals(false, messages.hasOverload(2));
		SocketArray subArray = messages.overload(0).getArray(0);
		Assertions.assertEquals(Message.CLEAR_SESSION_DATA, subArray.getInt(0));
	}
	
}
