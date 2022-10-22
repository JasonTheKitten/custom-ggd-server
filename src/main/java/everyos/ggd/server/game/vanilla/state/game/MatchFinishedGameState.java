package everyos.ggd.server.game.vanilla.state.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.game.vanilla.state.player.PlayerStats;
import everyos.ggd.server.game.vanilla.util.ScoreUtil;
import everyos.ggd.server.message.MatchFinishedMessage.PlayerAward;
import everyos.ggd.server.message.MatchFinishedMessage.PlayerAward.Award;
import everyos.ggd.server.message.imp.MatchFinishedMessageImp;
import everyos.ggd.server.message.imp.MatchFinishedMessageImp.PlayerAwardImp;

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
			generatePlayAwards()));
	}

	private PlayerAward[] generatePlayAwards() {
		List<PlayerAward> awards = new ArrayList<>();
		for (int i = 0; i < playerStats.length; i++) {
			addPlayerAwards(i, playerStats[i], awards);
		}
		return awards.toArray(new PlayerAward[awards.size()]);
	}

	private void addPlayerAwards(int playerIndex, PlayerStats playerPersonalStats, List<PlayerAward> awards) {
		if (playerPersonalStats.getScore() == getHighestScore()) {
			awards.add(new PlayerAwardImp(playerIndex, Award.MOST_SPIRITED));
		}
		if (playerPersonalStats.getStolen() == getHighestStolen()) {
			awards.add(new PlayerAwardImp(playerIndex, Award.POLTERHEIST));
		}
		if (playerPersonalStats.getStolenFrom() == getHighestStolenFrom()) {
			awards.add(new PlayerAwardImp(playerIndex, Award.MOST_GENEROUS));
		}
		if (playerPersonalStats.getScore() == getLowestScore()) {
			awards.add(new PlayerAwardImp(playerIndex, Award.SHYEST));
		}
	}

	private int getHighestScore() {
		int high = 0;
		for (int i = 0; i < playerStats.length; i++) {
			high = Math.max(high, playerStats[i].getScore());
		}
		return high;
	}
	
	private int getLowestScore() {
		int low = Integer.MAX_VALUE;
		for (int i = 0; i < playerStats.length; i++) {
			low = Math.min(low, playerStats[i].getScore());
		}
		return low;
	}
	
	private int getHighestStolen() {
		int high = 0;
		for (int i = 0; i < playerStats.length; i++) {
			high = Math.max(high, playerStats[i].getStolen());
		}
		return high;
	}
	
	private int getHighestStolenFrom() {
		int high = 0;
		for (int i = 0; i < playerStats.length; i++) {
			high = Math.max(high, playerStats[i].getStolenFrom());
		}
		return high;
	}

}
