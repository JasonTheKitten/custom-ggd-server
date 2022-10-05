package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.PlayerStateUpdate;

public class PlayerStateUpdateBuilder {
	
	private int entityId;

	public PlayerStateUpdateBuilder() {}
	
	public PlayerStateUpdateBuilder setEntityId(int entityId) {
		this.entityId = entityId;
		
		return this;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public PlayerStateUpdate build() {
		return new PlayerStateUpdateImp(this);
	}
	
	public static PlayerStateUpdateBuilder clone(PlayerStateUpdate stateUpdate) {
		PlayerStateUpdateBuilder builder = new PlayerStateUpdateBuilder();
		
		builder.setEntityId(stateUpdate.getEntityId());
		
		return builder;
	}
	
}
