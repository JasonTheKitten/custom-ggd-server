package everyos.ggd.server.game.vanilla.state.player;

import everyos.ggd.server.message.PlayerStateUpdate;

public interface PlayerState {

	PlayerStats getStats();
	
	void gain(SpiritGainReason reason);
	
	boolean needsUpdate();
	
	PlayerStateUpdate createUpdateInfo();
	
	public static enum SpiritGainReason {
		COLLECT_SPIRIT, BUDDY_BONUS, MEGA_FLAME, STEAL_SPIRIT
	}
	
}
