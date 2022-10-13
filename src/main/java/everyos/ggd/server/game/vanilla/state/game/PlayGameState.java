package everyos.ggd.server.game.vanilla.state.game;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import everyos.ggd.server.game.Player;
import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.game.vanilla.state.player.PlayerState;
import everyos.ggd.server.game.vanilla.state.player.PlayerStats;
import everyos.ggd.server.game.vanilla.state.spirit.SpiritState;
import everyos.ggd.server.game.vanilla.util.ScoreUtil;
import everyos.ggd.server.message.EntityMoveMessage;
import everyos.ggd.server.message.EntityTeleportMessage;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.SpiritStateUpdate.SpiritTeam;
import everyos.ggd.server.message.imp.MatchStateUpdateMessageImp;
import everyos.ggd.server.message.imp.SpiritStateUpdateMessageImp;
import everyos.ggd.server.physics.Position;

public class PlayGameState implements GameState {
	
	private static final int GAME_LENGTH_SECONDS = 180;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final MatchContext matchContext;
	private final PlayerState[] playerStates;
	private final List<SpiritState> spiritStates;
	
	private long timerStarted;
	
	public PlayGameState(MatchContext matchContext, PlayerState[] playerStates, List<SpiritState> spiritStates) {
		this.matchContext = matchContext;
		this.playerStates = playerStates;
		this.spiritStates = spiritStates;
	}

	@Override
	public void start() {
		this.timerStarted = System.currentTimeMillis();
		sendMatchUpdate();
	}

	@Override
	public void ping() {
		if (getSecondsRemaining() == 0) {
			matchContext.setGameState(new MatchFinishedGameState(matchContext, getPlayerStats()));
			return;
		}
		
		processPlayerMessages();
		processSpiritUpdates();
	}

	private void sendMatchUpdate() {
		PlayerStats[] playerStats = getPlayerStats();
		matchContext.broadcast(new MatchStateUpdateMessageImp(
			ScoreUtil.getGreenTeamScore(playerStats),
			ScoreUtil.getPurpleTeamScore(playerStats),
			getSecondsRemaining()));
	}
	
	private PlayerStats[] getPlayerStats() {
		PlayerStats[] stats = new PlayerStats[playerStates.length];
		for (int i = 0; i < playerStates.length; i++) {
			stats[i] = playerStates[i].getStats();
		}
		
		return stats;
	};
	
	private int getSecondsRemaining() {
		return GAME_LENGTH_SECONDS - (int) ((System.currentTimeMillis() - timerStarted)/1000);
	}
	
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
	}
	
	private void processEntityTeleportMessage(EntityTeleportMessage message, int playerId) {
		matchContext.rebroadcast(message, playerId);
		givePlayerNearbySpirits(message.getEntityId(), message.getPosition());
	}

	private void givePlayerNearbySpirits(int playerEntityId, Position playerPosition) {
		for (SpiritState spiritState: spiritStates) {
			if (playerInRangeOfSpirit(playerEntityId, playerPosition, spiritState)) {
				spiritState.setTeam(getSpiritTeam(playerEntityId));
			}
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
		Position spiritPosition = spiritState.getCurrentPosition();
		return getDistanceBetweenPositions(playerPosition, spiritPosition) <= 3f;
	}

	private float getDistanceBetweenPositions(Position position1, Position position2) {
		float xComponent = Math.abs(position1.getX() - position2.getX());
		float yComponent = Math.abs(position1.getY() - position2.getY());
		return (float) Math.sqrt(yComponent*yComponent + xComponent*xComponent);
	}
	
	private void processSpiritUpdates() {
		for (SpiritState spiritState: spiritStates) {
			if (spiritState.needsUpdate()) {
				matchContext.broadcast(
					new SpiritStateUpdateMessageImp(spiritState.createUpdate()));
			}
 		}
	}

}
