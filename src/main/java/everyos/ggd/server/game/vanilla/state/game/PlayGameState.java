package everyos.ggd.server.game.vanilla.state.game;

import java.util.List;

import everyos.ggd.server.game.MatchData;
import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.game.vanilla.state.game.play.GameTimer;
import everyos.ggd.server.game.vanilla.state.game.play.MatchStateTracker;
import everyos.ggd.server.game.vanilla.state.game.play.PhysicsTracker;
import everyos.ggd.server.game.vanilla.state.game.play.PlayerMessageProcessor;
import everyos.ggd.server.game.vanilla.state.game.play.SpiritTracker;
import everyos.ggd.server.game.vanilla.state.player.PlayerState;
import everyos.ggd.server.game.vanilla.state.player.PlayerStats;
import everyos.ggd.server.game.vanilla.state.spirit.SpiritState;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.physics.Position;
import everyos.ggd.server.player.BotPlayer;
import everyos.ggd.server.player.HumanPlayer;
import everyos.ggd.server.player.Player;

public class PlayGameState implements GameState {
	
	private final MatchContext matchContext;
	private final PlayerState[] playerStates;
	private final MatchData matchData;
	
	private final GameTimer timer = new GameTimer();
	private final PhysicsTracker physicsTracker;
	private final SpiritTracker spiritTracker;
	private final PlayerMessageProcessor playerMessageProcessor;
	private final MatchStateTracker matchStateTracker;
	
	public PlayGameState(MatchContext matchContext, PlayerState[] playerStates, List<SpiritState> mapSpiritStates) {
		this.matchContext = matchContext;
		this.playerStates = playerStates;
		
		this.physicsTracker = new PhysicsTracker(matchContext);
		this.spiritTracker = new SpiritTracker(matchContext, playerStates, mapSpiritStates);
		this.playerMessageProcessor = new PlayerMessageProcessor(
			matchContext, playerStates,
			(playerEntityId, playerPosition) -> handlePlayerPositionUpdate(playerEntityId, playerPosition));
		this.matchStateTracker = new MatchStateTracker(matchContext, playerStates, timer);
		
		this.matchData = new MatchData(
			matchContext.getMap(),
			matchContext.getEntityRegister().getAllEntities());
	}
	
	@Override
	public void start() {
		timer.start();
		physicsTracker.start();
		matchStateTracker.tick();
		startBots();
		sendEntityStateUpdates();
	}

	@Override
	public void ping() {
		if (timer.finished()) {
			setFinalPlayerStates();
			matchContext.setGameState(new MatchFinishedGameState(matchContext, getPlayerStats()));
			return;
		}
		
		pingPlayers();
		physicsTracker.tick();
		playerMessageProcessor.tick();
		spiritTracker.tick();
		matchStateTracker.tick();
		sendEntityStateUpdates();
	}
	
	private void startBots() {
		Player[] players = matchContext.getPlayers();
		for (int i = 0; i < players.length; i++) {
			Player player = players[i];
			PlayerState playerState = playerStates[i];
			if (player instanceof BotPlayer) {
				startBot((BotPlayer) player, playerState);
			}
		}
	}

	private void startBot(BotPlayer player, PlayerState playerState) {
		player.start(matchData, playerState);
	}

	private void pingPlayers() {
		Player[] players = matchContext.getPlayers();
		for (int i = 0; i < players.length; i++) {
			Player player = players[i];
			checkConnected(player, playerStates[i]);
			player.ping();
		}
	}

	private void checkConnected(Player player, PlayerState playerState) {
		if (player instanceof HumanPlayer) {
			playerState.setConnected(((HumanPlayer) player).getConnected());
		}
	}

	private PlayerStats[] getPlayerStats() {
		PlayerStats[] stats = new PlayerStats[playerStates.length];
		for (int i = 0; i < playerStates.length; i++) {
			stats[i] = playerStates[i].getStats();
		}
		
		return stats;
	}

	private void sendEntityStateUpdates() {
		List<Message> queuedMessages = matchContext
			.getEntityRegister()
			.getQueuedMessages();
		matchContext.broadcastMessages(queuedMessages);
	}
	
	private void setFinalPlayerStates() {
		for (PlayerState playerState: playerStates) {
			playerState.indicateMatchFinished();
			matchContext.broadcastMessages(playerState.getQueuedMessages());
		}
	}
	
	private void handlePlayerPositionUpdate(int playerEntityId, Position playerPosition) {
		spiritTracker.handlePlayerPositionUpdate(playerEntityId, playerPosition);
	}

}
