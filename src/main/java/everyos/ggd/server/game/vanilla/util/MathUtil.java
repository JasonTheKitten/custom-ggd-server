package everyos.ggd.server.game.vanilla.util;

import everyos.ggd.server.physics.Position;

public final class MathUtil {

	private MathUtil() {}
	
	public static float getDistanceBetweenPositions(Position position1, Position position2) {
		float xComponent = Math.abs(position1.getX() - position2.getX());
		float yComponent = Math.abs(position1.getY() - position2.getY());
		return (float) Math.sqrt(yComponent*yComponent + xComponent*xComponent);
	}
	
}
