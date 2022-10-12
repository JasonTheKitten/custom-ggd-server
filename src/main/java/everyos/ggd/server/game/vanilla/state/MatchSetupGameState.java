package everyos.ggd.server.game.vanilla.state;

import everyos.ggd.server.game.Player;
import everyos.ggd.server.game.vanilla.GameState;
import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.game.vanilla.util.MapUtil;
import everyos.ggd.server.map.MatchMap;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.PlayerStateUpdate;
import everyos.ggd.server.message.imp.MatchInitMessageImp;
import everyos.ggd.server.message.imp.PlayerInitMessageImp;
import everyos.ggd.server.message.imp.PlayerStateUpdateBuilder;
import everyos.ggd.server.message.imp.SessionDataSetMessageImp;
import everyos.ggd.server.physics.Location;
import everyos.ggd.server.physics.Position;

public class MatchSetupGameState implements GameState {
	
	private final MatchContext matchContext;

	public MatchSetupGameState(MatchContext matchContext) {
		this.matchContext = matchContext;
	}
	
	@Override
	public void start() {
		
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
			player.onMessageFromServer(matchInitMessage);
			matchContext.broadcast(createInitialPlayerStateUpdateMessage(i, player));
			player.onMessageFromServer(createSessionDataSetMessage(i));
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
			getPlayerStartPosition(matchContext.getMap(), playerNumber),
			playerNumber,
			player.isBot(),
			state);
	}

	private Position getPlayerStartPosition(MatchMap map, int playerNumber) {
		return MapUtil.locationToPosition(
			map,
			getPlayerStartLocation(map, playerNumber));
	}
	
	private Location getPlayerStartLocation(MatchMap map, int playerNumber) {
		Location[] locations = getPossiblePlayerStartLocations(map, playerNumber);
		
		return locations[(int) (Math.random() * locations.length)];
	}
	
	private Location[] getPossiblePlayerStartLocations(MatchMap map, int playerNumber) {
		Player[] players = matchContext.getPlayers();
		boolean playerIsGreen = playerNumber < players.length/2;
		return playerIsGreen ?
			MapUtil.getGreenBaseTileLocations(map) :
			MapUtil.getPurpleBaseTileLocations(map);
	}

}
