package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.EntityTeleportMessage;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.physics.Position;
import everyos.ggd.server.physics.Velocity;

public class EntityTeleportMessageImp implements EntityTeleportMessage{

	private final int entityId;
	private final Position position;
	private final Velocity velocity;
	private final boolean isMoving;

	public EntityTeleportMessageImp(int entityId, Position position, Velocity velocity, boolean isMoving) {
		this.entityId = entityId;
		this.position = position;
		this.velocity = velocity;
		this.isMoving = isMoving;
	}
	
	@Override
	public int getType() {
		return Message.ENTITY_TELEPORT;
	}
	
	@Override
	public int getEntityId() {
		return this.entityId;
	}

	@Override
	public Position getPosition() {
		return this.position;
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
