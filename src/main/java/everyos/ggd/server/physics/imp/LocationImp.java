package everyos.ggd.server.physics.imp;

import everyos.ggd.server.physics.Location;

public class LocationImp implements Location {
	
	private final int x;
	private final int y;

	public LocationImp(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}
	
	//TODO: Implement equals and hashCode

}
