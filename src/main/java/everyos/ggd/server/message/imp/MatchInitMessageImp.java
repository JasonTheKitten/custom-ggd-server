package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.MatchInitMessage;
import everyos.ggd.server.message.Message;

public class MatchInitMessageImp implements MatchInitMessage {
	
	private final String mapName;

	public MatchInitMessageImp(String mapName) {
		this.mapName = mapName;
	}

	@Override
	public int getType() {
		return Message.MATCH_INIT;
	}

	@Override
	public String getMapName() {
		return mapName;
	}

}
