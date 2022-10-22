package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.PlayerStateUpdate;

public class PlayerStateUpdateImp implements PlayerStateUpdate {

	private final int entityId;
	private final float speed;
	private final int glowRadius;
	private final Upgrade upgrade;
	private final int spiritsHeld;
	private final boolean connected;
	private final Animation animation;
	private final int animationAmount;
	private final Emotion emotion;
	private final UpgradeAnimation upgradeAnimation;
	private final Upgrade upgradeAnimationUpgrade;

	public PlayerStateUpdateImp(PlayerStateUpdateBuilder builder) {
		this.entityId = builder.getEntityId();
		this.speed = builder.getSpeed();
		this.glowRadius = builder.getGlowRadius();
		this.upgrade = builder.getUpgrade();
		this.spiritsHeld = builder.getTotalSpiritsCollected();
		this.connected = builder.getConnected();
		this.animation = builder.getAnimation();
		this.animationAmount = builder.getAnimationAmount();
		this.emotion = builder.getEmotion();
		this.upgradeAnimation = builder.getUpgradeAnimation();
		this.upgradeAnimationUpgrade = builder.getUpgradeAnimationUpgrade();
	}
	
	@Override
	public int getEntityId() {
		return this.entityId;
	}
	
	@Override
	public float getSpeed() {
		return this.speed;
	}

	@Override
	public int getGlowRadius() {
		return this.glowRadius;
	}
	
	@Override
	public Upgrade getUpgrade() {
		return this.upgrade;
	}
	
	@Override
	public int getTotalSpiritsCollected() {
		return this.spiritsHeld;
	}

	@Override
	public boolean isConnected() {
		return this.connected;
	}

	@Override
	public Animation getAnimation() {
		return this.animation;
	}
	
	@Override
	public int getAnimationAmount() {
		return this.animationAmount;
	}

	@Override
	public Emotion getEmotion() {
		return this.emotion;
	}

	@Override
	public UpgradeAnimation getUpgradeAnimation() {
		return this.upgradeAnimation;
	}

	@Override
	public Upgrade getUpgradeAnimationUpgrade() {
		return this.upgradeAnimationUpgrade;
	}

}
