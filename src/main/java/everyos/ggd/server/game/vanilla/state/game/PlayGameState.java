package everyos.ggd.server.game.vanilla.state.game;

import java.util.List;

import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.game.vanilla.state.game.play.GameTimer;
import everyos.ggd.server.game.vanilla.state.game.play.MatchStateTracker;
import everyos.ggd.server.game.vanilla.state.game.play.PhysicsTracker;
import everyos.ggd.server.game.vanilla.state.game.play.PlayerMessageProcessor;
import everyos.ggd.server.game.vanilla.state.game.play.PlayerSpiritPhysicsBodiesView;
import everyos.ggd.server.game.vanilla.state.game.play.SpiritTracker;
import everyos.ggd.server.game.vanilla.state.player.PlayerState;
import everyos.ggd.server.game.vanilla.state.player.PlayerStateImp;
import everyos.ggd.server.game.vanilla.state.player.PlayerStats;
import everyos.ggd.server.game.vanilla.state.spirit.SpiritState;
import everyos.ggd.server.physics.Position;

public class PlayGameState implements GameState {
	
	private final MatchContext matchContext;
	private final PlayerState[] playerStates;
	
	private final GameTimer timer = new GameTimer();
	private final PhysicsTracker physicsTracker;
	private final SpiritTracker spiritTracker;
	private final PlayerMessageProcessor playerMessageProcessor;
	private final MatchStateTracker matchStateTracker;
	
	public PlayGameState(MatchContext matchContext, PlayerState[] playerStates, List<SpiritState> spiritStates) {
		this.matchContext = matchContext;
		this.playerStates = playerStates;
		
		this.physicsTracker = new PhysicsTracker(
			new PlayerSpiritPhysicsBodiesView(playerStates, spiritStates));
		this.spiritTracker = new SpiritTracker(matchContext, playerStates, spiritStates);
		this.playerMessageProcessor = new PlayerMessageProcessor(
			matchContext, playerStates,
			(playerEntityId, playerPosition) -> handlePlayerPositionUpdate(playerEntityId, playerPosition));
		this.matchStateTracker = new MatchStateTracker(matchContext, playerStates, timer);
	}
	
	@Override
	public void start() {
		timer.start();
		matchStateTracker.tick();
	}

	@Override
	public void ping() {
		if (timer.finished()) {
			executeTemporaryCreditScreenPatch();
			matchContext.setGameState(new MatchFinishedGameState(matchContext, getPlayerStats()));
			return;
		}
		
		physicsTracker.tick();
		playerMessageProcessor.tick();
		processPlayerStateUpdates();
		spiritTracker.tick();
		playerMessageProcessor.tick2();
		matchStateTracker.tick();
	}
	
	private PlayerStats[] getPlayerStats() {
		PlayerStats[] stats = new PlayerStats[playerStates.length];
		for (int i = 0; i < playerStates.length; i++) {
			stats[i] = playerStates[i].getStats();
		}
		
		return stats;
	}

	private void executeTemporaryCreditScreenPatch() {
		for (PlayerState playerState: playerStates) {
			((PlayerStateImp) playerState).performTemporarayCreditScreenPatch();
			matchContext.broadcastMessages(playerState.getQueuedMessages());
		}
	}

	private void processPlayerStateUpdates() {
		for (PlayerState playerState: playerStates) {
			matchContext.broadcastMessages(
				playerState.getQueuedMessages());
		}
	}
	
	private void handlePlayerPositionUpdate(int playerEntityId, Position playerPosition) {
		spiritTracker.handlePlayerPositionUpdate(playerEntityId, playerPosition);
	}

}
