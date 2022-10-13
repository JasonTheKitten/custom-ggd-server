package everyos.ggd.server.message;

import everyos.ggd.server.physics.Velocity;

public interface EntityMoveMessage extends Message {

	int getEntityId();
	
	Velocity getVelocity();
	
	boolean isMoving();
	
}
