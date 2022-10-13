package everyos.ggd.server.message;

import everyos.ggd.server.physics.Position;

public interface SpiritInitMessage extends Message {

	int getEntityId();

	Position getInitialPosition();

	SpiritStateUpdate getStateUpdate();
	
}
