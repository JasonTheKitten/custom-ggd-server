package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.PlayerInitMessage;
import everyos.ggd.server.message.PlayerStateUpdate;
import everyos.ggd.server.physics.Position;

public class PlayerInitMessageImp implements PlayerInitMessage {

	private final int characterId;
	private final Position position;
	private final boolean isBot;
	private final PlayerStateUpdate state;

	public PlayerInitMessageImp(Position position, boolean isBot, PlayerStateUpdate state) {
		this.characterId = state.getEntityId();
		this.position = position;
		this.isBot = isBot;
		this.state = state;
	}
	
	@Override
	public int getType() {
		return Message.ENTITY_INIT;
	}
	
	@Override
	public int getCharacterId() {
		return this.characterId;
	}
	
	@Override
	public Position getInitialPosition() {
		return this.position;
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
