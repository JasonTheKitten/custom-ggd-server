package everyos.ggd.server.message;

public interface PlayerStateUpdate {

	int getEntityId();
	
	float getSpeed();
	
	int getGlowRadius();
	
	Upgrade getUpgrade();
	
	int getTotalSpiritsCollected();
	
	boolean isConnected();
	
	Animation getAnimation();
	
	int getAnimationAmount();
	
	Emotion getEmotion();
	
	UpgradeAnimation getUpgradeAnimation();
	
	Upgrade getUpgradeAnimationUpgrade();
	
	public static enum Animation {
		NONE,
		SPIRITS_LOST,
		SPIRITS_COLLECTED,
		BUDDY_BONUS,
		MEGA_FLAME
	}
	
	public static enum Emotion {
		NONE, HAPPY, SAD
	}
	
	public static enum UpgradeAnimation {
		NONE, RETURN_TO_BASE, ACHIEVED
	}
	
	public static enum Upgrade {
		NONE, SPEED_UPGRADE, LIGHT_UPGRADE, MAGNET_UPGRADE, TRANSPARENT
	}
	
}
