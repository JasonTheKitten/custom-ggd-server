package everyos.ggd.server.server.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import everyos.ggd.server.message.ClearSessionDataMessage;
import everyos.ggd.server.message.CountdownMessage;
import everyos.ggd.server.message.EntityMoveMessage;
import everyos.ggd.server.message.EntityTeleportMessage;
import everyos.ggd.server.message.MatchFinishedMessage;
import everyos.ggd.server.message.MatchFinishedMessage.PlayerAward;
import everyos.ggd.server.message.MatchInitMessage;
import everyos.ggd.server.message.MatchStateUpdateMessage;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.PlayerInitMessage;
import everyos.ggd.server.message.PlayerStateUpdate;
import everyos.ggd.server.message.ServerConnectMessage;
import everyos.ggd.server.message.SessionDataSetMessage;
import everyos.ggd.server.message.imp.ClearSessionDataMessageImp;
import everyos.ggd.server.message.imp.CountdownMessageImp;
import everyos.ggd.server.message.imp.EntityMoveMessageImp;
import everyos.ggd.server.message.imp.EntityTeleportMessageImp;
import everyos.ggd.server.message.imp.MatchFinishedMessageImp;
import everyos.ggd.server.message.imp.MatchInitMessageImp;
import everyos.ggd.server.message.imp.MatchStateUpdateMessageImp;
import everyos.ggd.server.message.imp.PlayerInitMessageImp;
import everyos.ggd.server.message.imp.PlayerStateUpdateBuilder;
import everyos.ggd.server.message.imp.ServerConnectMessageImp;
import everyos.ggd.server.message.imp.SessionDataSetMessageImp;
import everyos.ggd.server.physics.imp.PositionImp;
import everyos.ggd.server.physics.imp.VelocityImp;
import everyos.ggd.server.server.message.imp.MessageEncoderImp;
import everyos.ggd.server.socket.SocketArray;
import everyos.ggd.server.socket.decoder.SocketDecoder;
import everyos.ggd.server.socket.decoder.imp.SocketDecoderImp;
import everyos.ggd.server.socket.encoder.SocketEncoder;
import everyos.ggd.server.socket.encoder.imp.SocketEncoderImp;

public class MessageEncoderTest {
	
	private MessageEncoder messageEncoder;
	
	@BeforeEach
	private void beforeEach() {
		SocketEncoder encoder = new SocketEncoderImp();
		SocketDecoder decoder = new SocketDecoderImp();
		messageEncoder = new MessageEncoderImp(encoder, decoder);
	}

	@Test
	@DisplayName("Can encode server connect message")
	public void canEncodeServerConnectMessage() {
		ServerConnectMessage message = new ServerConnectMessageImp("https://google.com/", "mtch-1234");
		SocketArray encoded = messageEncoder.encode(message);
		Assertions.assertEquals(Message.SERVER_CONNECT, encoded.getInt(0));
		Assertions.assertEquals("https://google.com/", encoded.getString(1));
		SocketArray matchNameData = encoded.getArray(3);
		Assertions.assertEquals(4, matchNameData.getInt(0));
		Assertions.assertEquals("mtch-1234", matchNameData.getString(1));
	}
	
	@Test
	@DisplayName("Can encode session data set message")
	public void canEncodeSessionDataSetMessage() {
		SessionDataSetMessage message = new SessionDataSetMessageImp(5, 1234, "shard0");
		SocketArray encoded = messageEncoder.encode(message);
		Assertions.assertEquals(Message.SESSION_DATA_SET, encoded.getInt(0));
		SocketArray sessionData = encoded.getArray(4);
		Assertions.assertEquals(6, sessionData.getInt(0));
		Assertions.assertEquals("mtch-1234", sessionData.getString(1));
		Assertions.assertEquals(1234, sessionData.getInt(2));
		Assertions.assertEquals("shard0", sessionData.getString(3));
	}
	
	@Test
	@DisplayName("Can encode match init message")
	public void canEncodeMatchInitMessage() {
		MatchInitMessage message = new MatchInitMessageImp("map03.json");
		SocketArray encoded = messageEncoder.encode(message);
		Assertions.assertEquals(Message.MATCH_INIT, encoded.getInt(0));
		SocketArray matchInitData = encoded.getArray(6);
		Assertions.assertEquals("map03.json", matchInitData.getString(2));
	}
	
	@Test
	@DisplayName("Can encode countdown message")
	public void canEncodeCountdownMessage() {
		CountdownMessage message = new CountdownMessageImp(3);
		SocketArray encoded = messageEncoder.encode(message);
		Assertions.assertEquals(Message.COUNTDOWN_TIMER, encoded.getInt(0));
		SocketArray timerInfo = encoded.getArray(7);
		Assertions.assertEquals(3, timerInfo.getInt(0));
	}
	
	@Test
	@DisplayName("Can encode initial player state message")
	public void canEncodeInitialPlayerStateMessage() {
		PlayerStateUpdate stateUpdate = createSamplePlayerStateUpdate();
		PlayerInitMessage message = new PlayerInitMessageImp(
			new PositionImp(0, 0),
			5,
			true,
			stateUpdate);
		SocketArray encoded = messageEncoder.encode(message);
		Assertions.assertEquals(Message.PLAYER_INIT, encoded.getInt(0));
		SocketArray initStruct = encoded.getArray(8);
		SocketArray initData = initStruct.getArray(0);
		Assertions.assertEquals(6, initData.getInt(0));
		Assertions.assertEquals(1, initData.getInt(1));
		SocketArray characterData = initData.getArray(5);
		Assertions.assertEquals(2, characterData.getInt(1));
		Assertions.assertEquals(6, characterData.getInt(2));
		Assertions.assertEquals(true, characterData.getBoolean(3));
		assertCanEncodePlayerStateUpdate(initStruct.getArray(1));
	}
	
	@Test
	@DisplayName("Can encode match state update message")
	public void canEncodeMatchStateUpdateMessage() {
		MatchStateUpdateMessage message = new MatchStateUpdateMessageImp(1, 2, 3);
		SocketArray encoded = messageEncoder.encode(message);
		Assertions.assertEquals(Message.MATCH_UPDATE_OR_FINISH, encoded.getInt(0));
		SocketArray matchStateUpdateData = encoded.getArray(10);
		Assertions.assertEquals(1, matchStateUpdateData.getInt(0));
		Assertions.assertEquals(true, matchStateUpdateData.getBoolean(5));
		Assertions.assertEquals(3, matchStateUpdateData.getInt(2));
		Assertions.assertEquals(1, matchStateUpdateData.getInt(3));
		Assertions.assertEquals(2, matchStateUpdateData.getInt(4));
	}
	
	@Test
	@DisplayName("Can encode match finish message")
	public void canEncodeMatchFinishMessage() {
		MatchFinishedMessage message = new MatchFinishedMessageImp(1, 2, new PlayerAward[] {});
		SocketArray encoded = messageEncoder.encode(message);
		Assertions.assertEquals(Message.MATCH_UPDATE_OR_FINISH, encoded.getInt(0));
		SocketArray matchFinishedData = encoded.getArray(10);
		Assertions.assertEquals(1, matchFinishedData.getInt(0));
		Assertions.assertEquals(true, matchFinishedData.getBoolean(6));
		Assertions.assertEquals(2, matchFinishedData.getInt(7));
	}
	
	@Test
	@DisplayName("Can encode entity move message")
	public void canEncodeEntityMoveMessage() {
		EntityMoveMessage message = new EntityMoveMessageImp(
			1, new PositionImp(2f, 3f), true);
		SocketArray encoded = messageEncoder.encode(message);
		Assertions.assertEquals(Message.ENTITY_MOVE, encoded.getInt(0));
		Assertions.assertEquals(1, encoded.getInt(1));
		SocketArray moveData = encoded.getArray(11);
		Assertions.assertEquals(1, moveData.getInt(0));
		Assertions.assertEquals(2f, moveData.getFloat(1));
		Assertions.assertEquals(3f, moveData.getFloat(2));
		Assertions.assertEquals(true, moveData.getBoolean(3));
	}
	
	@Test
	@DisplayName("Can encode entity teleport message")
	public void canEncodeEntityTeleportMessage() {
		EntityTeleportMessage message = new EntityTeleportMessageImp(
			1,
			new PositionImp(2f, 3f), new VelocityImp(4f, 5f),
			true);
		SocketArray encoded = messageEncoder.encode(message);
		Assertions.assertEquals(Message.ENTITY_TELEPORT, encoded.getInt(0));
		Assertions.assertEquals(1, encoded.getInt(1));
		SocketArray teleportData = encoded.getArray(13);
		Assertions.assertEquals(1, teleportData.getInt(0));
		Assertions.assertEquals(2f, teleportData.getFloat(1));
		Assertions.assertEquals(3f, teleportData.getFloat(2));
		Assertions.assertEquals(4f, teleportData.getFloat(3));
		Assertions.assertEquals(5f, teleportData.getFloat(4));
		Assertions.assertEquals(true, teleportData.getBoolean(5));
	}

	@Test
	@DisplayName("Can encode clear sessiondata message")
	public void canEncodeClearSessionDataMessage() {
		ClearSessionDataMessage message = new ClearSessionDataMessageImp();
		SocketArray encoded = messageEncoder.encode(message);
		Assertions.assertEquals(Message.CLEAR_SESSION_DATA, encoded.getInt(0));
	}
	
	private PlayerStateUpdate createSamplePlayerStateUpdate() {
		return new PlayerStateUpdateBuilder()
			.setEntityId(5)
			.setConnected(true)
			.setSpeed(1.1f)
			.build();
	}
	
	private void assertCanEncodePlayerStateUpdate(SocketArray encoded) {
		Assertions.assertEquals(encoded.getInt(0), 6);
		Assertions.assertEquals(encoded.getInt(1), 1);
		
		SocketArray updateData = encoded.getArray(2);
		Assertions.assertEquals(true, updateData.getBoolean(7));
		Assertions.assertEquals(1.1f, updateData.getFloat(1));
	}
	
}
