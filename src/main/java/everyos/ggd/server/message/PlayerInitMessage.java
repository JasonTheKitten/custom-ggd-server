package everyos.ggd.server.message;

import everyos.ggd.server.common.Position;

public interface PlayerInitMessage extends Message {

	Position getInitialPosition();
	
	int getCharacterId();
	
	boolean isBot();
	
	PlayerStateUpdate getStateUpdate();
	
}
