package everyos.ggd.server.server.event;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import everyos.ggd.server.event.AuthenticateEvent;
import everyos.ggd.server.event.Event;
import everyos.ggd.server.event.MessageEvent;
import everyos.ggd.server.event.PingEvent;
import everyos.ggd.server.event.PongEvent;
import everyos.ggd.server.event.imp.MessageEventImp;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.server.event.imp.EventDecoderImp;
import everyos.ggd.server.server.event.imp.EventEncoderImp;
import everyos.ggd.server.server.message.MessageEncoder;
import everyos.ggd.server.server.message.imp.MessageEncoderImp;
import everyos.ggd.server.socket.SocketArray;
import everyos.ggd.server.socket.decoder.SocketDecoder;
import everyos.ggd.server.socket.decoder.imp.SocketDecoderImp;
import everyos.ggd.server.socket.encoder.SocketEncoder;
import everyos.ggd.server.socket.encoder.imp.SocketEncoderImp;

public class EventDecoderTest {
	
	private EventDecoder eventDecoder;
	
	@BeforeEach
	private void beforeEach() {
		this.eventDecoder = new EventDecoderImp();
	}

	@Test
	@DisplayName("Can decode ping event")
	public void canDecodePingEvent() {
		SocketArray array = Mockito.mock(SocketArray.class);
		Mockito.when(array.getInt(0)).thenReturn(Event.PING);
		Event decoded = eventDecoder.decodeEvent(array);
		Assertions.assertInstanceOf(PingEvent.class, decoded);
		Assertions.assertEquals(Event.PING, decoded.code());
	}
	
	@Test
	@DisplayName("Can decode pong event")
	public void canDecodePongEvent() {
		SocketArray array = Mockito.mock(SocketArray.class);
		Mockito.when(array.getInt(0)).thenReturn(Event.PONG);
		Event decoded = eventDecoder.decodeEvent(array);
		Assertions.assertInstanceOf(PongEvent.class, decoded);
		Assertions.assertEquals(Event.PONG, decoded.code());
	}
	
	@Test
	@DisplayName("Can decode authenticate event")
	public void canDecodeAuthenticateEvent() {
		SocketArray array = Mockito.mock(SocketArray.class);
		Mockito.when(array.getInt(0)).thenReturn(Event.AUTHENTICATE);
		Event decoded = eventDecoder.decodeEvent(array);
		Assertions.assertInstanceOf(AuthenticateEvent.class, decoded);
		Assertions.assertEquals(Event.AUTHENTICATE, decoded.code());
	}
	
	// This creates a dependency on the success of the encoder tests,
	// but is a lot easier to work with
	@Test
	@DisplayName("Can decode empty message")
	public void canDecodeEmptyMessage() {
		EventEncoder eventEncoder = createEventEncoder();
		MessageEvent targetEvent = new MessageEventImp(new Message[0]);
		SocketArray array = eventEncoder.encodeEvent(targetEvent);
		MessageEvent event = (MessageEvent) eventDecoder.decodeEvent(array);
		Assertions.assertEquals(Event.MESSAGE, event.code());
		Assertions.assertArrayEquals(new Message[0], event.getMessages());
	}

	private EventEncoder createEventEncoder() {
		SocketEncoder encoder = new SocketEncoderImp();
		SocketDecoder decoder = new SocketDecoderImp();
		MessageEncoder messageEncoder = new MessageEncoderImp(encoder, decoder);
		
		return new EventEncoderImp(encoder, decoder, messageEncoder);
	}
	
}
