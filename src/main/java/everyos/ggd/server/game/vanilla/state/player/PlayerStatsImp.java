package everyos.ggd.server.game.vanilla.state.player;

public class PlayerStatsImp implements PlayerStats {
	
	private int score = 0;

	@Override
	public int getScore() {
		return this.score;
	}
	
	@Override
	public void setScore(int score) {
		this.score += score;
	}

}
