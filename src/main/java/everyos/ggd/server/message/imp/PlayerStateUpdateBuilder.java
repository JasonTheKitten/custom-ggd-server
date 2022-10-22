package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.PlayerStateUpdate;
import everyos.ggd.server.message.PlayerStateUpdate.Animation;
import everyos.ggd.server.message.PlayerStateUpdate.Emotion;
import everyos.ggd.server.message.PlayerStateUpdate.Upgrade;
import everyos.ggd.server.message.PlayerStateUpdate.UpgradeAnimation;

public class PlayerStateUpdateBuilder {
	
	private int entityId;
	private float speed;
	private int glowRadius;
	private Upgrade upgrade;
	private int totalSpiritsCollected;
	private boolean connected;
	private Animation animation;
	private int animationAmount;
	private Emotion emotion;
	private UpgradeAnimation upgradeAnimation;
	private Upgrade upgradeAnimationUpgrade;

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
	
	public PlayerStateUpdateBuilder setGlowRadius(int glowRadius) {
		this.glowRadius = glowRadius;
		
		return this;
	}
	
	public int getGlowRadius() {
		return this.glowRadius;
	}
	
	public PlayerStateUpdateBuilder setUpgrade(Upgrade upgrade) {
		this.upgrade = upgrade;
		
		return this;
	}
	
	public Upgrade getUpgrade() {
		return this.upgrade;
	}
	
	public PlayerStateUpdateBuilder setTotalSpiritsCollected(int spiritsCollected) {
		this.totalSpiritsCollected = spiritsCollected;
		
		return this;
	}
	
	public int getTotalSpiritsCollected() {
		return this.totalSpiritsCollected;
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
	
	public PlayerStateUpdateBuilder setUpgradeAnimation(UpgradeAnimation upgradeData, Upgrade upgrade) {
		this.upgradeAnimation = upgradeData;
		this.upgradeAnimationUpgrade = upgrade;
		
		return this;
	}
	
	public UpgradeAnimation getUpgradeAnimation() {
		return this.upgradeAnimation;
	}
	
	public Upgrade getUpgradeAnimationUpgrade() {
		return this.upgradeAnimationUpgrade;
	}
	
	public PlayerStateUpdate build() {
		return new PlayerStateUpdateImp(this);
	}
	
	public static PlayerStateUpdateBuilder clone(PlayerStateUpdate stateUpdate) {
		PlayerStateUpdateBuilder builder = new PlayerStateUpdateBuilder();
		
		builder.setEntityId(stateUpdate.getEntityId());
		builder.setSpeed(stateUpdate.getSpeed());
		builder.setGlowRadius(stateUpdate.getGlowRadius());
		builder.setUpgrade(stateUpdate.getUpgrade());
		builder.setTotalSpiritsCollected(stateUpdate.getTotalSpiritsCollected());
		builder.setConnected(stateUpdate.isConnected());
		builder.setAnimation(stateUpdate.getAnimation(), stateUpdate.getAnimationAmount());
		builder.setEmotion(stateUpdate.getEmotion());
		builder.setUpgradeAnimation(stateUpdate.getUpgradeAnimation(), stateUpdate.getUpgradeAnimationUpgrade());
		
		return builder;
	}
	
}
