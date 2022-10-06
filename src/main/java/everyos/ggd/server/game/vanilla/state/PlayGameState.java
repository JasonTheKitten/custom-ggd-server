package everyos.ggd.server.game.vanilla.state;

import everyos.ggd.server.game.vanilla.GameState;
import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.game.vanilla.PlayerStats;
import everyos.ggd.server.game.vanilla.util.ScoreUtil;
import everyos.ggd.server.message.imp.MatchStateUpdateMessageImp;
import everyos.ggd.server.message.imp.PlayerStatsImp;

public class PlayGameState implements GameState {
	
	private static final int GAME_LENGTH_SECONDS = 180;
	
	private final MatchContext matchContext;
	private final PlayerStats[] playerStats;
	
	private long timerStarted;
	
	public PlayGameState(MatchContext matchContext) {
		this.matchContext = matchContext;
		this.playerStats = new PlayerStats[matchContext.getPlayers().length];
		initPlayerStats();
	}

	@Override
	public void start() {
		this.timerStarted = System.currentTimeMillis();
		sendMatchUpdate();
	}

	@Override
	public void ping() {
		if (getSecondsRemaining() == 0) {
			matchContext.setGameState(new MatchFinishedGameState(matchContext, playerStats));
		}
	}
	
	private void initPlayerStats() {
		for (int i = 0; i < playerStats.length; i++) {
			playerStats[i] = new PlayerStatsImp();
		}
	}
	
	private void sendMatchUpdate() {
		matchContext.broadcast(new MatchStateUpdateMessageImp(
			ScoreUtil.getGreenTeamScore(playerStats),
			ScoreUtil.getPurpleTeamScore(playerStats),
			getSecondsRemaining()));
	}
	
	private int getSecondsRemaining() {
		return GAME_LENGTH_SECONDS - (int) ((System.currentTimeMillis() - timerStarted)/1000);
	}

}
