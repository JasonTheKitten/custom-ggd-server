package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.EntityMoveMessage;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.physics.Position;

public class EntityMoveMessageImp implements EntityMoveMessage{

	private final int entityId;
	private final Position relativePosition;
	private final boolean isMoving;

	public EntityMoveMessageImp(int entityId, Position relativePosition, boolean isMoving) {
		this.entityId = entityId;
		this.relativePosition = relativePosition;
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
	public Position getRelativePosition() {
		return this.relativePosition;
	}

	@Override
	public boolean isMoving() {
		return this.isMoving;
	}

}
