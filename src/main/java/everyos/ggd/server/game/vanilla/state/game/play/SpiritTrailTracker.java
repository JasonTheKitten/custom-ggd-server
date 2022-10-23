package everyos.ggd.server.game.vanilla.state.game.play;

import java.util.List;

import everyos.ggd.server.game.vanilla.state.player.PlayerState;
import everyos.ggd.server.game.vanilla.state.spirit.SpiritState;
import everyos.ggd.server.game.vanilla.util.MathUtil;
import everyos.ggd.server.physics.PhysicsBody;
import everyos.ggd.server.physics.Position;
import everyos.ggd.server.physics.Velocity;
import everyos.ggd.server.physics.imp.PositionImp;
import everyos.ggd.server.physics.util.PhysicsUtil;

public class SpiritTrailTracker {
	
	public void handlePlayerPositionUpdate(PlayerState playerState, Position playerPosition) {
		List<SpiritState> spiritTrail = playerState.getSpiritList();
		if (spiritTrail.isEmpty()) {
			return;
		}
		
		PhysicsBody playerPhysicsBody = playerState.getPhysicsBody();
		{
			PhysicsBody spiritPhysicsBody = spiritTrail.get(0).getPhysicsBody();
			Position nextSpiritPosition = playerState.getPhysicsBody().getCurrentPosition();
			spiritPhysicsBody.setCurrentPosition(
				calculateNewTrailSpiritPosition(spiritPhysicsBody.getCurrentPosition(), nextSpiritPosition));
			spiritPhysicsBody.setCurrentVelocity(
				calculateNewTrailSpiritVelocity(
					spiritPhysicsBody.getCurrentPosition(),
					nextSpiritPosition,
					playerPhysicsBody.getCurrentVelocity()));
		}
		for (int i = 1; i < spiritTrail.size(); i++) {
			PhysicsBody spiritPhysicsBody = spiritTrail.get(i).getPhysicsBody();
			Position nextSpiritPosition = spiritTrail.get(i-1).getPhysicsBody().getCurrentPosition();
			spiritPhysicsBody.setCurrentPosition(
					calculateNewTrailSpiritPosition(spiritPhysicsBody.getCurrentPosition(), nextSpiritPosition));
		}
	}

	private Position calculateNewTrailSpiritPosition(Position currentPosition, Position nextSpiritPosition) {
		float totalDistance = MathUtil.getDistanceBetweenPositions(currentPosition, nextSpiritPosition);
		float xDistance = currentPosition.getX() - nextSpiritPosition.getX();
		float yDistance = currentPosition.getY() - nextSpiritPosition.getY();
		float xNewDistance = (xDistance / totalDistance) * 2;
		float yNewDistance = (yDistance / totalDistance) * 2;
		float xComponent = nextSpiritPosition.getX() + xNewDistance;
		float yComponent = nextSpiritPosition.getY() + yNewDistance;
		
		return new PositionImp(xComponent, yComponent);
	}
	
	private Velocity calculateNewTrailSpiritVelocity(Position currentPosition, Position nextSpiritPosition, Velocity playerVelocity) {
		float totalVelocity = (float) Math.sqrt(
			playerVelocity.getX() * playerVelocity.getX() +
			playerVelocity.getY() * playerVelocity.getY());
		return PhysicsUtil.calculateVelocityAngle(currentPosition, nextSpiritPosition, totalVelocity);
	}
	
}
