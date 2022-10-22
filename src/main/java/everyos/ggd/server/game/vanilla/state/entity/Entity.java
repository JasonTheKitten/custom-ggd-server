package everyos.ggd.server.game.vanilla.state.entity;

import java.util.List;

import everyos.ggd.server.message.Message;
import everyos.ggd.server.physics.PhysicsBody;

public interface Entity {

	int getEntityId();
	
	List<Message> getQueuedMessages();
	
	List<Message> createFinalMessages();
	
	PhysicsBody getPhysicsBody();
	
}
