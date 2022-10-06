package everyos.ggd.server.message;

import everyos.ggd.server.physics.Position;

public interface EntityMoveMessage extends Message {

	int getEntityId();
	
	Position getRelativePosition();
	
	boolean isMoving();
	
}
