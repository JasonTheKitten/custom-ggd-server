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
	
}
