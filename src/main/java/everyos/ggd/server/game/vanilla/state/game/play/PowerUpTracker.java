package everyos.ggd.server.game.vanilla.state.game.play;

import everyos.ggd.server.game.vanilla.state.player.PlayerState;
import everyos.ggd.server.message.PlayerStateUpdate.Upgrade;

public class PowerUpTracker {
	
	private static int SPEED_NUM_SOULS = 15;
	private static int LIGHT_NUM_SOULS = 45;
	private static int MAGNET_NUM_SOULS = 105;
	private static int WALLS_NUM_SOULS = 225;

	public void onBaseReturnGain(PlayerState playerState, int amount) {
		int score = playerState
			.getStats()
			.getScore();
		if (playerState.getUpgradeLevel() == Upgrade.NONE && score >= SPEED_NUM_SOULS) {
			playerState.setUpgradeLevel(Upgrade.SPEED_UPGRADE);
			playerState.setSpeed(20f);
		}
		if (playerState.getUpgradeLevel() == Upgrade.SPEED_UPGRADE && score >= LIGHT_NUM_SOULS) {
			playerState.setUpgradeLevel(Upgrade.LIGHT_UPGRADE);
			playerState.setLight(100);
		}
		if (playerState.getUpgradeLevel() == Upgrade.LIGHT_UPGRADE && score >= MAGNET_NUM_SOULS) {
			playerState.setUpgradeLevel(Upgrade.MAGNET_UPGRADE);
		}
		if (playerState.getUpgradeLevel() == Upgrade.MAGNET_UPGRADE && score >= WALLS_NUM_SOULS) {
			playerState.setUpgradeLevel(Upgrade.TRANSPARENT);
		}
	}
	
	public void onSpiritGain(PlayerState playerState, int amount) {
		if (playerState.getUpgradeLevel() == Upgrade.NONE && scoreWillBe(SPEED_NUM_SOULS, playerState, amount)) {
			playerState.setUpgradeHint(Upgrade.SPEED_UPGRADE);
		} else if (playerState.getUpgradeLevel() == Upgrade.SPEED_UPGRADE && scoreWillBe(LIGHT_NUM_SOULS, playerState, amount)) {
			playerState.setUpgradeHint(Upgrade.LIGHT_UPGRADE);
		} else if (playerState.getUpgradeLevel() == Upgrade.LIGHT_UPGRADE && scoreWillBe(MAGNET_NUM_SOULS, playerState, amount)) {
			playerState.setUpgradeHint(Upgrade.MAGNET_UPGRADE);
		} else if (playerState.getUpgradeLevel() == Upgrade.MAGNET_UPGRADE && scoreWillBe(WALLS_NUM_SOULS, playerState, amount)) {
			playerState.setUpgradeHint(Upgrade.TRANSPARENT);
		}
	}

	private boolean scoreWillBe(int i, PlayerState playerState, int amount) {
		int score = playerState
			.getStats()
			.getScore();
		int scoreAfter = score + playerState.getSpiritList().size();
		int scoreBefore = scoreAfter - amount;
		return scoreBefore < i && scoreAfter >= i;
	}

}
