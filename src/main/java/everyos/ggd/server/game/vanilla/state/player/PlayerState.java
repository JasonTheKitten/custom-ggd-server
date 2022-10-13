package everyos.ggd.server.game.vanilla.state.player;

import java.util.List;

import everyos.ggd.server.game.vanilla.state.spirit.SpiritState;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.physics.PhysicsBody;

public interface PlayerState {
	
	int getEntityId();

	PhysicsBody getPhysicsBody();
	
	PlayerStats getStats();
	
	List<SpiritState> getSpiritList();
	
	void gain(int amount, SpiritGainReason reason);
	
	Message createInitMessage(boolean isBot);
	
	List<Message> getQueuedMessages();
	
	public static enum SpiritGainReason {
		COLLECT_SPIRIT, BUDDY_BONUS, MEGA_FLAME, STEAL_SPIRIT, GOAL_RETURN
	}
	
}
