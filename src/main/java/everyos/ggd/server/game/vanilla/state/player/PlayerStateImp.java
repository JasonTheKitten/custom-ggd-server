package everyos.ggd.server.game.vanilla.state.player;

import everyos.ggd.server.message.PlayerStateUpdate;
import everyos.ggd.server.message.imp.PlayerStateUpdateBuilder;
import everyos.ggd.server.physics.Position;
import everyos.ggd.server.physics.Velocity;
import everyos.ggd.server.physics.imp.PositionImp;
import everyos.ggd.server.physics.imp.VelocityImp;

public class PlayerStateImp implements PlayerState {
	
	private final PlayerStats stats = new PlayerStatsImp();
	private final int entityId;
	
	private boolean needsUpdate = true;
	private Position position = new PositionImp(0, 0);
	private Velocity velocity = new VelocityImp(0, 0);
	
	public PlayerStateImp(int entityId) {
		this.entityId = entityId;
	}
	
	@Override
	public int getEntityId() {
		return this.entityId;
	}

	@Override
	public PlayerStats getStats() {
		return this.stats;
	}
	
	@Override
	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public Position getPosition() {
		return this.position;
	}

	@Override
	public void setVelocity(Velocity velocity) {
		this.velocity = velocity;
	}

	@Override
	public Velocity getVelocity() {
		return this.velocity;
	}

	@Override
	public void gain(SpiritGainReason reason) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean needsUpdate() {
		return needsUpdate;
	}

	@Override
	public PlayerStateUpdate createUpdateInfo() {
		needsUpdate = false;
		return new PlayerStateUpdateBuilder()
			.setEntityId(entityId)
			.setSpeed(15f)
			.setConnected(true)
			.build();
	}

}
