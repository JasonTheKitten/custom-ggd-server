package everyos.ggd.server.message;

import everyos.ggd.server.physics.Position;

public interface PlayerInitMessage extends Message {

	Position getInitialPosition();
	
	int getCharacterId();
	
	boolean isBot();
	
	PlayerStateUpdate getStateUpdate();
	
}
