package everyos.ggd.server.game.vanilla.state.spirit;

import java.util.List;

import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.SpiritStateUpdate.SpiritTeam;
import everyos.ggd.server.physics.PhysicsBody;
import everyos.ggd.server.physics.Position;

public interface SpiritState {
	
	int getEntityId();

	int getOwnerEntityId();
	
	void setOwnerEntityId(int id);
	
	PhysicsBody getPhysicsBody();
	
	Position getInitialPosition();

	SpiritTeam getTeam();
	
	void setTeam(SpiritTeam team);

	List<Message> getQueuedMessages();

	void reset();
	
}
