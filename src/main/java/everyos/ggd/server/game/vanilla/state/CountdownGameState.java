package everyos.ggd.server.game.vanilla.state;

import everyos.ggd.server.game.vanilla.GameState;
import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.message.imp.CountdownMessageImp;

public class CountdownGameState implements GameState {
	
	private final MatchContext matchContext;
	
	private long startTime;
	private int previousSecondsElapsed = 0;

	public CountdownGameState(MatchContext matchContext) {
		this.matchContext = matchContext;
	}
	
	@Override
	public void start() {
		this.startTime = System.currentTimeMillis();
	}

	@Override
	public void ping() {
		int secondsElapsed = getSecondsElapsed();
		if (secondsElapsed == previousSecondsElapsed) {
			return;
		}
		previousSecondsElapsed = secondsElapsed;
		
		if (secondsElapsed < 2) {
			return;
		}
		secondsElapsed -= 2;
		
		if (secondsElapsed >= 10) {
			matchContext.setGameState(new PlayGameState(matchContext));
		} else if (secondsElapsed >= 5) {
			setCountdownTimer(10 - secondsElapsed);
		}
	}

	private int getSecondsElapsed() {
		return (int) ((System.currentTimeMillis() - startTime) / 1000);
	}
	
	private void setCountdownTimer(int seconds) {
		matchContext.broadcast(new CountdownMessageImp(seconds));
	}

}
