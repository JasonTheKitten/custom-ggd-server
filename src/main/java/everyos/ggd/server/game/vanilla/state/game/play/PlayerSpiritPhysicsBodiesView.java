package everyos.ggd.server.game.vanilla.state.game.play;

import java.util.List;

import everyos.ggd.server.game.vanilla.state.game.play.PhysicsTracker.PhysicsBodiesView;
import everyos.ggd.server.game.vanilla.state.player.PlayerState;
import everyos.ggd.server.game.vanilla.state.spirit.SpiritState;
import everyos.ggd.server.physics.PhysicsBody;

public class PlayerSpiritPhysicsBodiesView implements PhysicsBodiesView {

	private final PlayerState[] playerStates;
	private final List<SpiritState> spiritStates;

	public PlayerSpiritPhysicsBodiesView(PlayerState[] playerStates, List<SpiritState> spiritStates) {
		this.playerStates = playerStates;
		this.spiritStates = spiritStates;
	}

	@Override
	public int getLength() {
		return playerStates.length + spiritStates.size();
	}

	@Override
	public PhysicsBody get(int index) {
		if (index < playerStates.length) {
			return playerStates[index].getPhysicsBody();
		} else {
			return spiritStates.get(index - playerStates.length).getPhysicsBody();
		}
	}

}
