package everyos.ggd.server.physics.imp;

import java.util.Objects;

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
	
	@Override
	public boolean equals(Object o) {
		return
			o instanceof Location &&
			this.x == ((Location) o).getX() &&
			this.y == ((Location) o).getY();
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
	
}
