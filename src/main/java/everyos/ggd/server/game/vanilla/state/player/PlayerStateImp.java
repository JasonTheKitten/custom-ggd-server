package everyos.ggd.server.game.vanilla.state.player;

import everyos.ggd.server.message.PlayerStateUpdate;
import everyos.ggd.server.message.imp.PlayerStateUpdateBuilder;

public class PlayerStateImp implements PlayerState {
	
	private final PlayerStats stats = new PlayerStatsImp();
	private final int entityId;
	
	private boolean needsUpdate = true;
	
	public PlayerStateImp(int entityId) {
		this.entityId = entityId;
	}

	@Override
	public PlayerStats getStats() {
		return this.stats;
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
