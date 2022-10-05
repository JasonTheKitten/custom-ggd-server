package everyos.ggd.server.message;

public interface EntityStateUpdate {
	
	int getEntityId();
	
	EntityType getEntityType();
	
	public static enum EntityType {
		PLAYER, SPIRIT, MEGA_FLAME
	}

}
