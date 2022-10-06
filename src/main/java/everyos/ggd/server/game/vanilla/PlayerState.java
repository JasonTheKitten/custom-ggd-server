package everyos.ggd.server.game.vanilla;

import everyos.ggd.server.message.PlayerStateUpdate;

public interface PlayerState {

	PlayerStats getStats();
	
	boolean isUpdated();
	
	PlayerStateUpdate createUpdateInfo();
	
	void gain(SpiritGainReason reason);
	
	public static enum SpiritGainReason {
		COLLECT_SPIRIT, BUDDY_BONUS, MEGA_FLAME, STEAL_SPIRIT
	}
	
}
