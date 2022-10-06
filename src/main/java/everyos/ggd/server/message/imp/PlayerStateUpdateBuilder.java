package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.PlayerStateUpdate;

public class PlayerStateUpdateBuilder {
	
	private int entityId;
	private float speed;
	private boolean connected;

	public PlayerStateUpdateBuilder() {}
	
	public PlayerStateUpdateBuilder setEntityId(int entityId) {
		this.entityId = entityId;
		
		return this;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public PlayerStateUpdateBuilder setSpeed(float speed) {
		this.speed = speed;
		
		return this;
	}
	
	public float getSpeed() {
		return this.speed;
	}
	
	public PlayerStateUpdateBuilder setConnected(boolean connected) {
		this.connected = connected;
		
		return this;
	}
	
	public boolean getConnected() {
		return this.connected;
	}
	
	public PlayerStateUpdate build() {
		return new PlayerStateUpdateImp(this);
	}
	
	public static PlayerStateUpdateBuilder clone(PlayerStateUpdate stateUpdate) {
		PlayerStateUpdateBuilder builder = new PlayerStateUpdateBuilder();
		
		builder.setEntityId(stateUpdate.getEntityId());
		builder.setSpeed(stateUpdate.getSpeed());
		builder.setConnected(stateUpdate.isConnected());
		
		return builder;
	}
	
}
