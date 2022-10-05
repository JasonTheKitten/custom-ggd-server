package everyos.ggd.server.server.event;

import org.junit.jupiter.api.Assertions;
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
import everyos.ggd.server.socket.SocketArray;

public class EventDecoderTest {

	@Test
	@DisplayName("Can decode ping event")
	public void canDecodePingEvent() {
		SocketArray array = Mockito.mock(SocketArray.class);
		Mockito.when(array.getInt(0)).thenReturn(Event.PING);
		Event decoded = EventDecoder.decodeEvent(array);
		Assertions.assertInstanceOf(PingEvent.class, decoded);
		Assertions.assertEquals(Event.PING, decoded.code());
	}
	
	@Test
	@DisplayName("Can decode pong event")
	public void canDecodePongEvent() {
		SocketArray array = Mockito.mock(SocketArray.class);
		Mockito.when(array.getInt(0)).thenReturn(Event.PONG);
		Event decoded = EventDecoder.decodeEvent(array);
		Assertions.assertInstanceOf(PongEvent.class, decoded);
		Assertions.assertEquals(Event.PONG, decoded.code());
	}
	
	@Test
	@DisplayName("Can decode authenticate event")
	public void canDecodeAuthenticateEvent() {
		SocketArray array = Mockito.mock(SocketArray.class);
		Mockito.when(array.getInt(0)).thenReturn(Event.AUTHENTICATE);
		Event decoded = EventDecoder.decodeEvent(array);
		Assertions.assertInstanceOf(AuthenticateEvent.class, decoded);
		Assertions.assertEquals(Event.AUTHENTICATE, decoded.code());
	}
	
	// This creates a dependency on the success of the encoder tests,
	// but is a lot easier to work with
	@Test
	@DisplayName("Can decode empty message")
	public void canDecodeEmptyMessage() {
		MessageEvent targetEvent = new MessageEventImp(new Message[0]);
		SocketArray array = EventEncoder.encodeEvent(targetEvent);
		MessageEvent event = (MessageEvent) EventDecoder.decodeEvent(array);
		Assertions.assertEquals(Event.MESSAGE, event.code());
		Assertions.assertArrayEquals(new Message[0], event.getMessages());
	}
	
}
