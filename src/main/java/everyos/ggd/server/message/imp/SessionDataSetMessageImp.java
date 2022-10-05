package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.SessionDataSetMessage;

public class SessionDataSetMessageImp implements SessionDataSetMessage {

	private final int entityId;
	private final int matchId;
	private final String serverName;

	public SessionDataSetMessageImp(int entityId, int matchId, String serverName) {
		this.entityId = entityId;
		this.matchId = matchId;
		this.serverName = serverName;
	}
	
	@Override
	public int getType() {
		return Message.SESSION_DATA_SET;
	}
	
	@Override
	public int getEntityId() {
		return this.entityId;
	}

	@Override
	public int getMatchId() {
		return this.matchId;
	}

	@Override
	public String getServerName() {
		return this.serverName;
	}

}
