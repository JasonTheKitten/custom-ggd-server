package everyos.ggd.server.message;

public interface PlayerStateUpdate extends EntityStateUpdate {

	int getSpeed();
	
	int getGlowRadius();
	
	int getNumSpiritsHeld();
	
	boolean isConnected();
	
	Animation getAnimation();
	
	Emotion getEmotion();
	
	boolean isBot();
	
	public static enum Animation {
		NONE,
		SPIRITS_COLLECTED,
		BUDDY_BONUS,
		MEGA_FLAME
	}
	
	public static enum Emotion {
		NONE, HAPPY, SAD
	}
	
}
