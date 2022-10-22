package everyos.ggd.server.message;

public interface PlayerStateUpdate {

	int getEntityId();
	
	float getSpeed();
	
	int getGlowRadius();
	
	int getNumSpiritsHeld();
	
	boolean isConnected();
	
	Animation getAnimation();
	
	int getAnimationAmount();
	
	Emotion getEmotion();
	
	UpgradeData getUpgradeData();
	
	Upgrade getUpgrade();
	
	public static enum Animation {
		NONE,
		UNKNOWN,
		SPIRITS_COLLECTED,
		BUDDY_BONUS,
		MEGA_FLAME
	}
	
	public static enum Emotion {
		NONE, HAPPY, SAD
	}
	
	public static enum UpgradeData {
		NONE, RETURN_TO_BASE, ACHIEVED
	}
	
	public static enum Upgrade {
		NONE, SPEED_UPGRADE, LIGHT_UPGRADE, MAGNET_UPGRADE, TRANSPARENT
	}
	
}
