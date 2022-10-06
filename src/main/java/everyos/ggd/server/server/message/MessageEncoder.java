package everyos.ggd.server.server.message;

import everyos.ggd.server.message.CountdownMessage;
import everyos.ggd.server.message.MatchFinishedMessage;
import everyos.ggd.server.message.MatchInitMessage;
import everyos.ggd.server.message.MatchStateUpdateMessage;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.PlayerInitMessage;
import everyos.ggd.server.message.PlayerStateUpdate;
import everyos.ggd.server.message.ServerConnectMessage;
import everyos.ggd.server.message.SessionDataSetMessage;
import everyos.ggd.server.message.imp.MatchStateUpdateMessageImp;
import everyos.ggd.server.socket.SocketArray;
import everyos.ggd.server.socket.decoder.SocketDecoder;
import everyos.ggd.server.socket.decoder.imp.SocketDecoderImp;
import everyos.ggd.server.socket.encoder.SocketEncoder;
import everyos.ggd.server.socket.encoder.imp.SocketEncoderImp;
import everyos.ggd.server.socket.imp.SocketArrayImp;

public final class MessageEncoder {
	
	//TODO: Dependency injection
	private static final SocketEncoder encoder = new SocketEncoderImp();
	private static final SocketDecoder decoder = new SocketDecoderImp();

	private MessageEncoder() {}

	public static SocketArray encode(Message message) {
		switch (message.getType()) {
		case Message.SERVER_CONNECT:
			return encodeServerConnectMessage((ServerConnectMessage) message);
		case Message.SESSION_DATA_SET:
			return encodeSessionDataSetMessage((SessionDataSetMessage) message);
		case Message.MATCH_INIT:
			return encodeMatchInitMessage((MatchInitMessage) message);
		case Message.COUNTDOWN_TIMER:
			return encodeCountdownMessage((CountdownMessage) message);
		case Message.PLAYER_INIT:
			return encodePlayerInitMessage((PlayerInitMessage) message);
		case Message.MATCH_UPDATE_OR_FINISH:
			return encodeMatchStateUpdateOrFinishMessage(message);
		case Message.CLEAR_SESSION_DATA:
			return encodeClearSessionDataMessage();
		case Message.INTERNAL:
			throw new RuntimeException("Message type is internal");
		default:
			throw new RuntimeException("No such message type [" + message.getType() + "]");
		}
	}

	private static SocketArray encodeServerConnectMessage(ServerConnectMessage message) {
		SocketArray matchNameData = createSocketArray();
		matchNameData.set(0, 4);
		matchNameData.set(1, message.getMatchName());
		
		SocketArray encoded = createSocketArray();
		encoded.set(0, Message.SERVER_CONNECT);
		encoded.set(1, message.getURL());
		encoded.set(3, matchNameData);
		
		return encoded;
	}
	
	private static SocketArray encodeSessionDataSetMessage(SessionDataSetMessage message) {
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
	
	private static SocketArray encodeMatchInitMessage(MatchInitMessage message) {
		SocketArray matchInitData = createSocketArray();
		matchInitData.set(2, message.getMapName());
		
		SocketArray encoded = createSocketArray();
		encoded.set(0, Message.MATCH_INIT);
		encoded.set(6, matchInitData);
		
		return encoded;
	}
	
	private static SocketArray encodeCountdownMessage(CountdownMessage message) {
		SocketArray timerInfo = createSocketArray();
		timerInfo.set(0, message.getSecondsLeft());
		
		SocketArray encoded = createSocketArray();
		encoded.set(0, Message.COUNTDOWN_TIMER);
		encoded.set(7, timerInfo);
		
		return encoded;
	}
	
	private static SocketArray encodePlayerInitMessage(PlayerInitMessage message) {
		PlayerStateUpdate state = message.getStateUpdate();
		
		SocketArray characterData = createSocketArray();
		characterData.set(1, message.getCharacterId() < 4 ? 1 : 2);
		characterData.set(2, message.getCharacterId() + 1);
		characterData.set(3, message.isBot());
		
		SocketArray initData = createSocketArray();
		initData.set(0, state.getEntityId() + 1);
		initData.set(1, 1);
		initData.set(5, characterData);
		
		SocketArray initStruct = createSocketArray();
		initStruct.set(0, initData);
		initStruct.set(1, encodePlayerStateUpdate(message.getStateUpdate()));
		
		SocketArray encoded = createSocketArray();
		encoded.set(0, Message.PLAYER_INIT);
		encoded.set(8, initStruct);
		
		return encoded;
	}
	
	private static SocketArray encodeMatchStateUpdateOrFinishMessage(Message message) {
		if (message instanceof MatchStateUpdateMessageImp) {
			return encodeMatchStateUpdateMessage((MatchStateUpdateMessage) message);
		} else {
			return encodeMatchFinishedMessage((MatchFinishedMessage) message);
		}
	}

	private static SocketArray encodeMatchStateUpdateMessage(MatchStateUpdateMessage message) {
		SocketArray matchStateUpdateData = createSocketArray();
		matchStateUpdateData.set(0, 1);
		matchStateUpdateData.set(5, true);
		matchStateUpdateData.set(2, message.getTimeRemaining());
		matchStateUpdateData.set(3, message.getGreenTeamScore());
		matchStateUpdateData.set(4, message.getPurpleTeamScore());
		
		SocketArray array = createSocketArray();
		array.set(0, Message.MATCH_UPDATE_OR_FINISH);
		array.set(10, matchStateUpdateData);
		
		return array;
	}
	
	private static SocketArray encodeMatchFinishedMessage(MatchFinishedMessage message) {
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
		
		SocketArray array = createSocketArray();
		array.set(0, Message.MATCH_UPDATE_OR_FINISH);
		array.set(10, matchFinishedData);
		
		return array;
	}

	private static SocketArray encodeClearSessionDataMessage() {
		SocketArray array = createSocketArray();
		array.set(0, Message.CLEAR_SESSION_DATA);
		
		return array;
	}
	
	private static SocketArray encodePlayerStateUpdate(PlayerStateUpdate stateUpdate) {
		SocketArray updateData = createSocketArray();
		updateData.set(1, stateUpdate.getSpeed());
		updateData.set(7, stateUpdate.isConnected());
		
		SocketArray encoded = createSocketArray();
		encoded.set(0, stateUpdate.getEntityId() + 1);
		encoded.set(1, 1);
		encoded.set(2, updateData);
		
		return encoded;
	}

	private static SocketArray createSocketArray() {
		return new SocketArrayImp(decoder, encoder);
	}
	
}
