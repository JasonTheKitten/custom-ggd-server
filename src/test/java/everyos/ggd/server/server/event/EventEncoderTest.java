package everyos.ggd.server.server.event;

import org.junit.jupiter.api.Assertions;
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
import everyos.ggd.server.socket.SocketArray;

public class EventEncoderTest {
	
	@Test
	@DisplayName("Can encode ping event")
	public void canEncodePingEvent() {
		PingEvent event = new PingEventImp();
		SocketArray encoded = EventEncoder.encodeEvent(event);
		Assertions.assertEquals(Event.PING, encoded.getInt(0));
	}
	
	@Test
	@DisplayName("Can encode pong event")
	public void canEncodePongEvent() {
		PongEvent event = new PongEventImp();
		SocketArray encoded = EventEncoder.encodeEvent(event);
		Assertions.assertEquals(Event.PONG, encoded.getInt(0));
	}
	
	@Test
	@DisplayName("Can encode authenticate event")
	public void canEncodeAuthenticateEvent() {
		AuthenticateEvent event = new AuthenticateEventImp();
		SocketArray encoded = EventEncoder.encodeEvent(event);
		Assertions.assertEquals(Event.AUTHENTICATE, encoded.getInt(0));
	}
	
	@Test
	@DisplayName("Can encode initial state push")
	public void canEncodeInitialStatePush() {
		InitialStatePushEvent event = new InitialStatePushEventImp(12, "XKCD");
		SocketArray encoded = EventEncoder.encodeEvent(event);
		Assertions.assertEquals(Event.INITIAL_STATE_PUSH, encoded.getInt(0));
		Assertions.assertEquals(12, encoded.getInt(2));
		Assertions.assertEquals("XKCD", encoded.getString(3));
	}
	
	@Test
	@DisplayName("Can encode empty message")
	public void canEncodeEmptyMessage() {
		MessageEvent event = new MessageEventImp(new Message[0]);
		SocketArray encoded = EventEncoder.encodeEvent(event);
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
		SocketArray encoded = EventEncoder.encodeEvent(event);
		SocketArray encodedData = encoded.getArray(1);
		SocketArray messages = encodedData.getArray(1);
		Assertions.assertArrayEquals(new int[] { 0, 1 }, messages.keys());
		SocketArray subArray = messages.getArray(1);
		Assertions.assertEquals(Message.CLEAR_SESSION_DATA, subArray.getInt(0));
	}
	
}
