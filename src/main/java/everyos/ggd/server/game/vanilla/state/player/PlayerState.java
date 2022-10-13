package everyos.ggd.server.game.vanilla.state.player;

import java.util.List;

import everyos.ggd.server.game.vanilla.state.spirit.SpiritState;
import everyos.ggd.server.message.PlayerStateUpdate;
import everyos.ggd.server.physics.PhysicsBody;

public interface PlayerState {
	
	int getEntityId();

	PhysicsBody getPhysicsBody();
	
	PlayerStats getStats();
	
	List<SpiritState> getSpiritList();
	
	void gain(SpiritGainReason reason);
	
	boolean needsUpdate();
	
	PlayerStateUpdate createUpdateInfo();
	
	public static enum SpiritGainReason {
		COLLECT_SPIRIT, BUDDY_BONUS, MEGA_FLAME, STEAL_SPIRIT
	}
	
}
