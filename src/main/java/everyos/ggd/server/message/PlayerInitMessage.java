package everyos.ggd.server.message;

import everyos.ggd.server.physics.Position;

public interface PlayerInitMessage extends Message {

	int getCharacterId();
	
	Position getInitialPosition();
	
	boolean isBot();
	
	PlayerStateUpdate getStateUpdate();
	
}
