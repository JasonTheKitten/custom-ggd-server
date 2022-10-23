package everyos.ggd.server.bot;

import everyos.ggd.server.physics.Location;
import everyos.ggd.server.physics.imp.LocationImp;

public enum Direction {
	NORTH_EAST(new LocationImp(1, -1)),
	NORTH_WEST(new LocationImp(-1, -1)),
	SOUTH_EAST(new LocationImp(1, 1)),
	SOUTH_WEST(new LocationImp(-1, 1)),
	NORTH(new LocationImp(0, -1)),
    EAST(new LocationImp(1, 0)),
    SOUTH(new LocationImp(0, 1)),
    WEST(new LocationImp(-1, 0)),
    NONE(new LocationImp(0, 0));
	
	public static final Direction[] DIRECTIONS = {
		Direction.NORTH_EAST,
		Direction.NORTH_WEST,
		Direction.SOUTH_EAST,
		Direction.SOUTH_WEST,
		Direction.NORTH,
		Direction.EAST,
		Direction.SOUTH,
		Direction.WEST
	};
    
    private final Location offset;
	
	private Direction(Location offset) {
		this.offset = offset;
	}
	
	public Location getOffset() {
		return this.offset;
	}
	
}
