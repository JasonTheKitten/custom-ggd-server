package everyos.ggd.server.physics.imp;

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

}
