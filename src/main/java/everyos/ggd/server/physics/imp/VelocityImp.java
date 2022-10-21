package everyos.ggd.server.physics.imp;

import java.util.Objects;

import everyos.ggd.server.physics.Velocity;

public class VelocityImp implements Velocity {
	
	private final float x;
	private final float y;

	public VelocityImp(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public float getX() {
		return this.x;
	}

	@Override
	public float getY() {
		return this.y;
	}
	
	@Override
	public boolean equals(Object o) {
		return
			o instanceof Velocity &&
			this.x == ((Velocity) o).getX() &&
			this.y == ((Velocity) o).getY();
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
	
}
