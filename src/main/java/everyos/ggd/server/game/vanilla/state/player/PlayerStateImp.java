package everyos.ggd.server.game.vanilla.state.player;

import java.util.ArrayList;
import java.util.List;

import everyos.ggd.server.game.vanilla.state.game.play.MessagingPhysicsBody;
import everyos.ggd.server.game.vanilla.state.spirit.SpiritState;
import everyos.ggd.server.message.PlayerStateUpdate;
import everyos.ggd.server.message.imp.PlayerStateUpdateBuilder;
import everyos.ggd.server.physics.PhysicsBody;

public class PlayerStateImp implements PlayerState {
	
	private final PlayerStats stats = new PlayerStatsImp();
	private final List<SpiritState> spiritList = new ArrayList<>();
	private final PhysicsBody physicsBody = new MessagingPhysicsBody();
	private final int entityId;
	
	private boolean needsUpdate = true;
	
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
	public PhysicsBody getPhysicsBody() {
		return physicsBody;
	}
	
	@Override
	public List<SpiritState> getSpiritList() {
		return this.spiritList;
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
