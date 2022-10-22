package everyos.ggd.server.game.vanilla.state.player;

import everyos.ggd.server.game.vanilla.state.player.PlayerState.SpiritGainReason;

public interface PlayerStateEventListener {

	void onGain(PlayerStateImp playerStateImp, int amount, SpiritGainReason reason);

}
