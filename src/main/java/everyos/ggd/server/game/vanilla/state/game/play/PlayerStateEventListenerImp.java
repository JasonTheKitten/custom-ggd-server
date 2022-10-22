package everyos.ggd.server.game.vanilla.state.game.play;

import everyos.ggd.server.game.vanilla.state.player.PlayerState.SpiritGainReason;
import everyos.ggd.server.game.vanilla.state.player.PlayerStateEventListener;
import everyos.ggd.server.game.vanilla.state.player.PlayerStateImp;

public class PlayerStateEventListenerImp implements PlayerStateEventListener {
	
	private final PowerUpTracker powerUpTracker = new PowerUpTracker();

	@Override
	public void onGain(PlayerStateImp playerStateImp, int amount, SpiritGainReason reason) {
		if (reason == SpiritGainReason.GOAL_RETURN) {
			powerUpTracker.onBaseReturnGain(playerStateImp, amount);
		} else if (reason == SpiritGainReason.STEAL_SPIRIT || reason == SpiritGainReason.COLLECT_SPIRIT) {
			powerUpTracker.onSpiritGain(playerStateImp, amount);
		}
	}

}
