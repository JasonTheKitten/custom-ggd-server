package everyos.ggd.server.game.vanilla.state.spirit;

import everyos.ggd.server.game.vanilla.state.entity.Entity;
import everyos.ggd.server.message.SpiritStateUpdate.SpiritTeam;
import everyos.ggd.server.physics.PhysicsBody;

public interface SpiritState extends Entity {
	
	int getOwnerEntityId();
	
	void setOwnerEntityId(int id);
	
	long getCollectedTime();
	
	void setCollectedTime(long time);
	
	PhysicsBody getPhysicsBody();

	SpiritTeam getTeam();
	
	void setTeam(SpiritTeam team);

	void reset();
	
}
