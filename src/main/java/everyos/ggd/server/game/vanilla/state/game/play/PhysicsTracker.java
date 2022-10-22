package everyos.ggd.server.game.vanilla.state.game.play;

import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.game.vanilla.state.entity.Entity;
import everyos.ggd.server.physics.PhysicsBody;
import everyos.ggd.server.physics.Position;
import everyos.ggd.server.physics.Velocity;
import everyos.ggd.server.physics.imp.PositionImp;

public class PhysicsTracker {

	private final MatchContext matchContext;
	
	private long lastPoll = System.currentTimeMillis();

	public PhysicsTracker(MatchContext matchContext) {
		this.matchContext = matchContext;
	}
	
	public void tick() {
		updatePhysicsPositions();
	}
	
	private void updatePhysicsPositions() {
		int timeElapsed = (int) (System.currentTimeMillis() - lastPoll);
		lastPoll = System.currentTimeMillis();
		
		for (Entity entity: matchContext.getEntityRegister().getAllEntities()) {
			updatePhysicsPosition(entity.getPhysicsBody(), timeElapsed);
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
	
}
