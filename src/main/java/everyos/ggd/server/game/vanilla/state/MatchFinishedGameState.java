package everyos.ggd.server.game.vanilla.state;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import everyos.ggd.server.event.Event;
import everyos.ggd.server.game.Player;
import everyos.ggd.server.game.vanilla.GameState;
import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.game.vanilla.PlayerStats;
import everyos.ggd.server.game.vanilla.util.ScoreUtil;
import everyos.ggd.server.message.MatchFinishedMessage.PlayerAward;
import everyos.ggd.server.message.Message;
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
	public List<Message> handleEvent(Player player, Event event) {
		return List.of();
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
