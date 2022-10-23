package everyos.ggd.server.physics.util;

import everyos.ggd.server.bot.Direction;
import everyos.ggd.server.physics.Location;
import everyos.ggd.server.physics.imp.LocationImp;

public final class LocationUtil {

	private LocationUtil() {}
	
	public static Location addDirection(Location location, Direction direction) {
		int xComponent = location.getX() + direction.getOffset().getX();
		int yComponent = location.getY() + direction.getOffset().getY();
		return new LocationImp(xComponent, yComponent);
	}
	
}
