package everyos.ggd.server.physics.util;

import everyos.ggd.server.physics.Position;
import everyos.ggd.server.physics.Velocity;
import everyos.ggd.server.physics.imp.VelocityImp;

public final class PhysicsUtil {
	
	private PhysicsUtil() {}

	public static Velocity calculateVelocityAngle(Position currentPosition, Position targetPosition, float totalVelocity) {
		float xDistance = targetPosition.getX() - currentPosition.getX();
		float yDistance = targetPosition.getY() - currentPosition.getY();
		float angle = (float) Math.atan(Math.abs(yDistance/xDistance));
		float xComponent = (float) (Math.signum(xDistance) * Math.abs(Math.cos(angle) * totalVelocity));
		float yComponent = (float) (Math.signum(yDistance) * Math.abs(Math.sin(angle) * totalVelocity));
		
		return new VelocityImp(xComponent, yComponent);
	}

}
