package everyos.ggd.server.game.vanilla;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import everyos.ggd.server.game.Match;
import everyos.ggd.server.game.Player;
import everyos.ggd.server.game.vanilla.state.game.GameState;
import everyos.ggd.server.game.vanilla.state.game.MatchSetupGameState;
import everyos.ggd.server.map.MatchMap;
import everyos.ggd.server.map.MapLoader;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.server.GGDServer;

public class VanillaMatch implements Match {
	
	private static final int FRAME_DELAY = 1000/GGDServer.FRAME_RATE;
	
	private static final String[] AVAILABLE_MAPS = new String[] {
		"map01"/*, "map02", "map03", "map04"*/
	};
	
	private final int id;
	private final Player[] players = new Player[8];
	private final MatchContext matchContext = new MatchContextImp();
	private final String mapName = chooseMap();
	private final MatchMap map = MapLoader.loadFromResourceByName(mapName);
	
	private GameState gameState = new MatchSetupGameState(matchContext);

	public VanillaMatch(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return this.id;
	}
	
	@Override
	public Player getPlayer(int playerId) {
		for (Player player: players) {
			if (player == null) {
				break;
			}
			if (player.getId() == playerId) {
				return player;
			}
		}
		return null;
	}

	@Override
	public boolean addPlayer(Player player) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] == null) {
				players[i] = player;
				return true;
			}
		}
		
		startMatch();
		
		return false;
	}
	
	private void startMatch() {
		shufflePlayers();
		startPing();
	}
	
	private String chooseMap() {
		return AVAILABLE_MAPS[(int) (Math.random() * AVAILABLE_MAPS.length)];
	}
	
	private void shufflePlayers() {
		for (int i = 0; i < players.length; i++) {
			int swapWith = (int) (Math.random() * players.length);
			Player swapPlayer = players[swapWith];
			players[swapWith] = players[i];
			players[i] = swapPlayer;
		}
	}
	
	private void startPing() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				ping();
			}
			
		}, FRAME_DELAY, FRAME_DELAY);
	}

	private void ping() {
		gameState.ping();
	}
	
	private class MatchContextImp implements MatchContext {

		@Override
		public Player[] getPlayers() {
			return players;
		}

		@Override
		public void setGameState(GameState newGameState) {
			gameState = newGameState;
			gameState.start();
		}
		
		@Override
		public MatchMap getMap() {
			return map;
		}

		@Override
		public String getMapName() {
			return mapName;
		}

		@Override
		public int getMatchId() {
			return id;
		}
		
		@Override
		public void broadcast(Message message) {
			for (Player player: players) {
				player.onMessageFromServer(message);
			}
		}
		
		@Override
		public void broadcastMessages(List<Message> messages) {
			for (Message message: messages) {
				matchContext.broadcast(message);
			}
		}

		@Override
		public void rebroadcast(Message message, int broadcasterId) {
			for (Player player: players) {
				if (player.getId() == broadcasterId) {
					continue;
				}
				player.onMessageFromServer(message);
			}
		}
		
	}
	
}
