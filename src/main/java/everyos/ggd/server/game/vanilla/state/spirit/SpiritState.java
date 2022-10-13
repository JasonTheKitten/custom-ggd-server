package everyos.ggd.server.game.vanilla.state.spirit;

import everyos.ggd.server.message.SpiritStateUpdate;
import everyos.ggd.server.message.SpiritStateUpdate.SpiritTeam;
import everyos.ggd.server.physics.Position;

public interface SpiritState {
	
	int getEntityId();

	int getOwnerEntityId();
	
	Position getInitialPosition();
	
	Position getCurrentPosition();

	SpiritTeam getTeam();
	
	void setTeam(SpiritTeam team);

	boolean needsUpdate();

	SpiritStateUpdate createUpdate();
	
}
