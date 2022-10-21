package everyos.ggd.server.game.vanilla.state.game.play;

import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.game.vanilla.state.player.PlayerState;
import everyos.ggd.server.game.vanilla.state.player.PlayerStats;
import everyos.ggd.server.game.vanilla.util.ScoreUtil;
import everyos.ggd.server.message.imp.MatchStateUpdateMessageImp;

public class MatchStateTracker {
	
	private final MatchContext matchContext;
	private final PlayerState[] playerStates;
	private final GameTimer timer;

	public MatchStateTracker(MatchContext matchContext, PlayerState[] playerStates, GameTimer timer) {
		this.matchContext = matchContext;
		this.playerStates = playerStates;
		this.timer = timer;
	}
	
	public void tick() {
		if (timer.changed()) {
			sendMatchUpdate();
		}
	};

	private void sendMatchUpdate() {
		PlayerStats[] playerStats = getPlayerStats();
		matchContext.broadcast(new MatchStateUpdateMessageImp(
			ScoreUtil.getGreenTeamScore(playerStats),
			ScoreUtil.getPurpleTeamScore(playerStats),
			timer.getSecondsRemaining()));
	}
	
	private PlayerStats[] getPlayerStats() {
		PlayerStats[] stats = new PlayerStats[playerStates.length];
		for (int i = 0; i < playerStates.length; i++) {
			stats[i] = playerStates[i].getStats();
		}
		
		return stats;
	}
	
}
