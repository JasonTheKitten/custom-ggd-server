package everyos.ggd.server.bot;

import everyos.ggd.server.physics.Location;
import everyos.ggd.server.physics.imp.LocationImp;

public enum Direction {
	NORTH(new LocationImp(0, -1)),
    EAST(new LocationImp(1, 0)),
    SOUTH(new LocationImp(0, 1)),
    WEST(new LocationImp(-1, 0)),
    NONE(new LocationImp(0, 0));
	
	public static final Direction[] DIRECTIONS = {
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
