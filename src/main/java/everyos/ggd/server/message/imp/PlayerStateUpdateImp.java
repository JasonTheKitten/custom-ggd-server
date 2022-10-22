package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.PlayerStateUpdate;

public class PlayerStateUpdateImp implements PlayerStateUpdate {

	private final int entityId;
	private final float speed;
	private final int glowRadius;
	private final int spiritsHeld;
	private final boolean connected;
	private final Animation animation;
	private final int animationAmount;
	private final Emotion emotion;
	private final UpgradeData upgradeData;
	private final Upgrade upgrade;

	public PlayerStateUpdateImp(PlayerStateUpdateBuilder builder) {
		this.entityId = builder.getEntityId();
		this.speed = builder.getSpeed();
		this.glowRadius = builder.getGlowRadius();
		this.spiritsHeld = builder.getNumSpiritsHeld();
		this.connected = builder.getConnected();
		this.animation = builder.getAnimation();
		this.animationAmount = builder.getAnimationAmount();
		this.emotion = builder.getEmotion();
		this.upgradeData = builder.getUpgradeData();
		this.upgrade = builder.getUpgrade();
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
	public int getNumSpiritsHeld() {
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
	public UpgradeData getUpgradeData() {
		return this.upgradeData;
	}

	@Override
	public Upgrade getUpgrade() {
		return this.upgrade;
	}

}
