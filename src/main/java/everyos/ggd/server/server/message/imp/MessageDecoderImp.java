package everyos.ggd.server.server.message.imp;

import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.imp.ClearSessionDataMessageImp;
import everyos.ggd.server.message.imp.EntityTeleportMessageImp;
import everyos.ggd.server.physics.Position;
import everyos.ggd.server.physics.Velocity;
import everyos.ggd.server.physics.imp.PositionImp;
import everyos.ggd.server.physics.imp.VelocityImp;
import everyos.ggd.server.server.message.MessageDecoder;
import everyos.ggd.server.socket.SocketArray;

public class MessageDecoderImp implements MessageDecoder {

	@Override
	public Message decode(SocketArray encoded) {
		int messageType = encoded.getInt(0);
		switch (messageType) {
		case Message.ENTITY_TELEPORT:
			return decodeEntityTeleportMessage(encoded);
		case Message.CLEAR_SESSION_DATA:
			return decodeClearSessionDataMessage();
		case Message.INTERNAL:
			throw new RuntimeException("Message type is internal");
		default:
			throw new RuntimeException("No such message type [" + messageType + "]");
		}
	}

	private Message decodeEntityTeleportMessage(SocketArray encoded) {
		int playerId = encoded.getInt(1);
		SocketArray teleportData = encoded.getArray(13);
		Position position = new PositionImp(teleportData.getFloat(1), teleportData.getFloat(2));
		Velocity velocity = new VelocityImp(teleportData.getFloat(3), teleportData.getFloat(4));
		boolean isMoving = teleportData.getBoolean(5);
		
		return new EntityTeleportMessageImp(playerId, position, velocity, isMoving);
	}
	
	// The client shpuldn't ever send us this, but it makes testing easier
	private Message decodeClearSessionDataMessage() {
		return new ClearSessionDataMessageImp();
	}

}
