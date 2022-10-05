package everyos.ggd.server.game.vanilla.util;

import everyos.ggd.server.game.vanilla.PlayerStats;

public final class ScoreUtil {

	private ScoreUtil() {}
	
	public static int getGreenTeamScore(PlayerStats[] stats) {
		int score = 0;
		for (int i = 0; i < stats.length/2; i++) {
			score += stats[i].getScore();
		}
		return score;
	}
	
	public static int getPurpleTeamScore(PlayerStats[] stats) {
		int score = 0;
		for (int i = stats.length/2; i < stats.length; i++) {
			score += stats[i].getScore();
		}
		return score;
	}
	
}
