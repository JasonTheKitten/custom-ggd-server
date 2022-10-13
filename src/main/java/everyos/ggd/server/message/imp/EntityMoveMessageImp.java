package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.EntityMoveMessage;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.physics.Velocity;

public class EntityMoveMessageImp implements EntityMoveMessage{

	private final int entityId;
	private final Velocity velocity;
	private final boolean isMoving;

	public EntityMoveMessageImp(int entityId, Velocity velocity, boolean isMoving) {
		this.entityId = entityId;
		this.velocity = velocity;
		this.isMoving = isMoving;
	}
	
	@Override
	public int getType() {
		return Message.ENTITY_MOVE;
	}
	
	@Override
	public int getEntityId() {
		return this.entityId;
	}

	@Override
	public Velocity getVelocity() {
		return this.velocity;
	}

	@Override
	public boolean isMoving() {
		return this.isMoving;
	}

}
