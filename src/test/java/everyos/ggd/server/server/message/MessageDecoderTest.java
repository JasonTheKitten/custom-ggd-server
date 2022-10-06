package everyos.ggd.server.server.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import everyos.ggd.server.message.ClearSessionDataMessage;
import everyos.ggd.server.message.EntityMoveMessage;
import everyos.ggd.server.message.EntityTeleportMessage;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.physics.Position;
import everyos.ggd.server.physics.Velocity;
import everyos.ggd.server.server.message.imp.MessageDecoderImp;
import everyos.ggd.server.socket.SocketArray;
import everyos.ggd.server.socket.decoder.SocketDecoder;
import everyos.ggd.server.socket.decoder.imp.SocketDecoderImp;
import everyos.ggd.server.socket.encoder.SocketEncoder;
import everyos.ggd.server.socket.encoder.imp.SocketEncoderImp;
import everyos.ggd.server.socket.imp.SocketArrayImp;

public class MessageDecoderTest {

	private SocketDecoder decoder;
	private SocketEncoder encoder;
	private MessageDecoder messageDecoder;
	
	@BeforeEach
	private void beforeEach() {
		decoder = new SocketDecoderImp();
		encoder = new SocketEncoderImp();
		messageDecoder = new MessageDecoderImp();
	}
	
	@Test
	@DisplayName("Can decode entity move message")
	public void canDecodeEntityMoveMessage() {
		SocketArray moveData = createSocketArray();
		moveData.set(0, 1);
		moveData.set(1, 2f);
		moveData.set(2, 3f);
		moveData.set(3, true);
		
		SocketArray array = createSocketArray();
		array.set(0, Message.ENTITY_MOVE);
		array.set(1, 1);
		array.set(11, moveData);
		
		EntityMoveMessage message = (EntityMoveMessage) messageDecoder.decode(array);
		Assertions.assertEquals(1, message.getEntityId());
		Assertions.assertEquals(true, message.isMoving());
		Position position = message.getRelativePosition();
		Assertions.assertEquals(2f, position.getX());
		Assertions.assertEquals(3f, position.getY());
	}
	
	@Test
	@DisplayName("Can decode entity teleport message")
	public void canDecodeEntityTeleportMessage() {
		SocketArray teleportData = createSocketArray();
		teleportData.set(0, 1);
		teleportData.set(1, 2f);
		teleportData.set(2, 3f);
		teleportData.set(3, 4f);
		teleportData.set(4, 5f);
		teleportData.set(5, true);
		
		SocketArray array = createSocketArray();
		array.set(0, Message.ENTITY_TELEPORT);
		array.set(1, 1);
		array.set(13, teleportData);
		
		
		EntityTeleportMessage message = (EntityTeleportMessage) messageDecoder.decode(array);
		Assertions.assertEquals(1, message.getEntityId());
		Assertions.assertEquals(true, message.isMoving());
		Position position = message.getPosition();
		Assertions.assertEquals(2f, position.getX());
		Assertions.assertEquals(3f, position.getY());
		Velocity velocity = message.getVelocity();
		Assertions.assertEquals(4f, velocity.getX());
		Assertions.assertEquals(5f, velocity.getY());
	}
	
	@Test
	@DisplayName("Can decode clear sessiondata message")
	public void canDecodeClearSessionDataMessage() {
		SocketArray array = createSocketArray();
		array.set(0, Message.CLEAR_SESSION_DATA);
		Message message = messageDecoder.decode(array);
		Assertions.assertInstanceOf(ClearSessionDataMessage.class, message);
	}
	
	private SocketArray createSocketArray() {
		return new SocketArrayImp(decoder, encoder);
	}
	
}
