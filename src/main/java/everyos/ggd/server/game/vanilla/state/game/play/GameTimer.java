package everyos.ggd.server.game.vanilla.state.game.play;

public class GameTimer {

	private static final int GAME_LENGTH_SECONDS = 180;
	
	private long timerStarted;
	
	public void start() {
		this.timerStarted = System.currentTimeMillis();
	}
	
	public boolean finished() {
		return getSecondsRemaining() == 0;
	}
	
	public int getSecondsRemaining() {
		return GAME_LENGTH_SECONDS - (int) ((System.currentTimeMillis() - timerStarted)/1000);
	}
	
}
