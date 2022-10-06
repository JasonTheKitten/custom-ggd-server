package everyos.ggd.server.game.vanilla.state;

import java.util.List;

import everyos.ggd.server.common.imp.PositionImp;
import everyos.ggd.server.event.Event;
import everyos.ggd.server.game.Player;
import everyos.ggd.server.game.vanilla.GameState;
import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.PlayerStateUpdate;
import everyos.ggd.server.message.imp.MatchInitMessageImp;
import everyos.ggd.server.message.imp.PlayerInitMessageImp;
import everyos.ggd.server.message.imp.PlayerStateUpdateBuilder;
import everyos.ggd.server.message.imp.SessionDataSetMessageImp;

public class MatchSetupGameState implements GameState {
	
	private final MatchContext matchContext;

	public MatchSetupGameState(MatchContext matchContext) {
		this.matchContext = matchContext;
	}
	
	@Override
	public void start() {
		
	}

	@Override
	public List<Message> handleEvent(Player player, Event event) {
		return List.of();
	}

	@Override
	public void ping() {
		Player[] players = matchContext.getPlayers();
		if (players[players.length - 1] != null) {
			initPlayerState();
			matchContext.setGameState(new CountdownGameState(matchContext));
		}
	}

	private void initPlayerState() {
		Message matchInitMessage = new MatchInitMessageImp(matchContext.getMapName() + ".json");
		Player[] players = matchContext.getPlayers();
		for (int i = 0; i < players.length; i++) {
			Player player = players[i];
			player.onMessage(matchInitMessage);
			matchContext.broadcast(createInitialPlayerStateUpdateMessage(i, player));
			player.onMessage(createSessionDataSetMessage(i));
		}
	}

	private Message createSessionDataSetMessage(int playerNumber) {
		return new SessionDataSetMessageImp(playerNumber, matchContext.getMatchId(), "shard0");
	}

	private Message createInitialPlayerStateUpdateMessage(int playerNumber, Player player) {
		PlayerStateUpdate state = new PlayerStateUpdateBuilder()
			.setEntityId(playerNumber)
			.setSpeed(15f)
			.setConnected(true)
			.build();
		
		return new PlayerInitMessageImp(
			new PositionImp(0, 0),
			playerNumber,
			player.isBot(),
			state);
	}

}
