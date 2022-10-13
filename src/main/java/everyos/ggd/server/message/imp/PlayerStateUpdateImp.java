package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.PlayerStateUpdate;

public class PlayerStateUpdateImp implements PlayerStateUpdate {

	private final int entityId;
	private final float speed;
	private final int spiritsHeld;
	private final boolean connected;
	private final Animation animation;
	private final int animationAmount;
	private final Emotion emotion;

	public PlayerStateUpdateImp(PlayerStateUpdateBuilder builder) {
		this.entityId = builder.getEntityId();
		this.speed = builder.getSpeed();
		this.spiritsHeld = builder.getNumSpiritsHeld();
		this.connected = builder.getConnected();
		this.animation = builder.getAnimation();
		this.animationAmount = builder.getAnimationAmount();
		this.emotion = builder.getEmotion();
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
		// TODO Auto-generated method stub
		return 0;
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

}
