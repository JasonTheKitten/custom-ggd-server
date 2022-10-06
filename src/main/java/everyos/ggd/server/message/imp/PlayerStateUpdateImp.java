package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.PlayerStateUpdate;

public class PlayerStateUpdateImp implements PlayerStateUpdate {

	private final int entityId;
	private final float speed;
	private final boolean connected;

	public PlayerStateUpdateImp(PlayerStateUpdateBuilder builder) {
		this.entityId = builder.getEntityId();
		this.speed = builder.getSpeed();
		this.connected = builder.getConnected();
	}
	
	@Override
	public int getEntityId() {
		return this.entityId;
	}
	
	@Override
	public EntityType getEntityType() {
		return EntityType.PLAYER;
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isConnected() {
		return this.connected;
	}

	@Override
	public Animation getAnimation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Emotion getEmotion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isBot() {
		// TODO Auto-generated method stub
		return false;
	}

}
