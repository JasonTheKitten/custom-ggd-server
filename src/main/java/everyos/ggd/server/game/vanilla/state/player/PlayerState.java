package everyos.ggd.server.game.vanilla.state.player;

import java.util.List;

import everyos.ggd.server.game.vanilla.state.entity.Entity;
import everyos.ggd.server.game.vanilla.state.spirit.SpiritState;
import everyos.ggd.server.message.PlayerStateUpdate.Upgrade;

public interface PlayerState extends Entity {
	
	PlayerStats getStats();
	
	List<SpiritState> getSpiritList();
	
	Upgrade getUpgradeLevel();
	
	void setUpgradeLevel(Upgrade upgrade);
	
	void setUpgradeHint(Upgrade speedUpgrade);
	
	float getSpeed();
	
	void setSpeed(float speed);
	
	void setLight(int glowRadius);
	
	long getLastBuddyBonusTime();
	
	void setLastBuddyBonusTime(long time);
	
	void gain(int amount, SpiritGainReason reason);
	
	void loose(int amount);

	void setConnected(boolean connected);
	
	void indicateMatchFinished();
	
	public static enum SpiritGainReason {
		COLLECT_SPIRIT, BUDDY_BONUS, MEGA_FLAME, STEAL_SPIRIT, GOAL_RETURN
	}
	
}
