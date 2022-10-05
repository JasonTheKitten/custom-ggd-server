package everyos.ggd.server.message.imp;

import everyos.ggd.server.common.Position;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.PlayerInitMessage;
import everyos.ggd.server.message.PlayerStateUpdate;

public class PlayerInitMessageImp implements PlayerInitMessage {

	private final Position position;
	private final int characterId;
	private final boolean isBot;
	private final PlayerStateUpdate state;

	public PlayerInitMessageImp(Position position, int characterId, boolean isBot, PlayerStateUpdate state) {
		this.position = position;
		this.characterId = characterId;
		this.isBot = isBot;
		this.state = state;
	}
	
	@Override
	public int getType() {
		return Message.PLAYER_INIT;
	}
	
	@Override
	public Position getInitialPosition() {
		return this.position;
	}

	@Override
	public int getCharacterId() {
		return this.characterId;
	}

	@Override
	public boolean isBot() {
		return this.isBot;
	}

	@Override
	public PlayerStateUpdate getStateUpdate() {
		return this.state;
	}

}
