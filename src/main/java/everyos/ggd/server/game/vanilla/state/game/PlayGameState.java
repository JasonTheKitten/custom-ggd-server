package everyos.ggd.server.game.vanilla.state.game;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import everyos.ggd.server.game.Player;
import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.game.vanilla.state.game.play.GameTimer;
import everyos.ggd.server.game.vanilla.state.game.play.PhysicsTracker;
import everyos.ggd.server.game.vanilla.state.game.play.PlayerSpiritPhysicsBodiesView;
import everyos.ggd.server.game.vanilla.state.game.play.SpiritTracker;
import everyos.ggd.server.game.vanilla.state.player.PlayerState;
import everyos.ggd.server.game.vanilla.state.player.PlayerStateImp;
import everyos.ggd.server.game.vanilla.state.player.PlayerStats;
import everyos.ggd.server.game.vanilla.state.spirit.SpiritState;
import everyos.ggd.server.game.vanilla.util.ScoreUtil;
import everyos.ggd.server.message.EntityMoveMessage;
import everyos.ggd.server.message.EntityTeleportMessage;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.imp.MatchStateUpdateMessageImp;
import everyos.ggd.server.physics.PhysicsBody;
import everyos.ggd.server.physics.Position;

public class PlayGameState implements GameState {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final MatchContext matchContext;
	private final PlayerState[] playerStates;
	@SuppressWarnings("unused")
	private final List<SpiritState> spiritStates;
	
	private final GameTimer timer = new GameTimer();
	private final PhysicsTracker physicsTracker;
	private final SpiritTracker spiritTracker;
	
	public PlayGameState(MatchContext matchContext, PlayerState[] playerStates, List<SpiritState> spiritStates) {
		this.matchContext = matchContext;
		this.playerStates = playerStates;
		this.spiritStates = spiritStates;
		
		this.physicsTracker = new PhysicsTracker(
			new PlayerSpiritPhysicsBodiesView(playerStates, spiritStates));
		this.spiritTracker = new SpiritTracker(matchContext, playerStates, spiritStates);
	}

	@Override
	public void start() {
		timer.start();
		sendMatchUpdate();
	}

	@Override
	public void ping() {
		if (timer.finished()) {
			executeTemporaryCreditScreenPatch();
			matchContext.setGameState(new MatchFinishedGameState(matchContext, getPlayerStats()));
			return;
		}
		
		physicsTracker.tick();
		processPlayerMessages();
		spiritTracker.tick();
		processPlayerPositions();
		processPlayerStateUpdates();
		
		if (timer.changed()) {
			sendMatchUpdate();
		}
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
	};

	private void processPlayerMessages() {
		Player[] players = matchContext.getPlayers();
		for (Player player: players) {
			for (Message message: player.getQueuedMessagesFromClient()) {
				processPlayerMessage(player, message);
			}
		}
	}

	private void processPlayerMessage(Player player, Message message) {
		switch (message.getType()) {
		case Message.ENTITY_MOVE:
			processEntityMoveMessage((EntityMoveMessage) message, player.getId());
			break;
		case Message.ENTITY_TELEPORT:
			processEntityTeleportMessage((EntityTeleportMessage) message, player.getId());
			break;
		default:
			logger.warn("Did not process message [type=" + message.getType() + "]");
		}
	}

	private void processEntityMoveMessage(EntityMoveMessage message, int playerId) {
		matchContext.rebroadcast(message, playerId);
		int playerEntityId = message.getEntityId();
		playerStates[playerEntityId]
			.getPhysicsBody()
			.setCurrentVelocity(message.getVelocity());
	}
	
	private void processEntityTeleportMessage(EntityTeleportMessage message, int playerId) {
		matchContext.rebroadcast(message, playerId);
		int playerEntityId = message.getEntityId();
		PhysicsBody playerPhysicsBody = playerStates[playerEntityId].getPhysicsBody();
		playerPhysicsBody.setCurrentPosition(message.getPosition());
		playerPhysicsBody.setCurrentVelocity(message.getVelocity());
	}
	
	void processPlayerPositions() {
		for (PlayerState playerState: playerStates) {
			Position newPosition = playerState.getPhysicsBody().getCurrentPosition();
			handlePlayerPositionUpdate(playerState.getEntityId(), newPosition);
		}
	}

	private void handlePlayerPositionUpdate(int playerEntityId, Position playerPosition) {
		spiritTracker.handlePlayerPositionUpdate(playerEntityId, playerPosition);
	}

}
