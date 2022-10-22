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
	
	void setSpeed(float speed);
	
	void setLight(int glowRadius);
	
	void gain(int amount, SpiritGainReason reason);
	
	void loose(int amount);
	
	void indicateMatchFinished();
	
	public static enum SpiritGainReason {
		COLLECT_SPIRIT, BUDDY_BONUS, MEGA_FLAME, STEAL_SPIRIT, GOAL_RETURN
	}
	
}
