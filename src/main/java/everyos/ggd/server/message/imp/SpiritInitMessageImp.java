package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.SpiritInitMessage;
import everyos.ggd.server.message.SpiritStateUpdate;
import everyos.ggd.server.physics.Position;

public class SpiritInitMessageImp implements SpiritInitMessage {

	private final int entityId;
	private final Position position;
	private SpiritStateUpdate stateUpdate;
	
	public SpiritInitMessageImp(int entityId, Position position, SpiritStateUpdate stateUpdate) {
		this.entityId = entityId;
		this.position = position;
		this.stateUpdate = stateUpdate;
	}

	@Override
	public int getType() {
		return Message.ENTITY_INIT;
	}
	
	@Override
	public int getEntityId() {
		return this.entityId;
	}
	
	@Override
	public Position getInitialPosition() {
		return this.position;
	}
	
	@Override
	public SpiritStateUpdate getStateUpdate() {
		return this.stateUpdate;
	}

}
