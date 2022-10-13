package everyos.ggd.server.game.vanilla.state.player;

import everyos.ggd.server.message.PlayerStateUpdate;
import everyos.ggd.server.physics.Position;
import everyos.ggd.server.physics.Velocity;

public interface PlayerState {
	
	int getEntityId();

	PlayerStats getStats();
	
	void setPosition(Position position);
	
	Position getPosition();
	
	void setVelocity(Velocity velocity);
	
	Velocity getVelocity();
	
	void gain(SpiritGainReason reason);
	
	boolean needsUpdate();
	
	PlayerStateUpdate createUpdateInfo();
	
	public static enum SpiritGainReason {
		COLLECT_SPIRIT, BUDDY_BONUS, MEGA_FLAME, STEAL_SPIRIT
	}
	
}
