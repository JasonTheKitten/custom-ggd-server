package everyos.ggd.server.message;

import everyos.ggd.server.physics.Position;
import everyos.ggd.server.physics.Velocity;

public interface EntityTeleportMessage extends Message {

	int getEntityId();
	
	Position getPosition();
	
	Velocity getVelocity();
	
	boolean isMoving();
	
}
