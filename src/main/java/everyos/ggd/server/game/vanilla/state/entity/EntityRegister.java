package everyos.ggd.server.game.vanilla.state.entity;

import java.util.List;
import java.util.function.Function;

import everyos.ggd.server.message.Message;

public interface EntityRegister {

	<T extends Entity> T createEntity(Function<Integer, T> creator);
	
	List<Entity> getAllEntities();
	
	void removeEntity(Entity entity);
	
	List<Message> getQueuedMessages();
	
}
