package everyos.ggd.server.game.vanilla.state.game;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import everyos.ggd.server.game.Player;
import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.game.vanilla.state.game.play.GameTimer;
import everyos.ggd.server.game.vanilla.state.game.play.PhysicsTracker;
import everyos.ggd.server.game.vanilla.state.game.play.PlayerSpiritPhysicsBodiesView;
import everyos.ggd.server.game.vanilla.state.player.PlayerState;
import everyos.ggd.server.game.vanilla.state.player.PlayerStats;
import everyos.ggd.server.game.vanilla.state.spirit.SpiritState;
import everyos.ggd.server.game.vanilla.util.ScoreUtil;
import everyos.ggd.server.message.EntityMoveMessage;
import everyos.ggd.server.message.EntityTeleportMessage;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.SpiritStateUpdate.SpiritTeam;
import everyos.ggd.server.message.imp.MatchStateUpdateMessageImp;
import everyos.ggd.server.physics.PhysicsBody;
import everyos.ggd.server.physics.Position;
import everyos.ggd.server.physics.Velocity;
import everyos.ggd.server.physics.imp.PositionImp;
import everyos.ggd.server.physics.imp.VelocityImp;

public class PlayGameState implements GameState {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final MatchContext matchContext;
	private final PlayerState[] playerStates;
	private final List<SpiritState> spiritStates;
	
	private final GameTimer timer = new GameTimer();
	private final PhysicsTracker physicsTracker;
	
	public PlayGameState(MatchContext matchContext, PlayerState[] playerStates, List<SpiritState> spiritStates) {
		this.matchContext = matchContext;
		this.playerStates = playerStates;
		this.spiritStates = spiritStates;
		
		this.physicsTracker = new PhysicsTracker(
			new PlayerSpiritPhysicsBodiesView(playerStates, spiritStates));
	}

	@Override
	public void start() {
		timer.start();
		sendMatchUpdate();
	}

	@Override
	public void ping() {
		if (timer.finished()) {
			matchContext.setGameState(new MatchFinishedGameState(matchContext, getPlayerStats()));
			return;
		}
		
		physicsTracker.tick();
		processPlayerMessages();
		processSpiritUpdates();
		processPlayerPositions();
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
		givePlayerNearbySpirits(playerEntityId, playerPosition);
		updateSpiritTrail(playerEntityId, playerPosition);
	}

	private void givePlayerNearbySpirits(int playerEntityId, Position playerPosition) {
		for (SpiritState spiritState: spiritStates) {
			if (spiritState.getOwnerEntityId() == -1 && playerInRangeOfSpirit(playerEntityId, playerPosition, spiritState)) {
				spiritState.setTeam(getSpiritTeam(playerEntityId));
				spiritState.setOwnerEntityId(playerEntityId);
				playerStates[playerEntityId]
					.getSpiritList()
					.add(spiritState);
			}
		}
	}
	
	private void updateSpiritTrail(int playerEntityId, Position playerPosition) {
		PlayerState playerState = playerStates[playerEntityId];
		List<SpiritState> spiritTrail = playerState.getSpiritList();
		if (spiritTrail.isEmpty()) {
			return;
		}
		
		PhysicsBody playerPhysicsBody = playerState.getPhysicsBody();
		{
			PhysicsBody spiritPhysicsBody = spiritTrail.get(0).getPhysicsBody();
			Position nextSpiritPosition = playerState.getPhysicsBody().getCurrentPosition();
			spiritPhysicsBody.setCurrentPosition(
				calculateNewTrailSpiritPosition(spiritPhysicsBody.getCurrentPosition(), nextSpiritPosition));
			spiritPhysicsBody.setCurrentVelocity(
				calculateNewTrailSpiritVelocity(
					spiritPhysicsBody.getCurrentPosition(),
					nextSpiritPosition,
					playerPhysicsBody.getCurrentVelocity()));
		}
		for (int i = 1; i < spiritTrail.size(); i++) {
			PhysicsBody spiritPhysicsBody = spiritTrail.get(i).getPhysicsBody();
			Position nextSpiritPosition = spiritTrail.get(i-1).getPhysicsBody().getCurrentPosition();
			spiritPhysicsBody.setCurrentPosition(
					calculateNewTrailSpiritPosition(spiritPhysicsBody.getCurrentPosition(), nextSpiritPosition));
		}
	}

	private SpiritTeam getSpiritTeam(int playerEntityId) {
		Player[] players = matchContext.getPlayers();
		return playerEntityId < players.length / 2 ?
			SpiritTeam.GREEN_TEAM :
			SpiritTeam.PURPLE_TEAM;
	}

	private boolean playerInRangeOfSpirit(int playerEntityId, Position playerPosition, SpiritState spiritState) {
		//TODO: Magnetism
		Position spiritPosition = spiritState.getPhysicsBody().getCurrentPosition();
		return getDistanceBetweenPositions(playerPosition, spiritPosition) <= 3f;
	}
	
	private Position calculateNewTrailSpiritPosition(Position currentPosition, Position nextSpiritPosition) {
		float totalDistance = getDistanceBetweenPositions(currentPosition, nextSpiritPosition);
		float xDistance = currentPosition.getX() - nextSpiritPosition.getX();
		float yDistance = currentPosition.getY() - nextSpiritPosition.getY();
		float xNewDistance = (xDistance / totalDistance) * 3;
		float yNewDistance = (yDistance / totalDistance) * 3;
		float xComponent = nextSpiritPosition.getX() + xNewDistance;
		float yComponent = nextSpiritPosition.getY() + yNewDistance;
		
		return new PositionImp(xComponent, yComponent);
	}
	
	private Velocity calculateNewTrailSpiritVelocity(Position currentPosition, Position nextSpiritPosition, Velocity playerVelocity) {
		float totalDistance = getDistanceBetweenPositions(currentPosition, nextSpiritPosition);
		float totalVelocity = (float) Math.sqrt(
			playerVelocity.getX() * playerVelocity.getX() +
			playerVelocity.getY() * playerVelocity.getY());
		float xDistance = currentPosition.getX() - nextSpiritPosition.getX();
		float yDistance = currentPosition.getY() - nextSpiritPosition.getY();
		float xNewDistance = xDistance / totalDistance;
		float yNewDistance = yDistance / totalDistance;
		float xComponent = xNewDistance * totalVelocity;
		float yComponent = yNewDistance * totalVelocity;
		
		return new VelocityImp(xComponent, yComponent);
	}

	private float getDistanceBetweenPositions(Position position1, Position position2) {
		float xComponent = Math.abs(position1.getX() - position2.getX());
		float yComponent = Math.abs(position1.getY() - position2.getY());
		return (float) Math.sqrt(yComponent*yComponent + xComponent*xComponent);
	}
	
	private void processSpiritUpdates() {
		for (SpiritState spiritState: spiritStates) {
			matchContext.broadcastMessages(spiritState.getQueuedMessages());
 		}
	}

}
