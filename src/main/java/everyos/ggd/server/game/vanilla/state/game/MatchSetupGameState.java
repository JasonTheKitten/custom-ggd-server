package everyos.ggd.server.game.vanilla.state.game;

import java.util.ArrayList;
import java.util.List;

import everyos.ggd.server.game.Player;
import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.game.vanilla.state.player.PlayerState;
import everyos.ggd.server.game.vanilla.state.player.PlayerStateImp;
import everyos.ggd.server.game.vanilla.state.spirit.SpiritState;
import everyos.ggd.server.game.vanilla.state.spirit.SpiritStateImp;
import everyos.ggd.server.game.vanilla.util.MapUtil;
import everyos.ggd.server.map.MatchMap;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.PlayerInitMessage;
import everyos.ggd.server.message.PlayerStateUpdate;
import everyos.ggd.server.message.imp.MatchInitMessageImp;
import everyos.ggd.server.message.imp.PlayerInitMessageImp;
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
			setupMatch();
		}
	}

	private void setupMatch() {
		PlayerState[] playerStates = initPlayerState();
		List<SpiritState> spiritStates = initSpiritStates();
		GameState playState = new PlayGameState(matchContext, playerStates, spiritStates);
		matchContext.setGameState(new CountdownGameState(matchContext, playState));
	}

	private PlayerState[] initPlayerState() {
		Message matchInitMessage = new MatchInitMessageImp(matchContext.getMapName() + ".json");
		Player[] players = matchContext.getPlayers();
		PlayerState[] playerStates = new PlayerState[players.length];
		for (int i = 0; i < players.length; i++) {
			Player player = players[i];
			player.onMessageFromServer(matchInitMessage);
			PlayerState playerState = new PlayerStateImp(i);
			playerStates[i] = playerState;
			PlayerInitMessage initialPlayerStateMessage = createInitialPlayerStateMessage(
				player, playerState.createUpdateInfo());
			playerState.getPhysicsBody().setCurrentPosition(
				initialPlayerStateMessage.getInitialPosition());
			matchContext.broadcast(initialPlayerStateMessage);
			player.onMessageFromServer(createSessionDataSetMessage(i));
		}
		
		return playerStates;
	}

	private Message createSessionDataSetMessage(int playerNumber) {
		return new SessionDataSetMessageImp(playerNumber, matchContext.getMatchId(), "shard0");
	}

	private PlayerInitMessage createInitialPlayerStateMessage(Player player, PlayerStateUpdate playerStateUpdate) {
		return new PlayerInitMessageImp(
			getPlayerStartPosition(matchContext.getMap(), playerStateUpdate.getEntityId()),
			player.isBot(),
			playerStateUpdate);
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
	
	private List<SpiritState> initSpiritStates() {
		List<SpiritState> states = new ArrayList<>();
		int nextEntityId = 8;
		MatchMap map = matchContext.getMap();
		for (Location spiritLocation: MapUtil.getSpiritFlameTileLocations(map)) {
			Position spiritPosition = MapUtil.locationToPosition(map, spiritLocation);
			int entityId = nextEntityId++;
			SpiritState state = new SpiritStateImp(entityId, spiritPosition);
			states.add(state);
			matchContext.broadcastMessages(state.getQueuedMessages());
		}
		
		return states;
	}

}
