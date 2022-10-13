package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.PlayerStateUpdate;
import everyos.ggd.server.message.PlayerStateUpdate.Animation;
import everyos.ggd.server.message.PlayerStateUpdate.Emotion;

public class PlayerStateUpdateBuilder {
	
	private int entityId;
	private float speed;
	private int spiritsHeld;
	private boolean connected;
	private Animation animation;
	private int animationAmount;
	private Emotion emotion;

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
	
	public PlayerStateUpdateBuilder setNumSpiritsHeld(int spiritsHeld) {
		this.spiritsHeld = spiritsHeld;
		
		return this;
	}
	
	public int getNumSpiritsHeld() {
		return this.spiritsHeld;
	}
	
	public PlayerStateUpdateBuilder setConnected(boolean connected) {
		this.connected = connected;
		
		return this;
	}
	
	public boolean getConnected() {
		return this.connected;
	}
	
	public PlayerStateUpdateBuilder setAnimation(Animation animation, int amount) {
		this.animation = animation;
		this.animationAmount = amount;
		
		return this;
	}
	
	public Animation getAnimation() {
		return this.animation;
	}
	
	public int getAnimationAmount() {
		return this.animationAmount;
	}

	public PlayerStateUpdateBuilder setEmotion(Emotion emotion) {
		this.emotion = emotion;
		
		return this;
	}
	
	public Emotion getEmotion() {
		return this.emotion;
	}
	
	public PlayerStateUpdate build() {
		return new PlayerStateUpdateImp(this);
	}
	
	public static PlayerStateUpdateBuilder clone(PlayerStateUpdate stateUpdate) {
		PlayerStateUpdateBuilder builder = new PlayerStateUpdateBuilder();
		
		builder.setEntityId(stateUpdate.getEntityId());
		builder.setSpeed(stateUpdate.getSpeed());
		builder.setNumSpiritsHeld(stateUpdate.getNumSpiritsHeld());
		builder.setConnected(stateUpdate.isConnected());
		builder.setAnimation(stateUpdate.getAnimation(), stateUpdate.getAnimationAmount());
		builder.setEmotion(stateUpdate.getEmotion());
		
		return builder;
	}
	
}
