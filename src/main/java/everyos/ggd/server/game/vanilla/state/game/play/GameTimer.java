package everyos.ggd.server.game.vanilla.state.game.play;

public class GameTimer {

	private static final int GAME_LENGTH_SECONDS = 180;
	
	private long timerStarted;
	private int lastPoll = 0;
	
	public void start() {
		this.timerStarted = System.currentTimeMillis();
	}
	
	public boolean finished() {
		return getSecondsRemainingInternal() == 0;
	}
	
	public int getSecondsRemaining() {
		lastPoll = getSecondsRemainingInternal();
		
		return lastPoll;
	}

	public boolean changed() {
		return lastPoll != getSecondsRemainingInternal();
	}
	
	private int getSecondsRemainingInternal() {
		return GAME_LENGTH_SECONDS - (int) ((System.currentTimeMillis() - timerStarted)/1000);
	}
	
}
