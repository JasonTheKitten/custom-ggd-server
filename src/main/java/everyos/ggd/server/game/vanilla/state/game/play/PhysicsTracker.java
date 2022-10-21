package everyos.ggd.server.game.vanilla.state.game.play;

import everyos.ggd.server.physics.PhysicsBody;
import everyos.ggd.server.physics.Position;
import everyos.ggd.server.physics.Velocity;
import everyos.ggd.server.physics.imp.PositionImp;
import everyos.ggd.server.server.GGDServer;

public class PhysicsTracker {

	private PhysicsBodiesView physicsBodies;

	public PhysicsTracker(PhysicsBodiesView physicsBodies) {
		this.physicsBodies = physicsBodies;
	}
	
	public void tick() {
		updatePhysicsPositions();
	}
	
	private void updatePhysicsPositions() {
		for (int i = 0; i < physicsBodies.getLength(); i++) {
			updatePhysicsPosition(physicsBodies.get(i));
		}
	}
	
	private void updatePhysicsPosition(PhysicsBody physicsBody) {
		Position initialPosition = physicsBody.getCurrentPosition();
		Velocity velocity = physicsBody.getCurrentVelocity();
		Position newPosition = new PositionImp(
			initialPosition.getX() + velocity.getX()/GGDServer.FRAME_RATE,	
			initialPosition.getY() + velocity.getY()/GGDServer.FRAME_RATE);
		physicsBody.setCurrentPosition(newPosition);
	}
	
	public static interface PhysicsBodiesView {
		
		int getLength();
		
		PhysicsBody get(int index);
		
	}
	
}
