package everyos.ggd.server.server.message.imp;

import everyos.ggd.server.message.CountdownMessage;
import everyos.ggd.server.message.EntityMoveMessage;
import everyos.ggd.server.message.EntityTeleportMessage;
import everyos.ggd.server.message.MatchFinishedMessage;
import everyos.ggd.server.message.MatchInitMessage;
import everyos.ggd.server.message.MatchStateUpdateMessage;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.PlayerInitMessage;
import everyos.ggd.server.message.PlayerStateUpdate;
import everyos.ggd.server.message.PlayerStateUpdate.UpgradeData;
import everyos.ggd.server.message.PlayerStateUpdateMessage;
import everyos.ggd.server.message.ServerConnectMessage;
import everyos.ggd.server.message.SessionDataSetMessage;
import everyos.ggd.server.message.SpiritInitMessage;
import everyos.ggd.server.message.SpiritStateUpdate;
import everyos.ggd.server.message.SpiritStateUpdateMessage;
import everyos.ggd.server.message.imp.MatchStateUpdateMessageImp;
import everyos.ggd.server.message.imp.PlayerStateUpdateMessageImp;
import everyos.ggd.server.server.message.MessageEncoder;
import everyos.ggd.server.socket.SocketArray;
import everyos.ggd.server.socket.decoder.SocketDecoder;
import everyos.ggd.server.socket.encoder.SocketEncoder;
import everyos.ggd.server.socket.imp.SocketArrayImp;

public class MessageEncoderImp implements MessageEncoder {
	
	private final SocketEncoder encoder;
	private final SocketDecoder decoder;
	
	public MessageEncoderImp(SocketEncoder encoder, SocketDecoder decoder) {
		this.encoder = encoder;
		this.decoder = decoder;
	}

	@Override
	public SocketArray encode(Message message) {
		switch (message.getType()) {
		case Message.SERVER_CONNECT:
			return encodeServerConnectMessage((ServerConnectMessage) message);
		case Message.SESSION_DATA_SET:
			return encodeSessionDataSetMessage((SessionDataSetMessage) message);
		case Message.MATCH_INIT:
			return encodeMatchInitMessage((MatchInitMessage) message);
		case Message.COUNTDOWN_TIMER:
			return encodeCountdownMessage((CountdownMessage) message);
		case Message.ENTITY_INIT:
			return encodePlayerOrSpiritUpdateMessage(message);
		case Message.MATCH_UPDATE_OR_FINISH:
			return encodeMatchStateUpdateOrFinishMessage(message);
		case Message.ENTITY_MOVE:
			return encodeEntityMoveMessage((EntityMoveMessage) message);
		case Message.ENTITY_UPDATE:
			return encodeEntityStateUpdateMessage(message);
		case Message.ENTITY_TELEPORT:
			return encodeEntityTeleportMessage((EntityTeleportMessage) message);
		case Message.CLEAR_SESSION_DATA:
			return encodeClearSessionDataMessage();
		case Message.INTERNAL:
			throw new RuntimeException("Message type is internal");
		default:
			throw new RuntimeException("No such message type [" + message.getType() + "]");
		}
	}

	private SocketArray encodeServerConnectMessage(ServerConnectMessage message) {
		SocketArray matchNameData = createSocketArray();
		matchNameData.set(0, 4);
		matchNameData.set(1, message.getMatchName());
		
		SocketArray encoded = createSocketArray();
		encoded.set(0, Message.SERVER_CONNECT);
		encoded.set(1, message.getURL());
		encoded.set(3, matchNameData);
		
		return encoded;
	}
	
	private SocketArray encodeSessionDataSetMessage(SessionDataSetMessage message) {
		SocketArray sessionData = createSocketArray();
		sessionData.set(0, message.getEntityId() + 1);
		sessionData.set(1, "mtch-" + message.getMatchId());
		sessionData.set(2, message.getMatchId());
		sessionData.set(3, message.getServerName());
		
		SocketArray encoded = createSocketArray();
		encoded.set(0, Message.SESSION_DATA_SET);
		encoded.set(4, sessionData);
		
		return encoded;
	}
	
	private SocketArray encodeMatchInitMessage(MatchInitMessage message) {
		SocketArray matchInitData = createSocketArray();
		matchInitData.set(2, message.getMapName());
		
		SocketArray encoded = createSocketArray();
		encoded.set(0, Message.MATCH_INIT);
		encoded.set(6, matchInitData);
		
		return encoded;
	}
	
	private SocketArray encodeCountdownMessage(CountdownMessage message) {
		SocketArray timerInfo = createSocketArray();
		timerInfo.set(0, message.getSecondsLeft());
		
		SocketArray encoded = createSocketArray();
		encoded.set(0, Message.COUNTDOWN_TIMER);
		encoded.set(7, timerInfo);
		
		return encoded;
	}
	
	private SocketArray encodePlayerOrSpiritUpdateMessage(Message message) {
		if (message instanceof PlayerInitMessage) {
			return encodePlayerInitMessage((PlayerInitMessage) message);
		} else {
			return encodeSpiritInitMessage((SpiritInitMessage) message);
		}
	}

	private SocketArray encodePlayerInitMessage(PlayerInitMessage message) {
		PlayerStateUpdate state = message.getStateUpdate();
		
		SocketArray characterData = createSocketArray();
		characterData.set(1, message.getCharacterId() < 4 ? 1 : 2);
		characterData.set(2, message.getCharacterId() + 1);
		characterData.set(3, message.isBot());
		
		SocketArray initData = createSocketArray();
		initData.set(0, state.getEntityId() + 1);
		initData.set(1, 1);
		initData.set(2, message.getInitialPosition().getX());
		initData.set(3, message.getInitialPosition().getY());
		initData.set(5, characterData);
		
		SocketArray initStruct = createSocketArray();
		initStruct.set(0, initData);
		initStruct.set(1, encodePlayerStateUpdate(message.getStateUpdate()));
		
		SocketArray encoded = createSocketArray();
		encoded.set(0, Message.ENTITY_INIT);
		encoded.set(8, initStruct);
		
		return encoded;
	}
	
	private SocketArray encodeSpiritInitMessage(SpiritInitMessage message) {
		SocketArray characterData = createSocketArray();
		
		SocketArray initData = createSocketArray();
		initData.set(0, message.getEntityId() + 1);
		initData.set(1, 16);
		initData.set(2, message.getInitialPosition().getX());
		initData.set(3, message.getInitialPosition().getY());
		initData.set(5, characterData);
		
		SocketArray initStruct = createSocketArray();
		initStruct.set(0, initData);
		initStruct.set(1, encodeSpiritStateUpdate(message.getStateUpdate()));
		
		SocketArray encoded = createSocketArray();
		encoded.set(0, Message.ENTITY_INIT);
		encoded.set(8, initStruct);
		
		return encoded;
	}
	
	private SocketArray encodeMatchStateUpdateOrFinishMessage(Message message) {
		if (message instanceof MatchStateUpdateMessageImp) {
			return encodeMatchStateUpdateMessage((MatchStateUpdateMessage) message);
		} else {
			return encodeMatchFinishedMessage((MatchFinishedMessage) message);
		}
	}

	private SocketArray encodeMatchStateUpdateMessage(MatchStateUpdateMessage message) {
		SocketArray matchStateUpdateData = createSocketArray();
		matchStateUpdateData.set(0, 1);
		matchStateUpdateData.set(5, true);
		matchStateUpdateData.set(2, message.getTimeRemaining());
		matchStateUpdateData.set(3, message.getGreenTeamScore());
		matchStateUpdateData.set(4, message.getPurpleTeamScore());
		
		SocketArray encoded = createSocketArray();
		encoded.set(0, Message.MATCH_UPDATE_OR_FINISH);
		encoded.set(10, matchStateUpdateData);
		
		return encoded;
	}
	
	private SocketArray encodeMatchFinishedMessage(MatchFinishedMessage message) {
		SocketArray matchFinishedData = createSocketArray();
		matchFinishedData.set(0, 1);
		matchFinishedData.set(6, true);
		if (message.getGreenTeamScore() == message.getPurpleTeamScore()) {
			matchFinishedData.set(7, 3);
		} else if (message.getGreenTeamScore() < message.getPurpleTeamScore()) {
			matchFinishedData.set(7, 2);
		} else {
			matchFinishedData.set(7, 1);
		}
		
		//TODO: Awards
		
		SocketArray encoded = createSocketArray();
		encoded.set(0, Message.MATCH_UPDATE_OR_FINISH);
		encoded.set(10, matchFinishedData);
		
		return encoded;
	}
	
	private SocketArray encodeEntityMoveMessage(EntityMoveMessage message) {
		SocketArray moveData = createSocketArray();
		moveData.set(0, message.getEntityId() + 1);
		moveData.set(1, message.getVelocity().getX());
		moveData.set(2, message.getVelocity().getY());
		moveData.set(3, message.isMoving());
		
		SocketArray encoded = createSocketArray();
		encoded.set(0, Message.ENTITY_MOVE);
		encoded.set(1, message.getEntityId() + 1);
		encoded.set(11, moveData);
		
		return encoded;
	}
	
	private SocketArray encodeEntityStateUpdateMessage(Message message) {
		if (message instanceof PlayerStateUpdateMessageImp) {
			return encodePlayerStateUpdateMessage((PlayerStateUpdateMessage) message);
		} else {
			return encodeSpiritStateUpdateMessage((SpiritStateUpdateMessage) message);
		}
	}
	
	private SocketArray encodePlayerStateUpdateMessage(PlayerStateUpdateMessage message) {
		SocketArray updateData = encodePlayerStateUpdate(message.getUpdate());
		
		SocketArray encoded = createSocketArray();
		encoded.set(0, Message.ENTITY_UPDATE);
		encoded.set(12, updateData);
		
		return encoded;
	}
	
	private SocketArray encodeSpiritStateUpdateMessage(SpiritStateUpdateMessage message) {
		SocketArray updateData = encodeSpiritStateUpdate(message.getUpdate());
		
		SocketArray encoded = createSocketArray();
		encoded.set(0, Message.ENTITY_UPDATE);
		encoded.set(12, updateData);
		
		return encoded;
	}

	private SocketArray encodeEntityTeleportMessage(EntityTeleportMessage message) {
		SocketArray teleportData = createSocketArray();
		teleportData.set(0, message.getEntityId() + 1);
		teleportData.set(1, message.getPosition().getX());
		teleportData.set(2, message.getPosition().getY());
		teleportData.set(3, message.getVelocity().getX());
		teleportData.set(4, message.getVelocity().getY());
		teleportData.set(5, message.isMoving());
		
		SocketArray encoded = createSocketArray();
		encoded.set(0, Message.ENTITY_TELEPORT);
		encoded.set(1, message.getEntityId() + 1);
		encoded.set(13, teleportData);
		
		return encoded;
	}

	private SocketArray encodeClearSessionDataMessage() {
		SocketArray array = createSocketArray();
		array.set(0, Message.CLEAR_SESSION_DATA);
		
		return array;
	}
	
	private SocketArray encodePlayerStateUpdate(PlayerStateUpdate stateUpdate) {
		SocketArray emotionData = createSocketArray();
		emotionData.set(0, stateUpdate.getEmotion().ordinal());
		
		SocketArray upgradeData = createSocketArray();
		upgradeData.set(0, stateUpdate.getUpgradeData().ordinal());
		upgradeData.set(1, stateUpdate.getUpgrade().ordinal());
		
		SocketArray animationData = createSocketArray();
		animationData.set(0, stateUpdate.getAnimation().ordinal());
		animationData.set(1, stateUpdate.getAnimationAmount());
		
		SocketArray updateData = createSocketArray();
		updateData.set(1, stateUpdate.getSpeed());
		updateData.set(2, stateUpdate.getGlowRadius());
		updateData.set(5, stateUpdate.getNumSpiritsHeld());
		updateData.set(7, stateUpdate.isConnected());
		updateData.set(12, upgradeData);
		updateData.set(13, animationData);
		updateData.set(14, emotionData);
		
		if (stateUpdate.getUpgradeData() == UpgradeData.ACHIEVED) {
			updateData.set(3, stateUpdate.getUpgrade().ordinal());
		}
		
		SocketArray encoded = createSocketArray();
		encoded.set(0, stateUpdate.getEntityId() + 1);
		encoded.set(1, 1);
		encoded.set(2, updateData);
		
		return encoded;
	}
	
	private SocketArray encodeSpiritStateUpdate(SpiritStateUpdate stateUpdate) {
		SocketArray updateData = createSocketArray();
		updateData.set(0, stateUpdate.getTeam().ordinal() + 1);
		
		SocketArray encoded = createSocketArray();
		encoded.set(0, stateUpdate.getEntityId() + 1);
		encoded.set(1, 16);
		encoded.set(3, updateData);
		
		return encoded;
	}

	private SocketArray createSocketArray() {
		return new SocketArrayImp(decoder, encoder);
	}
	
}
