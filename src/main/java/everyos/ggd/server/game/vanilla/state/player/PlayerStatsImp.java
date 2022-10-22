package everyos.ggd.server.game.vanilla.state.player;

public class PlayerStatsImp implements PlayerStats {
	
	private int score = 0;
	private int stolen = 0;
	private int stolenFrom;

	@Override
	public int getScore() {
		return this.score;
	}
	
	@Override
	public void incrementScore(int score) {
		this.score += score;
	}

	@Override
	public int getStolen() {
		return this.stolen;
	}

	@Override
	public void incrementStolen(int stolen) {
		this.stolen += stolen;
	}

	@Override
	public int getStolenFrom() {
		return this.stolenFrom;
	}

	@Override
	public void incrementStolenFrom(int stolen) {
		this.stolenFrom += stolen;
	}
	
}
