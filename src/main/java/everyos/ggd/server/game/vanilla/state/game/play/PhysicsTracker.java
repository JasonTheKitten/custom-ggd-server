package everyos.ggd.server.game.vanilla.state.game.play;

import everyos.ggd.server.physics.PhysicsBody;
import everyos.ggd.server.physics.Position;
import everyos.ggd.server.physics.Velocity;
import everyos.ggd.server.physics.imp.PositionImp;

public class PhysicsTracker {

	private final PhysicsBodiesView physicsBodies;
	
	private long lastPoll = System.currentTimeMillis();

	public PhysicsTracker(PhysicsBodiesView physicsBodies) {
		this.physicsBodies = physicsBodies;
	}
	
	public void tick() {
		updatePhysicsPositions();
	}
	
	private void updatePhysicsPositions() {
		int timeElapsed = (int) (System.currentTimeMillis() - lastPoll);
		lastPoll = System.currentTimeMillis();
		
		for (int i = 0; i < physicsBodies.getLength(); i++) {
			updatePhysicsPosition(physicsBodies.get(i), timeElapsed);
		}
	}
	
	private void updatePhysicsPosition(PhysicsBody physicsBody, int timeElapsed) {
		Position initialPosition = physicsBody.getCurrentPosition();
		Velocity velocity = physicsBody.getCurrentVelocity();
		Position newPosition = new PositionImp(
			initialPosition.getX() + velocity.getX()/1000*timeElapsed,	
			initialPosition.getY() + velocity.getY()/1000*timeElapsed);
		physicsBody.setCurrentPosition(newPosition);
	}
	
	public static interface PhysicsBodiesView {
		
		int getLength();
		
		PhysicsBody get(int index);
		
	}
	
}
