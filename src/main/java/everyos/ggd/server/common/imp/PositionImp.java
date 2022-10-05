package everyos.ggd.server.common.imp;

import everyos.ggd.server.common.Position;

public class PositionImp implements Position {
	
	private final float x;
	private final float y;

	public PositionImp(float x, float y) {
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
