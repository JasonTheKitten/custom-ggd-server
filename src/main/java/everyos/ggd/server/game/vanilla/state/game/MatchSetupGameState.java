package everyos.ggd.server.game.vanilla.state.game;

import java.util.ArrayList;
import java.util.List;

import everyos.ggd.server.game.Player;
import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.game.vanilla.state.game.play.PlayerStateEventListenerImp;
import everyos.ggd.server.game.vanilla.state.player.PlayerState;
import everyos.ggd.server.game.vanilla.state.player.PlayerStateImp;
import everyos.ggd.server.game.vanilla.state.spirit.SpiritState;
import everyos.ggd.server.game.vanilla.state.spirit.SpiritStateImp;
import everyos.ggd.server.game.vanilla.util.MapUtil;
import everyos.ggd.server.map.MatchMap;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.imp.MatchInitMessageImp;
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
		sendMatchInitMessage();
		PlayerState[] playerStates = initPlayerState();
		List<SpiritState> mapSpiritStates = initMapSpiritStates();
		sendQueuedEntityMessages();
		sendSessionDataMessages();
		GameState playState = new PlayGameState(matchContext, playerStates, mapSpiritStates);
		matchContext.setGameState(new CountdownGameState(matchContext, playState));
	}

	private void sendMatchInitMessage() {
		Message matchInitMessage = new MatchInitMessageImp(matchContext.getMapName() + ".json");
		matchContext.broadcast(matchInitMessage);
	}
	
	private void sendSessionDataMessages() {
		Player[] players = matchContext.getPlayers();
		for (int i = 0; i < players.length; i++) {
			players[i].onMessageFromServer(createSessionDataSetMessage(i));
		}
	}
	
	private Message createSessionDataSetMessage(int playerNumber) {
		return new SessionDataSetMessageImp(playerNumber, matchContext.getMatchId(), "shard0");
	}

	private PlayerState[] initPlayerState() {
		Player[] players = matchContext.getPlayers();
		PlayerState[] playerStates = new PlayerState[players.length];
		for (int i = 0; i < players.length; i++) {
			Player player = players[i];
			playerStates[i] = createPlayerState(player, i);
		}
		
		return playerStates;
	}

	private PlayerState createPlayerState(Player player, int characterId) {
		PlayerState playerState = matchContext
			.getEntityRegister()
			.createEntity(id -> new PlayerStateImp(id, player.isBot(), new PlayerStateEventListenerImp()));
		playerState.getPhysicsBody().setCurrentPosition(getPlayerStartPosition(matchContext.getMap(), characterId));
		
		return playerState;
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
	
	private List<SpiritState> initMapSpiritStates() {
		List<SpiritState> states = new ArrayList<>();
		MatchMap map = matchContext.getMap();
		for (Location spiritLocation: MapUtil.getSpiritFlameTileLocations(map)) {
			Position spiritPosition = MapUtil.locationToPosition(map, spiritLocation);
			SpiritState state = createMapSpiritState(spiritPosition);
			states.add(state);
			matchContext.broadcastMessages(state.getQueuedMessages());
		}
		
		return states;
	}
	
	private SpiritState createMapSpiritState(Position spiritPosition) {
		return matchContext
			.getEntityRegister()
			.createEntity(id -> new SpiritStateImp(id, spiritPosition));
	}

	private void sendQueuedEntityMessages() {
		matchContext.broadcastMessages(matchContext.getEntityRegister().getQueuedMessages());
	}

}
