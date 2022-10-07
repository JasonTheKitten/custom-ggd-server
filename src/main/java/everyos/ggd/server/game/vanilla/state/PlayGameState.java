package everyos.ggd.server.game.vanilla.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import everyos.ggd.server.game.Player;
import everyos.ggd.server.game.vanilla.GameState;
import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.game.vanilla.PlayerStats;
import everyos.ggd.server.game.vanilla.util.ScoreUtil;
import everyos.ggd.server.message.EntityMoveMessage;
import everyos.ggd.server.message.EntityTeleportMessage;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.imp.MatchStateUpdateMessageImp;
import everyos.ggd.server.message.imp.PlayerStatsImp;

public class PlayGameState implements GameState {
	
	private static final int GAME_LENGTH_SECONDS = 180;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final MatchContext matchContext;
	private final PlayerStats[] playerStats;
	
	private long timerStarted;
	
	public PlayGameState(MatchContext matchContext) {
		this.matchContext = matchContext;
		this.playerStats = new PlayerStats[matchContext.getPlayers().length];
		initPlayerStats();
	}

	@Override
	public void start() {
		this.timerStarted = System.currentTimeMillis();
		sendMatchUpdate();
	}

	@Override
	public void ping() {
		if (getSecondsRemaining() == 0) {
			matchContext.setGameState(new MatchFinishedGameState(matchContext, playerStats));
			return;
		}
		
		processPlayerMessages();
	}

	private void initPlayerStats() {
		for (int i = 0; i < playerStats.length; i++) {
			playerStats[i] = new PlayerStatsImp();
		}
	}
	
	private void sendMatchUpdate() {
		matchContext.broadcast(new MatchStateUpdateMessageImp(
			ScoreUtil.getGreenTeamScore(playerStats),
			ScoreUtil.getPurpleTeamScore(playerStats),
			getSecondsRemaining()));
	}
	
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
	}

}
