package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.PlayerStateUpdate;
import everyos.ggd.server.message.PlayerStateUpdateMessage;

public class PlayerStateUpdateMessageImp implements PlayerStateUpdateMessage {

	private final PlayerStateUpdate stateUpdate;

	public PlayerStateUpdateMessageImp(PlayerStateUpdate stateUpdate) {
		this.stateUpdate = stateUpdate;
	}
	
	@Override
	public int getType() {
		return Message.ENTITY_UPDATE;
	}
	
	@Override
	public PlayerStateUpdate getUpdate() {
		return this.stateUpdate;
	}

}
