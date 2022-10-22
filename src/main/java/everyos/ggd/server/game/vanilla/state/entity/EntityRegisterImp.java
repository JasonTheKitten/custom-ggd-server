package everyos.ggd.server.game.vanilla.state.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import everyos.ggd.server.message.Message;

public class EntityRegisterImp implements EntityRegister {
	
	private final List<Entity> entities = new ArrayList<>();
	private final List<Message> queuedMessages = new ArrayList<>();
	
	private int nextEntityId = 0;

	@Override
	public <T extends Entity> T createEntity(Function<Integer, T> creator) {
		int entityId = nextEntityId++;
		T entity = creator.apply(entityId);
		entities.add(entity);
		
		return entity;
	}
	

	@Override
	public List<Entity> getAllEntities() {
		return entities;
	}

	@Override
	public void removeEntity(Entity entity) {
		entities.remove(entity);
		queuedMessages.addAll(entity.createFinalMessages());
	}

	@Override
	public List<Message> getQueuedMessages() {
		List<Message> messages = new ArrayList<>();
		
		addInternalQueuedMessages(messages);
		addExternalQueuedMessages(messages);
		
		return messages;
	}

	private void addInternalQueuedMessages(List<Message> messages) {
		messages.addAll(queuedMessages);
		queuedMessages.clear();
	}
	
	private void addExternalQueuedMessages(List<Message> messages) {
		for (Entity entity: entities) {
			messages.addAll(entity.getQueuedMessages());
		}
	}

}
