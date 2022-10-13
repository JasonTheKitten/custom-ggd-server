package everyos.ggd.server.game.vanilla.state.game;

import java.util.Timer;
import java.util.TimerTask;

import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.game.vanilla.state.player.PlayerStats;
import everyos.ggd.server.game.vanilla.util.ScoreUtil;
import everyos.ggd.server.message.MatchFinishedMessage.PlayerAward;
import everyos.ggd.server.message.imp.MatchFinishedMessageImp;

public class MatchFinishedGameState implements GameState {

	private final MatchContext matchContext;
	private final PlayerStats[] playerStats;

	public MatchFinishedGameState(MatchContext matchContext, PlayerStats[] playerStats) {
		this.matchContext = matchContext;
		this.playerStats = playerStats;
	}

	@Override
	public void start() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				showScoreScreen();
			}
			
		}, 1000);
		
	}

	@Override
	public void ping() {
		
	}
	
	private void showScoreScreen() {
		matchContext.broadcast(new MatchFinishedMessageImp(
			ScoreUtil.getGreenTeamScore(playerStats),
			ScoreUtil.getPurpleTeamScore(playerStats),
			new PlayerAward[] {}));
	}

}
